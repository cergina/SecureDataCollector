package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.AccessPrivilegeJournal;
import Model.Database.Interaction.Address;
import Model.Database.Interaction.Hash;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_AccessPrivilegeJournal;
import Model.Database.Tables.Table.T_Address;
import Model.Database.Tables.Table.T_Hash;
import Model.Database.Tables.Table.T_User;
import Model.Web.Auth;
import View.Support.CustomExceptions.EmailNotRegisteredException;
import View.Support.CustomExceptions.InvalidOperationException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/*
* THIS CLASS SHOULD NOT BE USED IN PRODUCTION
* */
public class ExampleUseCase {
    private DbProvider db;

    public ExampleUseCase(DbProvider dbProvider) {
        this.db = dbProvider;
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

        try {
            db.beforeSqlExecution();

            Dictionary dict = new Hashtable();
            dict.put(T_User.DBNAME_BEFORETITLE, "Bc."); // TODO
            dict.put(T_User.DBNAME_FIRSTNAME, auth.getUser().getFirstname());
            dict.put(T_User.DBNAME_MIDDLENAME, auth.getUser().getMiddlename());
            dict.put(T_User.DBNAME_LASTNAME, auth.getUser().getLastname());
            dict.put(T_User.DBNAME_PHONE, auth.getUser().getPhone());
            dict.put(T_User.DBNAME_EMAIL, auth.getUser().getEmail());
            dict.put(T_User.DBNAME_PERMANENTRESIDENCE, auth.getUser().getResidence());
            dict.put(T_User.DBNAME_BLOCKED, false);

            T_User userToRegister = T_User.CreateFromScratch(dict);
            boolean canEnterUser = userToRegister.IsTableOkForDatabaseEnter();
            if(!canEnterUser){
                throw new InvalidOperationException("The entered data is not acceptable.");
            }

            Model.Database.Interaction.User.insert(db.getConn(), db.getPs(), userToRegister);
            int userID = Model.Database.Interaction.User.retrieveLatestID(db.getConn(), db.getPs(), db.getRs());

            Dictionary verificationDict = new Hashtable();
            verificationDict.put(T_Hash.DBNAME_VALUE, auth.getVerificationcode());
            verificationDict.put(T_Hash.DBNAME_USER_ID, userID);

            T_Hash hashToInsert = T_Hash.CreateFromScratch(verificationDict);
            Hash.insert(db.getConn(), db.getPs(), hashToInsert);

            int adminID = 1; // TODO ziskat realne ID admina ktory pridava pouzivatela zo sessionu
            java.util.Date date = new java.util.Date();
            java.sql.Date dateCreated = new java.sql.Date(date.getTime());
            int privilege = auth.isIsadmin() ? 1 : 2; // TODO move to constants

            Dictionary journalDict = new Hashtable(); //TODO Spravit funkciu na vytvaranie Dictionaries pre tabulky, opakovany kod
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_AT, dateCreated); // TODO generate date within SQL
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_USER_ID, userID);
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID, privilege);
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID, adminID);
            T_AccessPrivilegeJournal journalEntryToSave = T_AccessPrivilegeJournal.CreateFromScratch(journalDict);
            AccessPrivilegeJournal.insert(db.getConn(), db.getPs(), journalEntryToSave);

            db.afterSuccessfulSqlExecution();
            return true;
        } catch (SQLException throwables) {
            db.transactionRollback();
        } catch (InvalidOperationException e) {
            db.transactionRollback();
        } finally {
            db.restartAutoCommit();
        }
        return false;
    }

    public Pair<Auth,Integer> retrieveAuthByEmail(final String email) {

        try {
            db.beforeSqlExecution();

            //Finding user by the entered email in the database
            T_User userToRegister = Model.Database.Interaction.User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), email);
            if (userToRegister == null) { //If there isn't a user with the entered email in the database
                throw new EmailNotRegisteredException("The email entered by the user is not registered.");
            }

            Model.Web.User user = new Model.Web.User();
            // TODO before title
            user.setFirstname(userToRegister.getA_FirstName());
            user.setMiddlename(userToRegister.getA_MiddleName());
            user.setLastname(userToRegister.getA_LastName());
            user.setPhone(userToRegister.getA_Phone());
            user.setEmail(userToRegister.getA_Email());
            user.setResidence(userToRegister.getA_PermanentResidence());

            T_Hash hash = Hash.retrieveLatest(db.getConn(), db.getPs(), db.getRs(), userToRegister.getA_pk());

            Auth auth = new Auth();
            auth.setUser(user);
            auth.setPassword(hash.getA_Value());

            db.afterSuccessfulSqlExecution();
            return new Pair<>(auth, userToRegister.getA_pk());
        } catch (SQLException throwables) {
            db.transactionRollback();
        } catch (EmailNotRegisteredException e) {
            db.transactionRollback();
        } finally {
            db.restartAutoCommit();
        }
        return null;
    }

    public boolean updateAuthPassword(final String passwordHash, final int userID) {
        try {
            //Inserting the hashed password in the database
            Dictionary dict = new Hashtable();
            dict.put(T_Hash.DBNAME_VALUE, passwordHash);
            dict.put(T_Hash.DBNAME_USER_ID, userID);
            T_Hash hashToInsert = T_Hash.CreateFromScratch(dict);
            Hash.insert(db.getConn(), db.getPs(), hashToInsert);
            return true;
        } catch (SQLException throwables) {
            CustomLogs.Error(throwables.getMessage());
        }
        return false;
    }
}
