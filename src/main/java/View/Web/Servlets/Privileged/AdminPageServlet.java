package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import View.Configuration.TemplateEngineUtil;
import View.Support.DcsWebContext;
import View.Web.Servlets.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminPageServlet", urlPatterns = AdminPageServlet.SERVLET_URL)
public class AdminPageServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/admin/view";
    public static final String TEMPLATE_NAME = "admin-view.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        context.setVariable("loggedUser", "Maroš Čergeť");
        context.setVariable("privileges", "admin");

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
