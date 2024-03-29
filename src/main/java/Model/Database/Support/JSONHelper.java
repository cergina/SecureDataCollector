package Model.Database.Support;

import Control.ConfigClass;
import View.Support.ServletHelper;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

import static View.Support.ServletHelper.DoesBodyContainXSS;

public class JSONHelper {
    private final static String PASS_KEY = "key";

    public static JSONObject ReturnBodyIfValid(HttpServletRequest req, String typeOfRequest, String url, boolean mandatoryKey) throws ServletException, IOException {
        // Base
        req = ServletHelper.ProcessRequest_forDoGet_First(req);

        // get body of request
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // verify empty body
        if (body.length() <= 0) {
            throw new ServletException("Exception while processing " + typeOfRequest +  " from " + url + " | Cause: Empty body");
        }

        // check for XSS
        if (DoesBodyContainXSS(body))
            throw new ServletException("XSS attempt detected");

        // verify JSON validity
        JSONObject json;
        try {
            json = new JSONObject(body);
        } catch (JSONException e) {
            throw new JSONException("Exception while processing " + typeOfRequest + " from " + url + " | Cause: JSON object is not a valid object");
        }

        // remove key always, check if its parsed
        if (mandatoryKey) {
            if (ConfigClass.DCS_CHECKING_CODE != null && ConfigClass.DCS_CHECKING_CODE.equals(json.getString(PASS_KEY)) == false)
                throw new JSONException("Exception. Invalid or unpresent password");

            json.remove(PASS_KEY);
        }

        // return valid non empty json object to be parsed afterwards
        return json;
    }
}
