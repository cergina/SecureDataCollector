package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Model.Web.CentralUnit;
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

@WebServlet(name = "CentralUnitInformationServlet", urlPatterns = CentralUnitInformationServlet.SERVLET_URL)
public class CentralUnitInformationServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/action/centralUnits";
    public static final String TEMPLATE_NAME_SINGLE = "views/privileged/my_centralUnit.html";
    public static final String ONETIME_TEMPLATE_NAME = "views/adminOnly/admin-centralunit-when_no_controllers.html";

    private static final String VARIABLE_CENTRALUNIT = "centralUnit";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            processSingle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Decider between empty and already initialized central unit
     */
    private void processSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer requestedId = ServletHelper.getRequestParamId(request);
        if (requestedId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // if no controllers do one time view insert of
        int count = (new UC_FlatSummary(getDb())).countNumberOfControllersForCentralUnit(requestedId);

        processSingleCentralUnit(request, response, requestedId);
    }

    /**
     * specific central unit view
     */
    private void processSingleCentralUnit(HttpServletRequest request, HttpServletResponse response, int requestedCentralUnitId) throws IOException {
        // only continue this way when there is something to show
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        CentralUnit centralUnit = (new UC_FlatSummary(getDb())).get_CentralUnitWithFlats(requestedCentralUnitId);
        if (centralUnit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Variables settings
        context.setVariable(VARIABLE_LOGGED_USER, SessionUtil.getUser(request.getSession(false)));
        context.setVariable(VARIABLE_CENTRALUNIT, centralUnit);
        context.setVariable(VARIABLE_ISADMIN, super.checkIfPrivilegeIsAdmin(request));

        // Generate html and return it
        engine.process(TEMPLATE_NAME_SINGLE, context, response.getWriter());
    }
}
