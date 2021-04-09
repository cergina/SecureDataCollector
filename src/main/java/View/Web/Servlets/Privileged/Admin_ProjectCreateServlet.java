package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_NewProject;
import Model.Database.Support.CustomLogs;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import Model.Web.Specific.ProjectCreation;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Admin_ProjectCreateServlet", urlPatterns = Admin_ProjectCreateServlet.SERVLET_URL)
public class Admin_ProjectCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/projects/create";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-project_create.html";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        // parse JSON from Body object
        ProjectCreation projectCreation = (ProjectCreation) PrettyObject.parse(ServletHelper.RequestBody(request), ProjectCreation.class);

        CustomLogs.Error(projectCreation.getProject_name() + projectCreation.getRequired_email());
        CustomLogs.Error(projectCreation.getProject_name() + projectCreation.getRequired_email() + projectCreation.getAdditional_emails().get(0) + projectCreation.getAdditional_emails().get(1));

        final JsonResponse jsonResponse = (new UC_NewProject(getDb()).createNewProject(projectCreation)); // create new project

        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
