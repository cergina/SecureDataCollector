package Database.Support;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class JSONHelper {
    public static JSONObject ReturnBodyIfValid(HttpServletRequest req, String typeOfRequest, String url) throws ServletException, Exception {
        // Base
        req.setCharacterEncoding("UTF-8");

        // get body of request
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // verify empty body
        if (body.length() <= 0) {
            throw new ServletException("Exception while processing " + typeOfRequest +  " from " + url + " | Cause: Empty body");
        }

        // verify JSON validity
        JSONObject json;
        try {
            json = new JSONObject(body);
        } catch (JSONException e) {
            throw new JSONException("Exception while processing " + typeOfRequest + " from " + url + " | Cause: JSON object is not a valid object");
        }

        // return valid non empty json object to be parsed afterwards
        return json;
    }
}
