package View.Support;

import Model.Database.Support.CustomLogs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class ServletHelper {

    private static final String REQUEST_PARAM_ID = "id";

    public static String ReturnBodyIfValid(HttpServletRequest req, String typeOfRequest, String url) throws ServletException, IOException {
        // get body of request
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // verify empty body
        if (body.length() <= 0) {
            throw new ServletException("Exception while processing " + typeOfRequest + " from " + url + " | Cause: Empty body");
        }

        return body;
    }

    public static HttpServletRequest ProcessRequest_forDoGet_First(HttpServletRequest req) throws IOException{
        req.setCharacterEncoding("UTF-8");

        return req;
    }

    public static HttpServletResponse PrepareResponse_forDoGet_First(HttpServletResponse resp) throws IOException{
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");

        return resp;
    }

    public static HttpServletResponse PrepareResponse_forDoPost_First(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        return resp;
    }

    /**
     * Extract body string from HTTP request
     */
    public static String RequestBody(HttpServletRequest request) {
        String bodyString = null;
        try {
            bodyString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            CustomLogs.Error(e.getMessage());
        }
        return bodyString;
    }

    /**
     * Extract ID from
     * <URL>?id=<int:ID>
     */
    public static Integer getRequestParamId(HttpServletRequest request) throws IOException {
        Integer requestedId = null;
        try {
            requestedId = Integer.parseInt(request.getParameter(REQUEST_PARAM_ID));
            CustomLogs.Development("V requeste prisiel id: " + requestedId);
        } catch (NumberFormatException nfe) {
            CustomLogs.Error("Bad request or nothing came into server as ?id=[number should be here]");
        }
        return requestedId;
    }
}
