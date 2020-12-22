package Control;

public class ConfigClass {
    public static boolean DEBUG_CONSOLE_OUTPUT = true;

    public static String JNDI_DATASOURCE_NAME = "jdbc/MySQLDataSource";

    // changes db table names from lowercase to camelcase if true
    public static boolean RUNNING_ON_SERVER = true;

}
