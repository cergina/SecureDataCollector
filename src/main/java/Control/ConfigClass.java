package Control;

public class ConfigClass {
    // whether debug output is turned on
    public final static boolean DEBUG = false;

    public final static String JNDI_DATASOURCE_NAME = "jdbc/MySQLDataSource";

    // changes db table names from lowercase to camelcase if true and some other things
    public final static boolean RUNNING_ON_SERVER = false;
    // in TimakCommonFiles there is a setup File for creation database, the number should match
    public final static int CODE_SUPPOSED_TO_WORK_WITH_SQL_VERSION = 16;

    // HTML variable name that tells if we are running remotely
    public final static String HTML_VARIABLENAME_RUNNINGREMOTELY = "trueIfRunningRemotely";

    // URLS
    public final static String URL_BASE_LOCAL = "/dcs";
    public final static String URL_BASE_SERVER = "/dcs";

    // DCS SPECIFIC
    public final static int VERIFICATION_CODE_LENGTH = 14;
}
