package Database.Support;

import Database.Interaction.Presentation.Servlets.GET_Addresses;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CustomLogs {
    //Logging
    private static final String LOG_FILE = "log4j.properties";
    private static Logger logger = Logger.getLogger(CustomLogs.class);
    private static Properties properties = new Properties();

    public static void DatabaseLog(boolean isError, String msg) {
        try {
            properties.load(new FileInputStream(LOG_FILE));
            PropertyConfigurator.configure(properties);

            if (isError)
                logger.error(msg);
            else
                logger.info(msg);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*public static void ServletLog(boolean isError, String msg) throws IOException {
        properties.load(new FileInputStream(LOG_FILE));
        PropertyConfigurator.configure(properties);

        if (isError)
            logger.error(msg);
        else
            logger.info(msg);
    }*/
}
