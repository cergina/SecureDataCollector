package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_ListUserCreations;
import Model.Web.Specific.UserCreation;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Admin_ViewUserCreationsServlet", urlPatterns = Admin_ViewUserCreationsServlet.SERVLET_URL)
public class Admin_ViewUserCreationsServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/user/creations";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-user_creations_view.html";

    private static final String VARIABLE_CREATIONS = "creations";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            return;
        }

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        final List<UserCreation> creations = (new UC_ListUserCreations(getDb()).getUserCreations());
        context.setVariable(VARIABLE_CREATIONS, creations);
        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }


}
