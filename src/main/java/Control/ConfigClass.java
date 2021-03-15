package Control;

public class ConfigClass {
    // whether debug output is turned on
    public static boolean DEBUG = false;

    public static String JNDI_DATASOURCE_NAME = "jdbc/MySQLDataSource";

    // changes db table names from lowercase to camelcase if true and some other things
    public static boolean RUNNING_ON_SERVER = true;

    // HTML variable name that tells if we are running remotely
    public static String HTML_VARIABLENAME_RUNNINGREMOTELY = "trueIfRunningRemotely";

    // URLS
    public static String URL_BASE_LOCAL = "/dcs";
    public static String URL_BASE_SERVER = "/dcs";
}
