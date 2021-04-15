package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_User;
import Model.Database.Tables.Table.T_User;
import Model.Web.User;
import Model.Web.Users;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_UserListing {
    private DbProvider db;

    public UC_UserListing(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final Users allUsers() {
        Users users = new Users();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            List<T_User> arr = I_User.retrieveAll(db.getConn(), db.getPs(), db.getRs());
            List<User> temp = new ArrayList<User>();

            for (T_User  t: arr) {
                User usr = new User();

                FillEntityFromTable(usr, t);

                temp.add(usr);
            }

            // dont forget to set data that was inserted into json
            users.setUsers(temp);

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return users;
    }

    public final User specificUser(int id) {
        User usr = new User();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            T_User t = I_User.retrieve(db.getConn(), db.getPs(), db.getRs(), id);

            FillEntityFromTable(usr, t);

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return usr;
    }


    private void FillEntityFromTable(User usr, T_User t) {
        usr.setEmail(t.getA_Email());
        usr.setBeforetitle(t.getA_BeforeTitle());
        usr.setFirstname(t.getA_FirstName());
        usr.setMiddlename(t.getA_MiddleName());
        usr.setLastname(t.getA_LastName());
        usr.setPhone(t.getA_Phone());
        usr.setResidence(t.getA_PermanentResidence());
        usr.setUserID(t.getA_pk());
    }
}
