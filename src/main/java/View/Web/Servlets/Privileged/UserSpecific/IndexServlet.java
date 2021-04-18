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

    private static final String VARIABLE_ISADMIN = "isAdmin";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        boolean isAdmin = super.checkIfPrivilegeIsAdmin(request);

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);


        // example accessing session attributes
        User user = SessionUtil.getUser(request.getSession(false));

        context.setVariable("Email", user.getEmail());

        context.setVariable("BeforeTitle", user.getBeforetitle());
        context.setVariable("Phone", user.getPhone());
        context.setVariable("LoggedAddress", user.getResidence());
        context.setVariable("FirstName", user.getFirstname());
        context.setVariable("MiddleName", user.getMiddlename());
        context.setVariable("LastName", user.getLastname());

        context.setVariable(VARIABLE_ISADMIN, isAdmin);

        // part 4
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
