package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Control.Scenario.UC_ProjectListing;
import Control.Scenario.UC_UserListing;
import Model.Database.Support.CustomLogs;
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
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProjectsInformationServlet", urlPatterns = ProjectsInformationServlet.SERVLET_URL)
public class ProjectsInformationServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/action/projects";
    public static final String TEMPLATE_NAME = "views/privileged/my_projects.html";

    private static final String VARIABLE_PROJECTS = "projects";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<Project> projects;
        if (SessionUtil.getIsadmin(request.getSession(false))) {
            // all projects for admin
            projects = (new UC_ProjectListing(getDb())).allProjects();
        } else {
            // own projects for user
            User user = SessionUtil.getUser(request.getSession(false));
            projects = (new UC_ProjectListing(getDb())).allProjectsForUser(user.getUserID());
        }

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        context.setVariable(VARIABLE_PROJECTS, projects);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
