package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomLogs.Development("Sme vo doGet");

        // Get user from session
        User user = SessionUtil.getUser(request.getSession(false));
        List<Project> projects = (new UC_FlatSummary(getDb())).allProjectsForUser(user.getUserID());

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        // part 3
        // TODO
        context.setVariable("Email", user.getEmail());
        context.setVariable("ProjectsCount", projects.size());

        // part 4
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
