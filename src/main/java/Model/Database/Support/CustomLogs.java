package Model.Database.Support;

import Control.ConfigClass;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CustomLogs {
    public static final int DEBUG = 0;
    public static final int INFORM = 1;
    public static final int WARN = 2;
    public static final int ERR = 3;

    //Logging
    private static final String LOG_FILE = "log4j.properties";
    private static Logger logger = Logger.getLogger(CustomLogs.class);
    private static Properties properties = new Properties();

    public static void InfoLog(String msg, boolean printStackTrace) {
        if (ConfigClass.DEBUG)
            DoLog(INFORM, msg, printStackTrace);
    }

    public static void Debug(String msg) {
        if (ConfigClass.DEBUG)
            DoLog(DEBUG, msg, true);
    }

    public static void Error(String msg) {

        DoLog(ERR, msg, true);
    }

    // PRIVATES

    private static void DoLog(int level, String msg, boolean printStack) {

        try {
            properties.load(new FileInputStream(LOG_FILE));
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        PropertyConfigurator.configure(properties);

        String str[] = null;

        if (printStack){
            str = StackTraceInfo(3);
            msg = str[0] + ":" + str[3] + " | " + str[1] + "." + str[2] + "()\n" + msg;
        }

        switch (level) {
            case DEBUG:
                logger.debug(msg);
                break;
            case INFORM:
                logger.info(msg);
                break;
            case WARN:
                logger.warn(msg);
                break;
            case ERR:
                logger.error(msg);
                break;
            default:
        }
    }

    private static String[] StackTraceInfo(int pos){
        //defaultPos is 2

        String[] result = new String[4];
        Exception e = new Exception();
        result[0] = e.getStackTrace()[pos].getFileName();
        result[1] = e.getStackTrace()[pos].getClassName();
        result[2] = e.getStackTrace()[pos].getMethodName();
        result[3] = Integer.toString(e.getStackTrace()[pos].getLineNumber());

        return result;
    }
}
