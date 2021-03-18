package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_Address;
import Model.Web.Auth;
import Model.Web.User;

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

    public ArrayList<T_Address> retrieveAllAddress() { // TODO remove this method later
        try {
            return Address.retrieveAll(db.getConn(), db.getPs(), db.getRs());
        } catch (SQLException e) {
            CustomLogs.Error(e.getMessage());
        }
        return null;
    }

    public boolean createAuth(final Auth auth) {
        CustomLogs.Error(auth.toStringPretty()); // remove this line
        return true;
        /*
        try {
            // TODO save auth.getUser() to table User
            //      save auth.getPassword() to table Hash-Value, password is already hashed
            //      process also auth.isadmin() appropriately
            return true;
        } catch (SQLException e) {

        }
        return false; if email already exists
        */
    }

    public Auth retrieveAuthByEmail(final String email) {
        Auth auth = new Auth(); // remove this
        auth.setVerificationcode("xxx");
        auth.setPassword("xxx");
        return auth;
        /*
        try {
            return // TODO retrieve object that contains User and password hash from database
        } catch (SQLException e) {

        }
        return null; if email not in database
        */
    }

    public boolean updateAuth(final Auth auth) { // this will be re-used for user edit later
        CustomLogs.Error(auth.toStringPretty()); // remove this line
        return true;
        /*
        try {
            // TODO similar to createAuth but update
            return true;
        } catch (SQLException e) {

        }
        return false; if operation fails
        */
    }
}
