package View.Web.Servlets.Public;

import Control.ConfigClass;
import Control.Scenario.UC_Auth;
import Model.Web.Auth;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.PublicServlet;
import View.Support.ServletHelper;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = LoginServlet.SERVLET_URL)
public class LoginServlet extends PublicServlet {
    public static final String SERVLET_URL =  "/login";
    public static final String TEMPLATE_NAME = "authentication/login.html";

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

        // parse JSON from Body object as Auth java representation
        Auth auth = (Auth) PrettyObject.parse(ServletHelper.RequestBody(request), Auth.class);

        final JsonResponse jsonResponse = (new UC_Auth(getDb())).authenticateUser(auth); // login user
        response.setStatus(jsonResponse.getStatus());

        if (jsonResponse.getStatus() == HttpServletResponse.SC_OK) {
            // initiate session
            HttpSession session = request.getSession();
            SessionUtil.setUser(session, ((Auth) jsonResponse.getData()).getUser());
            SessionUtil.setIsadmin(session, ((Auth) jsonResponse.getData()).getIsadmin());

            // add log to database
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            (new UC_Auth(getDb())).LogLoginIntoTheDatabase(SessionUtil.getUser(session).getUserID(), ipAddress);
        }

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
