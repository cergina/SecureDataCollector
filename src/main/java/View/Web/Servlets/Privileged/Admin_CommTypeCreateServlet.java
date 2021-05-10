package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Types;
import Model.Web.CommType;
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

@WebServlet(name = "Admin_CommTypeCreateServlet", urlPatterns = Admin_CommTypeCreateServlet.SERVLET_URL)
public class Admin_CommTypeCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/comm-type/create";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-commtype_create.html";

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

        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        CommType commType = (CommType) PrettyObject.parse(ServletHelper.RequestBody(request), CommType.class);

        final JsonResponse jsonResponse = (new UC_Types(getDb()).createCommType(commType));
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse);
        writer.close();
    }
}
