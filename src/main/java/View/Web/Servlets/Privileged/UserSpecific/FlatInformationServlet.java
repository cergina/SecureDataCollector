package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Control.Scenario.UC_FlatSummary;
import Model.Database.Support.CustomLogs;
import Model.Web.thymeleaf.Flat;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.SessionServlet;
import View.Support.ServletHelper;
import View.Support.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FlatInformationServlet", urlPatterns = FlatInformationServlet.SERVLET_URL)
public class FlatInformationServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/action/projects/flats";
    public static final String TEMPLATE_NAME = "views/privileged/my_flat.html";

    private static final String VARIABLE_FLAT = "flat";

    private static final String REQUEST_PARAM_FLAT_ID = "fid";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
//        if (checkPrivilege(request, response) == false) { // TODO uncomment back once UC finished
//            ServletHelper.SendReturnCode(response, HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }

        CustomLogs.Development("Sme vo doGet");

        // has to have some request for flat id
        int requestedFlatId;
        try {
            requestedFlatId = Integer.parseInt(request.getParameter(REQUEST_PARAM_FLAT_ID));
            CustomLogs.Development("V requeste prisiel flat id: " + requestedFlatId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?fid=[number should be here]");
            ServletHelper.SendReturnCode(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

//        User user = SessionUtil.getUser(request.getSession(false));
        UC_FlatSummary uc = new UC_FlatSummary(getDb());
        if (uc.doesUserHaveRightToSeeProjectBelongingToFlat(1, requestedFlatId) == false) {
            // TODO send 403
            return;
        }
        Flat flat = uc.getFlatSummary(requestedFlatId);
        if (flat == null) { // toto podla mna asi ani nenastane lebo je to zahrnute uz v predoslom
            // TODO send 404
            return;
        }
        context.setVariable(VARIABLE_FLAT, flat);

        // Generate html and return it
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}