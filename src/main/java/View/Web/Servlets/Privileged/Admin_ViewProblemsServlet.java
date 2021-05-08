package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Problems;
import Model.Web.Specific.Problem;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Admin_ViewProblemsServlet", urlPatterns = Admin_ViewProblemsServlet.SERVLET_URL)
public class Admin_ViewProblemsServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/problems";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-problems_view.html";

    private static final String VARIABLE_PROBLEMS = "problems";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            return;
        }

        //
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        final List<Problem> problems = (new UC_Problems(getDb()).getProblems());
        context.setVariable(VARIABLE_PROBLEMS, problems);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }


}
