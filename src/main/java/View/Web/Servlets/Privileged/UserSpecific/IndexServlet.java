package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminEditableUserViewableServlet;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "IndexServlet", urlPatterns = IndexServlet.SERVLET_URL)
public class IndexServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/index";
    public static final String TEMPLATE_NAME = "index.html";


    private static final String VARIABLE_USER = "user";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(ConfigClass.DEPLOYED_ON_BASE_URL + "/login");
            return;
        }

        boolean isAdmin = super.checkIfPrivilegeIsAdmin(request);

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);


        // example accessing session attributes
        User user = SessionUtil.getUser(request.getSession(false));

        context.setVariable(VARIABLE_USER, user);
        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));
        context.setVariable(VARIABLE_ISADMIN, isAdmin);

        // part 4
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
