package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_Graph;
import Control.Scenario.UC_ListBuilding;
import Control.Scenario.UC_ListCentralUnit;
import Control.Scenario.UC_ListProject;
import Model.Database.Support.CustomLogs;
import Model.Web.Building;
import Model.Web.CentralUnit;
import Model.Web.JsonResponse;
import Model.Web.Specific.GraphSingleBuilding;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.SessionServlet;
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

@WebServlet(name = "BuildingsInformationServlet", urlPatterns = BuildingsInformationServlet.SERVLET_URL)
public class BuildingsInformationServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/action/buildings";
    public static final String TEMPLATE_NAME_SINGLE = "views/privileged/my_building.html";

    private static final String VARIABLE_BUILDING = "building";
    private static final String VARIABLE_CENTRAL_AT_SAME_ADDRESS = "centralsAtSameAddress";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            processSingle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
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

        Building building = (new UC_ListBuilding(getDb())).specificBuilding(requestedId);
        if (building == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HttpSession session = request.getSession(false);
        if (!SessionUtil.getIsadmin(session)) { // is not admin?
            User user = SessionUtil.getUser(session);
            if (!(new UC_ListProject(getDb())).isUserInProject(building.getProjectId(), user.getUserID())) { // is not authorized to see?
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // find all central units at the same address as is the building
        List<CentralUnit> centralsAtSameAddress = (new UC_ListCentralUnit(getDb())).centralUnitsOnTheSameAddressNameAsBuilding(building.getId());

        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));
        context.setVariable(VARIABLE_BUILDING, building);
        context.setVariable(VARIABLE_CENTRAL_AT_SAME_ADDRESS, centralsAtSameAddress);
        context.setVariable(VARIABLE_ISADMIN, SessionUtil.getIsadmin(request.getSession(false)));

        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        // has to have some request for building id
        Integer requestedId = ServletHelper.getRequestParamId(request);
        try {
            CustomLogs.Development("V requeste prisiel building id: " + requestedId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UC_Graph graphScenario = new UC_Graph(getDb());
        GraphSingleBuilding graph = new GraphSingleBuilding(graphScenario.getDatesAsLabelsOfLast30Days(), graphScenario.getFlatsForBuilding(requestedId));

        final JsonResponse jsonResponse = (graphScenario.dataForGraphBuilding(graph));

        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse);
        writer.close();

    }


}
