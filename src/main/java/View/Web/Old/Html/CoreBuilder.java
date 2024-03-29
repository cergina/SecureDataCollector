package View.Web.Old.Html;

import Control.ConfigClass;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.DbEntity;
import View.Web.Old.Servlets.Debugging.GENERIC_INFO;

import java.util.List;

public class CoreBuilder{
    // Html start
    public static StringBuilder GenerateBaseOfSite(String  title) {
        CustomLogs.Debug("Entering GenerateBaseOfSite");

        StringBuilder document = new StringBuilder();
        document.append("<!DOCTYPE html>");
        document.append("<html>");

        document.append("<head>");
        document.append("<meta charset=\"utf-8\">");
        document.append("<meta http-equiv=\"Content-Security-Policy\" content=\"default-src 'self'>");
        document.append(title  + "<br>");

        if (ConfigClass.PRODUCTION) {
            document.append("This site is in production state and only test log is viewable // for penetration tester");
        } else {
            document.append("This site is NOT in production state. Enable it in config file and deploy again if it's not what you want.");
        }

        document.append("</head>");

        document.append("<body>");

        document = AddLinkWithName(document, GENERIC_INFO.SITE_URL + GENERIC_INFO.SERVLET_URL, GENERIC_INFO.SITE_NAME);
        document.append("<br>");

        CustomLogs.Debug("Exiting GenerateBaseOfSite");
        return document;
    }

    public static StringBuilder FinalizeSite(StringBuilder document) {
        CustomLogs.Debug("Entering FinalizeSite");

        document.append("</body>");
        document.append("</html>");

        CustomLogs.Debug("Exiting FinalizeSite");

        return document;
    }

    /*
    *
    * */

    //CentralUnit - User
    public static <T extends DbEntity> StringBuilder GenerateDataForPresentation(StringBuilder document, List<T> arr, T objectX) {
        CustomLogs.Debug("Entering GenerateDataForPresentation");

        document = GenerateSiteWithDataPresentation(document);

        // for cycle to generate names
        document = TableHead_FillRow(document, objectX.GetTableCodeNames());

        // for cycle to generate data //arr je arraylist tadries
        for (T xxx: arr
             ) {
            document = TableData_FillRow(document, xxx);
        }

        // finish
        document = FinishGeneratingSiteWithDataPresentation(document);

        CustomLogs.Debug("Exiting GenerateDataForPresentation");
        return document;
    }

    public static StringBuilder AddLinkWithName(StringBuilder document, String link, String cover) {
        document.append("<br>");
        document.append("<a href=\"" + link + "\">" + cover + "</a>");
        return document;
    }

    /*
    *
    * */

    // Privates
    private static StringBuilder GenerateSiteWithDataPresentation(StringBuilder document) {
        document.append("<table border = \"10\" align=\"center\" width = \"80%\">");
        document.append("<tr>");
        document.append("<td>");
        document.append("<table border = \"1\" width = \"100%\">");

        // for cycle to generate names

        // for cycle to generate data

        return document;
    }

    private static StringBuilder FinishGeneratingSiteWithDataPresentation(StringBuilder document) {
        document.append("</table>");
        document.append("</td>");
        document.append("</tr>");
        document.append("</table>");

        return document;
    }

    private static StringBuilder TableHead_FillRow(StringBuilder document, String[] data) {
        document.append("<tr>");
        for (String str:data
             ) {
            document.append("<th>" + str + "</th>");
        }
        document.append("</tr>");

        return document;
    }

    private static <T extends DbEntity> StringBuilder TableData_FillRow(StringBuilder document, T data) {
        document.append("<tr>");
        for (String data_unit:data.GenerateHtmlTableRow_FromDbRow()
        ) {
            document.append("<td>" + data_unit + "</td>");
        }
        document.append("</tr>");

        return document;
    }

}
