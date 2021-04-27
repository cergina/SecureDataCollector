package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Control.Scenario.UC_Addresses;
import Model.Enums.Countries;
import Model.Web.Address;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
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

@WebServlet(name = "Admin_AddressCreateServlet", urlPatterns = Admin_AddressCreateServlet.SERVLET_URL)
public class Admin_AddressCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/addresses/create";
    public static final String TEMPLATE_NAME = "views/adminOnly/admin-address_create.html";

    private static final String VARIABLE_COUNTRIES_LIST = "countriesList";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            return;
        }

        TemplateEngine engine = ContextUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response,
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        context.setVariable(VARIABLE_COUNTRIES_LIST, Countries.GetAll_CountriesAsStringList());

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        // parse JSON from Body object
        Address address = (Address) PrettyObject.parse(ServletHelper.RequestBody(request), Address.class);

        final JsonResponse jsonResponse = (new UC_Addresses(getDb()).createNewAddress(address));

        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse);
        writer.close();
    }

}

