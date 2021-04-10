package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_CreateTypes;
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

@WebServlet(name = "Admin_SensorTypeCreateServlet", urlPatterns = Admin_SensorTypeCreateServlet.SERVLET_URL)
public class Admin_SensorTypeCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/sensor-type/create";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-sensortype_create.html";

    private static final String VARIABLE_COMM_TYPES = "commTypes";

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

        final List<CommType> commTypeList = (new UC_CreateTypes(getDb()).getAllCommType());
        context.setVariable(VARIABLE_COMM_TYPES, commTypeList);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        SensorType sensorType = (SensorType) PrettyObject.parse(ServletHelper.RequestBody(request), SensorType.class);

        final JsonResponse jsonResponse = (new UC_CreateTypes(getDb()).createSensorType(sensorType));
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
