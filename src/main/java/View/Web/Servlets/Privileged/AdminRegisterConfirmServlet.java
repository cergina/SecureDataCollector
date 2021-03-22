package View.Web.Servlets.Privileged;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminRegisterConfirmServlet", urlPatterns = AdminRegisterConfirmServlet.SERVLET_URL)
public class AdminRegisterConfirmServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/admin-register-confirm";
    public static final String TEMPLATE_NAME = "admin-register-confirm.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        HttpSession session = request.getSession(false);
        context.setVariable("VerificationCode", session.getAttribute("verificationCode"));

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
