package Control;

import View.Support.DcsWebContext;

public class ConfigClass {
    //////////////////////
    //  USER CHANGEABLE //
    //////////////////////

    // changes db table names from lowercase to camelcase if true and some other things
    public final static boolean RUNNING_ON_SERVER = true;

    // whether debug output is turned on
    public final static boolean DEBUG = false;
    /*
        whether to send emails via gmail api - disabled by default
        and also disable some servlets from working by making them not working in production
     */
    public final static boolean PRODUCTION = true;

    // in TimakCommonFiles there is a setup File for creation database, the number should match
    public final static int CODE_SUPPOSED_TO_WORK_WITH_SQL_VERSION = 23;

    //////////////////////
    //  DCS SPECIFIC    //
    //////////////////////
    public final static int VERIFICATION_CODE_LENGTH = 14;  // length of verification code created to the user
    public final static String JNDI_DATASOURCE_NAME = "jdbc/MySQLDataSource";   // datasource
    public final static String DCS_CHECKING_CODE = null;    // protection against malicious post requests

    // URLS
    public final static String DEPLOYED_ON_BASE_URL = (RUNNING_ON_SERVER == true) ? DcsWebContext.SERVER_BASE_URL : DcsWebContext.LOCALHOST_BASE_URL;
    public final static String URL_BASE_LOCAL = "/dcs";
    public final static String URL_BASE_SERVER = "/dcs";

    // HTML variable name that tells if we are running remotely
    public final static String HTML_VARIABLENAME_RUNNINGREMOTELY = "trueIfRunningRemotely";
}
