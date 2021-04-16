package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Model.Database.Support.CustomLogs;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.AdminEditableUserViewableServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CentralUnitInformationServlet", urlPatterns = CentralUnitInformationServlet.SERVLET_URL)
public class CentralUnitInformationServlet extends AdminEditableUserViewableServlet {
    public static final String SERVLET_URL =  "/action/centralUnits";
    public static final String TEMPLATE_NAME = "views/privileged/my_centralunit.html";
    public static final String ONETIME_TEMPLATE_NAME = "views/adminOnly/admin-centralunit-when_no_controllers.html";

    private static final String VARIABLE_CENTRALUNIT = "centralUnit";
    private static final String VARIABLE_FLATS_LIST = "flatsList";
    private static final String VARIABLE_ISADMIN = "isAdmin";
    private static final String REQUEST_PARAM_CENTRAL_UNIT_ID = "id";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            processSingleCentralUnit(request, response);
        } else {
            processForAllCentralUnits(request, response);
        }
    }

    private void processSingleCentralUnit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // has to have some request for controller unit id
        int requestedCentralUnitId;
        try {
            requestedCentralUnitId = Integer.parseInt(request.getParameter(REQUEST_PARAM_CENTRAL_UNIT_ID));
            CustomLogs.Development("V requeste prisiel central unit id: " + requestedCentralUnitId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // if no controllers do one time view insert of
        int count = (new UC_FlatSummary(getDb())).countNumberOfControllersForCentralUnit(requestedCentralUnitId);

        if (count == 0) {
            processYetSingleEmptyCentralUnit(request, response, requestedCentralUnitId);
            return;
        }

        // only continue this way when there is something to show
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        Model.Web.CentralUnit centralUnit = (new UC_FlatSummary(getDb())).get_CentralUnitWithFlats(requestedCentralUnitId);
        if (centralUnit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Variables settings
        context.setVariable(VARIABLE_FLATS_LIST, centralUnit.flats);
        context.setVariable(VARIABLE_CENTRALUNIT, centralUnit);
        context.setVariable(VARIABLE_ISADMIN, super.checkIfPrivilegeIsAdmin(request));

        // Generate html and return it
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    private void processYetSingleEmptyCentralUnit(HttpServletRequest request, HttpServletResponse response, int requestedCentralUnitId) throws IOException {
        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        Model.Web.CentralUnit centralUnit = (new UC_FlatSummary(getDb())).get_CentralUnit(requestedCentralUnitId);
        if (centralUnit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Variables settings
        context.setVariable(VARIABLE_CENTRALUNIT, centralUnit);
        context.setVariable(VARIABLE_ISADMIN, super.checkIfPrivilegeIsAdmin(request));

        // Generate html and return it
        engine.process(ONETIME_TEMPLATE_NAME, context, response.getWriter());
    }

    private void processForAllCentralUnits(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // admin sees  every central unit
        // eg.: list of central units of project 1, list of central units of project 2, ...

        // user sees only his central units within his projects
        // eg.: list of central units of project 2, list of central units of project 13, ...
    }
}
