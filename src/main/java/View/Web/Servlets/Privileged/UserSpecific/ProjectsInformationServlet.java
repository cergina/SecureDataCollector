package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_Addresses;
import Control.Scenario.UC_ListProject;
import Control.Scenario.UC_ListUser;
import Model.Web.Address;
import Model.Web.Project;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.SessionServlet;
import View.Support.ServletHelper;
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

    private static final String VARIABLE_PROJECTS = "projects";
    private static final String VARIABLE_PROJECT = "project";
    private static final String VARIABLE_USERS = "users";
    private static final String VARIABLE_ADDRESS_TYPES = "addresses";

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
            projects = (new UC_ListProject(getDb())).allProjects();
        } else {
            // own projects for user
            User user = SessionUtil.getUser(request.getSession(false));
            projects = (new UC_ListProject(getDb())).allProjectsForUser(user.getUserID());
        }

        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));
        context.setVariable(VARIABLE_PROJECTS, projects);
        context.setVariable(VARIABLE_ISADMIN, SessionUtil.getIsadmin(request.getSession(false)));

        engine.process(TEMPLATE_NAME, context, response.getWriter());
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

        UC_ListProject uc = new UC_ListProject(getDb());
        HttpSession session = request.getSession(false);
        if (!SessionUtil.getIsadmin(session)) { // is not admin?
            User user = SessionUtil.getUser(session);
            if (!uc.isUserInProject(requestedId, user.getUserID())) { // is not authorized to see?
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        Project project = uc.specificProject(requestedId);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<User> users = (new UC_ListUser(getDb())).allUsersForProject(requestedId);

        final List<Address> addressList = (new UC_Addresses(getDb()).getAll_UnusedAddress());


        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));
        context.setVariable(VARIABLE_PROJECT, project);
        context.setVariable(VARIABLE_USERS, users);
        context.setVariable(VARIABLE_ADDRESS_TYPES, addressList);
        context.setVariable(VARIABLE_ISADMIN, SessionUtil.getIsadmin(request.getSession(false)));

        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
