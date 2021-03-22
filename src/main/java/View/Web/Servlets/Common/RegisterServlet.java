package View.Web.Servlets.Common;

import Control.ConfigClass;
import Control.Scenario.ExampleUseCase;
import Model.Database.Support.UserAccessHelper;
import Model.Web.Auth;
import View.Configuration.TemplateEngineUtil;
import View.Support.DcsWebContext;
import View.Support.ServletHelper;
import View.Web.Servlets.ConnectionServlet;
import javafx.util.Pair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet", urlPatterns = RegisterServlet.SERVLET_URL)
public class RegisterServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/register";
    public static final String TEMPLATE_NAME = "register.html";

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
        String passwordHash = UserAccessHelper.hashPassword(authReq.getPassword());

        Pair<Auth,Integer> authWithId = (new ExampleUseCase(dbProvider))
                .retrieveAuthByEmail(authReq.getUser().getEmail());

        // TODO Veronika's exceptions
        if (authWithId == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.println("No such email.");
        } else if (!authWithId.getKey().getPassword().equals(authReq.getVerificationcode())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.println("Verification code does not match.");
        } else {
            if ((new ExampleUseCase(dbProvider)).updateAuthPassword(passwordHash, authWithId.getValue())) {
                response.setStatus(HttpServletResponse.SC_OK);
                writer.println("Registration complete.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.println("Registration error");
            }
        }

        writer.close();
    }
}
