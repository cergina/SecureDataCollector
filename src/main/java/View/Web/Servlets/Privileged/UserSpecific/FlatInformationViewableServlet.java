package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Control.Scenario.UC_Graph;
import Model.Database.Support.CustomLogs;
import Model.Web.JsonResponse;
import Model.Web.Specific.GraphSingleFlat;
import Model.Web.User;
import Model.Web.Flat;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminEditableUserViewableServlet;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FlatInformationServlet", urlPatterns = FlatInformationViewableServlet.SERVLET_URL)
public class FlatInformationViewableServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/action/projects/flats";
    public static final String TEMPLATE_NAME = "views/privileged/my_flat.html";

    private static final String VARIABLE_FLAT = "flat";
    private static final String VARIABLE_ISADMIN = "isAdmin";
    private static final String REQUEST_PARAM_FLAT_ID = "fid";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION

        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(ConfigClass.DEPLOYED_ON_BASE_URL + "/login");
            return;
        }

        boolean isAdmin = super.checkIfPrivilegeIsAdmin(request);

        CustomLogs.Development("Sme vo doGet");

        // has to have some request for flat id
        int requestedFlatId;
        try {
            requestedFlatId = Integer.parseInt(request.getParameter(REQUEST_PARAM_FLAT_ID));
            CustomLogs.Development("V requeste prisiel flat id: " + requestedFlatId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?fid=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        /* find out if he has access to the project */
        User user = SessionUtil.getUser(request.getSession(false));
        UC_FlatSummary uc = new UC_FlatSummary(getDb());
        if (uc.doesUserHaveRightToSeeProjectBelongingToFlat(user.getUserID(), requestedFlatId) == false) {
            if (isAdmin == false) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        /* this will not happen - then why is it here */
        Flat flat = uc.getFlatSummary(requestedFlatId);
        if (flat == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        context.setVariable(VARIABLE_FLAT, flat);
        context.setVariable(VARIABLE_ISADMIN, isAdmin);

        // Generate html and return it
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        // has to have some request for flat id
        int requestedFlatId;
        try {
            requestedFlatId = Integer.parseInt(request.getParameter(REQUEST_PARAM_FLAT_ID));
            CustomLogs.Development("V POST requeste prisiel flat id: " + requestedFlatId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?fid=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        GraphSingleFlat graph = new GraphSingleFlat(new UC_Graph(getDb()).getDatesAsLabelsOfLast30Days(), new UC_Graph(getDb()).getSensorsForFlat(requestedFlatId));

        final JsonResponse jsonResponse = (new UC_Graph(getDb()).dataForGraph(graph));

        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }

}
