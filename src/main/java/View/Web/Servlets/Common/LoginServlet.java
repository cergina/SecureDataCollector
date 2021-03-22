package View.Web.Servlets.Common;

import Control.ConfigClass;
import Control.Scenario.ExampleUseCase;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.UserAccessHelper;
import Model.Web.Auth;
import Model.Web.User;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = LoginServlet.SERVLET_URL)
public class LoginServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/login";
    public static final String TEMPLATE_NAME = "login.html";

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

        if (authWithId == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.println("No such email.");
        } else if (!authWithId.getKey().getPassword().equals(passwordHash)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.println("Password does not match.");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("email", authReq.getUser().getEmail()); // TODO session attrs to be decided after layouts
            session.setAttribute("user", authReq.getUser());
            session.setAttribute("userID", authWithId.getValue());

            response.setStatus(HttpServletResponse.SC_OK);
            writer.println("Login successful.");
        }

        writer.close();
    }
}
