package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_Addresses;
import Control.Scenario.UC_ProjectListing;
import Control.Scenario.UC_UserListing;
import Model.Database.Support.CustomLogs;
import Model.Web.Address;
import Model.Web.Project;
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

@WebServlet(name = "ProjectsInformationServlet", urlPatterns = ProjectsInformationServlet.SERVLET_URL)
public class ProjectsInformationServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/action/projects";
    public static final String TEMPLATE_NAME = "views/privileged/my_projects.html";
    public static final String TEMPLATE_NAME_SINGLE = "views/privileged/my_project.html";

    private static final String VARIABLE_ISADMIN = "isAdmin";
    private static final String VARIABLE_PROJECTS = "projects";
    private static final String VARIABLE_PROJECT = "project";
    private static final String VARIABLE_USERS = "users";
    private static final String VARIABLE_ADDRESS_TYPES = "addresses";

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
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        List<Project> projects;
        if (SessionUtil.getIsadmin(request.getSession(false))) {
            // all projects for admin
            projects = (new UC_ProjectListing(getDb())).allProjects();
        } else {
            // own projects for user
            User user = SessionUtil.getUser(request.getSession(false));
            projects = (new UC_ProjectListing(getDb())).allProjectsForUser(user.getUserID());
        }

        context.setVariable(VARIABLE_PROJECTS, projects);
        context.setVariable(VARIABLE_ISADMIN, false);
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    private void processSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        int requestedProjectId;
        try {
            requestedProjectId = Integer.parseInt(request.getParameter(REQUEST_PARAM_ID));
            CustomLogs.Development("V requeste prisiel id: " + requestedProjectId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UC_ProjectListing uc = new UC_ProjectListing(getDb());
        HttpSession session = request.getSession(false);
        if (!SessionUtil.getIsadmin(session)) { // is not admin?
            User user = SessionUtil.getUser(session);
            if (!uc.isUserInProject(requestedProjectId, user.getUserID())) { // is not authorized to see?
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        Project project = uc.specificProject(requestedProjectId);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<User> users = (new UC_UserListing(getDb())).allUsersForProject(requestedProjectId);

        final List<Address> addressList = (new UC_Addresses(getDb()).getAll_UnusedAddress());


        context.setVariable(VARIABLE_PROJECT, project);
        context.setVariable(VARIABLE_USERS, users);
        context.setVariable(VARIABLE_ADDRESS_TYPES, addressList);

        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
