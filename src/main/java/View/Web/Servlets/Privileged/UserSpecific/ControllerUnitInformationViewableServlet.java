package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Control.Scenario.UC_Types;
import Model.Web.ControllerUnit;
import Model.Web.SensorType;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminEditableUserViewableServlet;
import View.Support.ServletHelper;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ControllerUnit0InformationServlet", urlPatterns = ControllerUnitInformationViewableServlet.SERVLET_URL)
public class ControllerUnitInformationViewableServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/action/controllerUnit";
    public static final String TEMPLATE_NAME = "views/privileged/my_controllerUnit.html";

    private static final String VARIABLE_CONTROLLER_UNIT = "controllerUnit";
    private static final String VARIABLE_SENSOR_TYPES = "sensorTypes";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        boolean isAdmin = super.checkIfPrivilegeIsAdmin(request);

        Integer requestedId = ServletHelper.getRequestParamId(request);
        if (requestedId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        ControllerUnit controllerUnit = (new UC_FlatSummary(getDb())).get_ControllerUnit(requestedId);
        if (controllerUnit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        final List<SensorType> sensorTypeList = (new UC_Types(getDb()).getAll_SensorType(false));
        context.setVariable(VARIABLE_SENSOR_TYPES, sensorTypeList);

        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));

        context.setVariable(VARIABLE_CONTROLLER_UNIT, controllerUnit);
        context.setVariable(VARIABLE_ISADMIN, isAdmin);

        // Generate html and return it
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
