package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_ListProject;
import Control.Scenario.UC_ListUser;
import Model.Web.Project;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;
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

        UC_ListUser uc = new UC_ListUser(getDb());
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

        Integer requestedId = ServletHelper.getRequestParamId(request);
        if (requestedId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UC_ListUser uc = new UC_ListUser(getDb());
        User user = uc.specificUser(requestedId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        context.setVariable(VARIABLE_ISADMIN, true);
        context.setVariable(VARIABLE_USER, user);

        // show his projects
        List<Project> projects = (new UC_ListProject(getDb())).allProjectsForUser(user.getUserID());
        context.setVariable(VARIABLE_PROJECTS, projects);

        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
