package Database.Support;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class ServletHelper {
    public static void Send404(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public static void Send417(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
    }

    public static String ReturnBodyIfValid(HttpServletRequest req, String typeOfRequest, String url) throws ServletException, Exception {
        // get body of request
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // verify empty body
        if (body.length() <= 0) {
            throw new ServletException("Exception while processing " + typeOfRequest + " from " + url + " | Cause: Empty body");
        }

        return body;
    }
}
