package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Types;
import Model.Web.CommType;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import Model.Web.SensorType;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Admin_TypesServlet", urlPatterns = Admin_TypesServlet.SERVLET_URL)
public class Admin_TypesServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/types";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-types.html";

    private static final String VARIABLE_SENSOR_TYPES = "sensorTypes";

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

        final List<SensorType> sensorTypeList = (new UC_Types(getDb()).getAll_SensorType(true));
        context.setVariable(VARIABLE_SENSOR_TYPES, sensorTypeList);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
