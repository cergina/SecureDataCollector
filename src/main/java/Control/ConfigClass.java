package Control;

public class ConfigClass {
    // whether debug output is turned on
    public static boolean DEBUG = false;

    public static String JNDI_DATASOURCE_NAME = "jdbc/MySQLDataSource";

    // changes db table names from lowercase to camelcase if true and some other things
    public static boolean RUNNING_ON_SERVER = false;
}
