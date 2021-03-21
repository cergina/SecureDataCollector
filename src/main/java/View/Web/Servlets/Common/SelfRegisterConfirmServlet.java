package View.Web.Servlets.Common;

import Control.ConfigClass;
import View.Configuration.TemplateEngineUtil;
import View.Support.DcsWebContext;
import View.Web.Servlets.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SelfRegisterConfirmServlet", urlPatterns = SelfRegisterConfirmServlet.SERVLET_URL)
public class SelfRegisterConfirmServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/register-confirm";
    public static final String TEMPLATE_NAME = "self-register-confirm.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
