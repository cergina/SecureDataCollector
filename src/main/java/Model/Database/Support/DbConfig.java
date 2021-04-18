package Model.Database.Support;

import Control.ConfigClass;

public class DbConfig {
    public static final String DS_CONTEXT_NAME = ConfigClass.JNDI_DATASOURCE_NAME;
    public static final boolean DB_USE_CAMELCASE = ConfigClass.RUNNING_ON_SERVER;

    public static final int DB_DO_NOT_USE_THIS_FILTER = 0;
}
