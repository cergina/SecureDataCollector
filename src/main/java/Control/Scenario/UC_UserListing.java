package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_User;
import Model.Web.User;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_UserListing {
    private final DbProvider db;

    public UC_UserListing(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final List<User> allUsers() {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        List<User> temp = new ArrayList<User>();

        try {
            List<T_User> arr = InteractionWithDatabase.retrieveAll(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class));


            for (T_User  t: arr) {
                User usr = FillEntityFromTable(t);
                temp.add(usr);
            }

            // dont forget to set data that was inserted into json

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return temp;
    }

    public final User specificUser(int id) {
        User usr = null;

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            T_User t = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class), id);

            if (t != null) {
                usr = FillEntityFromTable(t);
            }

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return usr;
    }


    private User FillEntityFromTable(T_User t) {
        User usr = new User();
        usr.setEmail(t.getA_Email());
        usr.setBeforetitle(t.getA_BeforeTitle());
        usr.setFirstname(t.getA_FirstName());
        usr.setMiddlename(t.getA_MiddleName());
        usr.setLastname(t.getA_LastName());
        usr.setPhone(t.getA_Phone());
        usr.setResidence(t.getA_PermanentResidence());
        usr.setUserID(t.getA_pk());
        return usr;
    }
}
