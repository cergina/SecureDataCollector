package View.Web.Servlets.Common;

import Control.ConfigClass;
import Control.Scenario.ExampleUseCase;
import Model.Database.Support.CustomLogs;
import Model.Web.Auth;
import View.Configuration.TemplateEngineUtil;
import View.Support.DcsWebContext;
import View.Support.ServletHelper;
import View.Web.Servlets.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", urlPatterns = UserServlet.SERVLET_URL)
public class UserServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/user";
    public static final String TEMPLATE_NAME = "create-user.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        Auth authReq = Auth.parse(ServletHelper.RequestBody(request));
        authReq.setPassword(authReq.getVerificationcode());
        authReq.setVerificationcode("");

        if ((new ExampleUseCase(dbProvider)).createAuth(authReq)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            writer.println("User created.");
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            writer.println("Email already exists.");
        }

        writer.close();
    }
}
