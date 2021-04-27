package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_ProjectUser;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Project_user;
import Model.Database.Tables.T_User;
import Model.Web.User;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_ListUser {
    private final DbProvider db;

    public UC_ListUser(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public @NotNull final List<User> allUsersForProject(@NotNull final Integer projectID) {
        List<User> users = new ArrayList<>();

        db.beforeSqlExecution(false);

        try {
            for (T_Project_user tpu : I_ProjectUser.retrieveAllForProject(db.getConn(), db.getPs(), db.getRs(), projectID)) {

                T_User tu = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class), tpu.getA_UserID());
                User user = FillEntityFromTable(tu);
                users.add(user);
            }

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return users;
    }

    public final List<User> allUsers() {
        db.beforeSqlExecution(false);

        List<User> temp = new ArrayList<User>();

        try {
            List<T_User> arr = InteractionWithDatabase.retrieveAll(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class));


            for (T_User  t: arr) {
                User usr = FillEntityFromTable(t);
                temp.add(usr);
            }

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return temp;
    }

    public final User specificUser(int id) {
        User usr = null;

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
