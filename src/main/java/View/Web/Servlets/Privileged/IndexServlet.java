package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Auth;
import Model.Database.Tables.Table.T_Address;
import Model.Web.User;
import View.Configuration.TemplateEngineUtil;
import View.Support.DcsWebContext;
import View.Support.ServletHelper;
import View.Web.Servlets.Common.LoginServlet;
import View.Web.Servlets.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "IndexServlet", urlPatterns = IndexServlet.SERVLET_URL)
public class IndexServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/index";
    public static final String TEMPLATE_NAME = "index.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        // Session
        // I know its just an example, but Code like this please, DO NOT USE if's in if's!
        HttpSession session = request.getSession(false);
        if (session == null) {
            engine.process("403.html", context, response.getWriter());
            return;
        }

        // part 2
        User user = (User) session.getAttribute(LoginServlet.SESSION_ATTR_USER);
        context.setVariable("Email", user.getEmail());

        ArrayList<T_Address> arr = (new UC_Auth(dbProvider)).retrieveAllAddress();

        // i believe that if there is nothing it's not bad request but just empty array
        // bad request would be if it would crash
        if (arr == null) {
            ServletHelper.Send404(response);
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
