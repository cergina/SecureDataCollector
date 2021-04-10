package View.Support;

import Control.ConfigClass;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DcsWebContext {
    public static WebContext WebContextInitForDCS(HttpServletRequest request, HttpServletResponse response, String runningRemotelyVariableName, boolean runningRemotely) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext());

        context.setVariable(runningRemotelyVariableName, runningRemotely);
        context.setVariable("base_url",
                ConfigClass.RUNNING_ON_SERVER ? "/dcs" : "");
        context.setVariable("deployment_url",
                ConfigClass.RUNNING_ON_SERVER ? "https://team14-20.studenti.fiit.stuba.sk/dcs" : "http://localhost:8080/dcs");

        return context;
    }
}
