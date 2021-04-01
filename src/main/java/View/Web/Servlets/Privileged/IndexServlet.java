package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Auth;
import Model.Database.Tables.Table.T_Address;
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
import java.util.List;

// TODO to be replaced with info about registrated user
@WebServlet(name = "IndexServlet", urlPatterns = IndexServlet.SERVLET_URL)
public class IndexServlet extends SessionServlet {
    public static final String SERVLET_URL =  "/index";
    public static final String TEMPLATE_NAME = "index.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response); // call always parent method first
        if (checkPrivilege(request, response) == false) {
            ServletHelper.SendReturnCode(response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);


        // example accessing session attributes
        User user = SessionUtil.getUser(request.getSession(false));

        context.setVariable("Email", user.getEmail());

        context.setVariable("BeforeTitle", user.getBeforetitle());
        context.setVariable("Phone", user.getPhone());
        context.setVariable("loggedId", user.getUserID());
        context.setVariable("logged_address", user.getResidence());
        context.setVariable("FirstName", user.getFirstname());
        context.setVariable("MiddleName", user.getMiddlename());
        context.setVariable("LastName", user.getLastname());

        List<T_Address> arr = (new UC_Auth(getDb())).retrieveAllAddress();

        if (arr == null) {
            // example sending error for GET request
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // part 3
        context.setVariable(T_Address.DBNAME_COUNTRY, "" + arr.get(0).getA_Country());
        context.setVariable(T_Address.DBNAME_CITY, "" + arr.get(0).getA_City());
        context.setVariable(T_Address.DBNAME_STREET, "" + arr.get(0).getA_Street());
        context.setVariable(T_Address.DBNAME_HOUSENO, "" + arr.get(0).getA_HouseNO());
        context.setVariable(T_Address.DBNAME_ZIP, "" + arr.get(0).getA_ZIP());

        // part 4
        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
