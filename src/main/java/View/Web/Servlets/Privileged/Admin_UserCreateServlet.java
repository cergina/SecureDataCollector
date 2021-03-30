package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Auth;
import Model.Database.Support.UserAccessHelper;
import Model.Web.Auth;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Admin_UserCreateServlet", urlPatterns = Admin_UserCreateServlet.SERVLET_URL)
public class Admin_UserCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/user/create";
    public static final String TEMPLATE_NAME = "authentication/admin_create-user.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            ServletHelper.SendReturnCode(response, HttpServletResponse.SC_UNAUTHORIZED);
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

        String verificationCode = UserAccessHelper.generateVerification(ConfigClass.VERIFICATION_CODE_LENGTH);
        auth.setVerificationcode(verificationCode);

        Integer adminID = SessionUtil.getUser(request.getSession(false)).getUserID();
        final JsonResponse jsonResponse = (new UC_Auth(getDb()).createUser(auth, adminID)); // create new user
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
