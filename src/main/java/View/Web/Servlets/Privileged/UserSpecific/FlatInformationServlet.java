package View.Web.Servlets.Privileged.UserSpecific;

import Control.ConfigClass;
import Model.Database.Support.CustomLogs;
import Model.Web.User;
import View.Configuration.ContextUtil;
import View.Support.DcsWebContext;
import View.Support.ServletAbstracts.SessionServlet;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // AUTHENTICATION
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomLogs.Development("Sme vo doGet");

        // has to have some request for flat id
        int requestedFlatId;
        try {
            requestedFlatId = Integer.parseInt(request.getParameter("fid"));
            CustomLogs.Development("V requeste prisiel flat id: " + requestedFlatId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?fid=[number should be here]");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // check if flat id belongs to user requesting it


        // get all controllers, sensors for the flat with id requested



        /* find out if project belongs to logged in user*/
        User user = SessionUtil.getUser(request.getSession(false));

        // TEMPLATE PREPARATION
        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);


        // part 3
        //context.setVariable(T_Address.DBNAME_COUNTRY, "" + arr.get(0).getA_Country());

        // part 4
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
