package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Control.Scenario.UC_Graph;
import Model.Web.Building;
import Model.Web.FlatOwner;
import Model.Web.JsonResponse;
import Model.Web.Specific.GraphSingleFlat;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminEditableUserViewableServlet;
import View.Support.ServletHelper;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "FlatInformationServlet", urlPatterns = FlatInformationViewableServlet.SERVLET_URL)
public class FlatInformationViewableServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/action/projects/flats";
    public static final String TEMPLATE_NAME_SINGLE = "views/privileged/my_flat.html";

    private static final String VARIABLE_BUILDING = "building";
    private static final String VARIABLE_FLAT_OWNERS = "flatOwners";
    private static final String VARIABLE_ISADMIN = "isAdmin";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(ConfigClass.DEPLOYED_ON_BASE_URL + "/login");
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            processSingle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        Integer requestedId = ServletHelper.getRequestParamId(request);
        if (requestedId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST); // tu ma byt odoslana JsonResponse so status kodom 400, nie takto, toto nie je GET
            return;
        }

        GraphSingleFlat graph = new GraphSingleFlat(new UC_Graph(getDb()).getDatesAsLabelsOfLast30Days(), new UC_Graph(getDb()).getSensorsForFlat(requestedId));

        final JsonResponse jsonResponse = (new UC_Graph(getDb()).dataForGraph(graph));

        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse);
        writer.close();
    }

    private void processSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        Integer requestedId = ServletHelper.getRequestParamId(request);
        if (requestedId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean isAdmin = SessionUtil.getIsadmin(session);
        UC_FlatSummary uc = new UC_FlatSummary(getDb());
        if (!isAdmin) { // is not admin?
            User user = SessionUtil.getUser(session);
            if (!uc.doesUserHaveRightToSeeProjectBelongingToFlat(user.getUserID(), requestedId)) { // is not authorized to see?
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        Building building = uc.getFlatSummary(requestedId);
        if (building == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<FlatOwner> flatOwners = uc.getFlatOwnersForFlat(requestedId);

        context.setVariable(VARIABLE_BUILDING, building);
        context.setVariable(VARIABLE_FLAT_OWNERS, flatOwners);
        context.setVariable(VARIABLE_ISADMIN, isAdmin);
        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
