package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_ProjectListing;
import Control.Scenario.UC_UserListing;
import Model.Database.Support.CustomLogs;
import Model.Web.Project;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Admin_UsersServlet", urlPatterns = Admin_UsersServlet.SERVLET_URL)
public class Admin_UsersServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/users";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-users.html";
    public static final String TEMPLATE_NAME_SINGLE = "views/adminOnly/admin-user.html";

    private static final String VARIABLE_ISADMIN = "isAdmin";
    private static final String VARIABLE_USERS = "users";
    private static final String VARIABLE_USER = "user";
    private static final String VARIABLE_PROJECTS = "projects";

    private static final String REQUEST_PARAM_ID = "id";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            processSingle(request, response);
        } else {
            processAll(request, response);
        }
    }


    /****
     * See list of all users in the system
     * @param request
     * @param response
     * @throws IOException
     */
    private void processAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        UC_UserListing uc = new UC_UserListing(getDb());
        List<User> users = uc.allUsers();

        context.setVariable(VARIABLE_ISADMIN, true);
        context.setVariable(VARIABLE_USERS, users);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    /***
     * See info about single user + his projects
     * @param request
     * @param response
     * @throws IOException
     */
    private void processSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        int requestedUserId;
        try {
            requestedUserId = Integer.parseInt(request.getParameter(REQUEST_PARAM_ID));
            CustomLogs.Development("V requeste prisiel id: " + requestedUserId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UC_UserListing uc = new UC_UserListing(getDb());
        User user = uc.specificUser(requestedUserId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        context.setVariable(VARIABLE_ISADMIN, true);
        context.setVariable(VARIABLE_USER, user);

        // show his projects
        List<Project> projects = (new UC_ProjectListing(getDb())).allProjectsForUser(user.getUserID());
        context.setVariable(VARIABLE_PROJECTS, projects);

        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
