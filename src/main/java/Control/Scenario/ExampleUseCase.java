package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_Address;
import Model.Database.Tables.Table.T_User;

import java.sql.SQLException;
import java.util.ArrayList;

/*
* THIS CLASS SHOULD NOT BE USED IN PRODUCTION
* */
public class ExampleUseCase {
    private DbProvider db;

    public ExampleUseCase(DbProvider dbProvider) {
        db = dbProvider;
    }

    public ArrayList<T_Address> retrieveAllAddress() {
        try {
            return Address.retrieveAll(db.getConn(), db.getPs(), db.getRs());
        } catch (SQLException e) {
            CustomLogs.Error(e.getMessage());
        }
        return null;
    }

    public T_User retrieveUserByEmail(String email) {
        /*try {
            return // TODO
        } catch (SQLException e) {
            CustomLogs.Error(e.getMessage());
        }*/
        return null;
    }
}
