package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_AccessPrivilegeJournal;
import Model.Database.Interaction.I_AccessPrivillege;
import Model.Database.Interaction.I_User;
import Model.Database.Tables.*;
import Model.Web.Specific.UserCreation;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class UC_UserCreations {
    private final DbProvider db;

    public UC_UserCreations(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public List<UserCreation> getUserCreations() {
        List<UserCreation> creations = new ArrayList<>();

        try {
            db.beforeSqlExecution(false);

            // get all user access privilege journal entries
            List<T_AccessPrivilegeJournal> list = I_AccessPrivilegeJournal.retrieveAll(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_AccessPrivilegeJournal.class));

            //get user's name, user's privileges, admin's name and creation date for each
            for (T_AccessPrivilegeJournal accessPrivilegeJournalEntry: list) {
                String userName = null;
                String adminName = null;

                T_User user = I_User.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class), accessPrivilegeJournalEntry.getA_UserID());
                if(user.getA_MiddleName() != null) {
                    userName = user.getA_FirstName() + " " + user.getA_MiddleName() + " " + user.getA_LastName();
                }
                else {
                    userName = user.getA_FirstName() + " " + user.getA_LastName();
                }

                E_AccessPrivilege accessPrivilege = I_AccessPrivillege.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(E_AccessPrivilege.class), accessPrivilegeJournalEntry.getA_AccessPrivilegeID());
                String userPrivileges = accessPrivilege.getA_Name();

                T_User admin = I_User.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_User.class), accessPrivilegeJournalEntry.getA_CreatedByUserID());
                if(user.getA_MiddleName() != null) {
                    adminName = admin.getA_FirstName() + " " + admin.getA_MiddleName() + " " + admin.getA_LastName();
                }
                else {
                    adminName = admin.getA_FirstName() + " " + admin.getA_LastName();
                }

                creations.add(new UserCreation(userName, userPrivileges, adminName, accessPrivilegeJournalEntry.getA_CreatedAt()));
            }
            db.afterOkSqlExecution();
        } catch (Exception e) {
            db.afterExceptionInSqlExecution(e);
        }
        return creations;
    }
}
