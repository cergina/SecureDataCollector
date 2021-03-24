package View.Web.Servlets.Common;

import Control.ConfigClass;
import Control.Scenario.UC_Auth;
import Model.Database.Support.UserAccessHelper;
import Model.Web.Auth;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import View.Configuration.ContextUtil;
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

@WebServlet(name = "LoginServlet", urlPatterns = LoginServlet.SERVLET_URL)
public class LoginServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/login";
    public static final String TEMPLATE_NAME = "authentication/login.html";

    public static final String SESSION_ATTR_USER = "user";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        Auth auth = (Auth) PrettyObject.parse(ServletHelper.RequestBody(request), Auth.class);

        String passwordHash = UserAccessHelper.hashPassword(auth.getPassword()); // hash password
        auth.setPassword(passwordHash);

        final JsonResponse jsonResponse = (new UC_Auth(getDb())).authenticateUser(auth); // login user
        response.setStatus(jsonResponse.getStatus());

        if (jsonResponse.getStatus() == HttpServletResponse.SC_OK) {
            request.getSession().setAttribute(SESSION_ATTR_USER, ((Auth) jsonResponse.getData()).getUser()); // initiate session
        }

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
