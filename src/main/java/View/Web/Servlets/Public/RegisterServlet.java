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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet", urlPatterns = RegisterServlet.SERVLET_URL)
public class RegisterServlet extends PublicServlet {
    public static final String SERVLET_URL =  "/register";
    public static final String TEMPLATE_NAME = "authentication/register.html";

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

        // Check validity of user requested password to be used
        if (auth.isSuchPasswordOkay() == false) {

            JsonResponse jsonResponse = new JsonResponse();
            jsonResponse.setMessage("Attempted password does not match one of our conditions");
            response.setStatus(400);

            writer.println(jsonResponse);
            writer.close();

            return;
        }

        final JsonResponse jsonResponse = (new UC_Auth(getDb())).finishRegistration(auth); // finish registration
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse);
        writer.close();
    }
}
