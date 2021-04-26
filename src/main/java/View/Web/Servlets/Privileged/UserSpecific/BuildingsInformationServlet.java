package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_BuildingListing;
import Control.Scenario.UC_ProjectListing;
import Model.Database.Support.CustomLogs;
import Model.Web.Building;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.SessionServlet;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BuildingsInformationServlet", urlPatterns = BuildingsInformationServlet.SERVLET_URL)
public class BuildingsInformationServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/action/buildings";
    public static final String TEMPLATE_NAME = "views/privileged/my_buildings.html";
    public static final String TEMPLATE_NAME_SINGLE = "views/privileged/my_building.html";

    private static final String VARIABLE_BUILDINGS = "buildings";
    private static final String VARIABLE_BUILDING = "building";

    private static final String REQUEST_PARAM_ID = "id";

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
            processAll(request, response);
        }
    }

    private void processAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        // TEMPLATE PREPARATION
//        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
//        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
//                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);
//
//        List<Project> projects;
//        if (SessionUtil.getIsadmin(request.getSession(false))) {
//            // all projects for admin
//            projects = (new UC_ProjectListing(getDb())).allProjects();
//        } else {
//            // own projects for user
//            User user = SessionUtil.getUser(request.getSession(false));
//            projects = (new UC_ProjectListing(getDb())).allProjectsForUser(user.getUserID());
//        }
//
//        context.setVariable(VARIABLE_PROJECTS, projects);
//        context.setVariable(VARIABLE_ISADMIN, false);
//        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    private void processSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        int requestedBuildingId;
        try {
            requestedBuildingId = Integer.parseInt(request.getParameter(REQUEST_PARAM_ID));
            CustomLogs.Development("V requeste prisiel id: " + requestedBuildingId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Building building = (new UC_BuildingListing(getDb())).specificBuilding(requestedBuildingId);
        if (building == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HttpSession session = request.getSession(false);
        if (!SessionUtil.getIsadmin(session)) { // is not admin?
            User user = SessionUtil.getUser(session);
            if (!(new UC_ProjectListing(getDb())).isUserInProject(building.getProjectId(), user.getUserID())) { // is not authorized to see?
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        context.setVariable(VARIABLE_BUILDING, building);
        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
