package View.Web.Servlets.Public;

import Control.ConfigClass;
import Control.Scenario.UC_OutsiderConsumption;
import Control.Scenario.UC_Types;
import Model.Database.Support.CustomLogs;
import Model.Web.SensorType;
import Model.Web.thymeleaf.ControllerUnit;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.PublicServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ConsumptionViewServlet", urlPatterns = ConsumptionViewServlet.SERVLET_URL)
public class ConsumptionViewServlet extends PublicServlet {
    public static final String SERVLET_URL =  "/consumption-view";
    public static final String TEMPLATE_NAME = "views/consumption-view.html";


    private static final String VARIABLE_CONTROLLER_UNIT = "controllerUnit";
    private static final String VARIABLE_SENSOR_TYPES = "sensorTypes";
    private static final String REQUEST_PARAM_CONTROLLER_UNIT_UID = "uid";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // has to have some request for controller unit id
        int requestedControllerUnitUid;
        try {
            requestedControllerUnitUid = Integer.parseInt(request.getParameter(REQUEST_PARAM_CONTROLLER_UNIT_UID));
            CustomLogs.Development("V requeste prisiel controller unit uid: " + requestedControllerUnitUid);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?uid=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        ControllerUnit controllerUnit = (new UC_OutsiderConsumption(getDb())).get_ControllerUnit_ByUid(requestedControllerUnitUid);
        if (controllerUnit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        final List<SensorType> sensorTypeList = (new UC_Types(getDb()).getAll_SensorType(false)); // MAROS
        context.setVariable(VARIABLE_SENSOR_TYPES, sensorTypeList);

        context.setVariable(VARIABLE_CONTROLLER_UNIT, controllerUnit);

        // Generate html and return it
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
