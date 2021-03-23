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
import Model.Web.JsonResponse;
import View.Support.CustomExceptions.AuthenticationException;
import View.Support.CustomExceptions.InvalidOperationException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class AuthController {
    private DbProvider db;

    public AuthController(DbProvider dbProvider) {
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

    /**
     * Retrieve user and password hash from database
     */
    private Auth retrieveAuthByEmail(final String email) throws SQLException {

        T_User t_user = Model.Database.Interaction.User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), email);
        if (t_user == null) { // isnt email in db?
            return null;
        }

        Model.Web.User user = new Model.Web.User();
        user.setBeforetitle(t_user.getA_BeforeTitle());
        user.setFirstname(t_user.getA_FirstName());
        user.setMiddlename(t_user.getA_MiddleName());
        user.setLastname(t_user.getA_LastName());
        user.setPhone(t_user.getA_Phone());
        user.setEmail(t_user.getA_Email());
        user.setResidence(t_user.getA_PermanentResidence());
        user.setUserID(t_user.getA_pk());

        T_Hash t_hash = Hash.retrieveLatest(db.getConn(), db.getPs(), db.getRs(), t_user.getA_pk());

        Auth auth = new Auth();
        auth.setUser(user);
        auth.setPassword(t_hash.getA_Value());

        return auth;
    }

    /**
     * Create new user
     */
    public final JsonResponse createUser(final Auth auth) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution();

            if (retrieveAuthByEmail(auth.getUser().getEmail()) != null) { // is email already in db?
                jsonResponse.setMessage("User with this email already exists.");
                throw new AuthenticationException("User with this email already exists.");
            }

            // modify User table START
            Dictionary dict_user = new Hashtable();
            dict_user.put(T_User.DBNAME_BEFORETITLE, auth.getUser().getBeforetitle());
            dict_user.put(T_User.DBNAME_FIRSTNAME, auth.getUser().getFirstname());
            dict_user.put(T_User.DBNAME_MIDDLENAME, auth.getUser().getMiddlename());
            dict_user.put(T_User.DBNAME_LASTNAME, auth.getUser().getLastname());
            dict_user.put(T_User.DBNAME_PHONE, auth.getUser().getPhone());
            dict_user.put(T_User.DBNAME_EMAIL, auth.getUser().getEmail());
            dict_user.put(T_User.DBNAME_PERMANENTRESIDENCE, auth.getUser().getResidence());
            dict_user.put(T_User.DBNAME_BLOCKED, false);

            T_User t_user = T_User.CreateFromScratch(dict_user);
            if (!t_user.IsTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database schema.");
                throw new InvalidOperationException("Data does not match database schema.");
            }

            Model.Database.Interaction.User.insert(db.getConn(), db.getPs(), t_user); // TODO differentiate between 2 User classes
            // modify User table END

            int userID = Model.Database.Interaction.User.retrieveLatestID(db.getConn(), db.getPs(), db.getRs());

            // modify Hash table START
            Dictionary dict_hash = new Hashtable();
            dict_hash.put(T_Hash.DBNAME_VALUE, auth.getVerificationcode());
            dict_hash.put(T_Hash.DBNAME_USER_ID, userID);

            T_Hash hashToInsert = T_Hash.CreateFromScratch(dict_hash);
            Hash.insert(db.getConn(), db.getPs(), hashToInsert);
            // modify Hash table END

            // modify AccessPrivilegeJournal table START
            java.util.Date date = new java.util.Date();
            java.sql.Date dateCreated = new java.sql.Date(date.getTime());
            int privilege = auth.isIsadmin() ?
                    T_AccessPrivilegeJournal.ACCESS_PRIVILEGE_ID_ADMIN : T_AccessPrivilegeJournal.ACCESS_PRIVILEGE_ID_USER;
            int adminID = 1; // TODO ziskat realne ID admina ktory pridava pouzivatela zo sessionu

            Dictionary dict_journal = new Hashtable();
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_CREATED_AT, dateCreated); // TODO generate date within SQL
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_USER_ID, userID);
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID, privilege);
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID, adminID);

            T_AccessPrivilegeJournal journalEntryToSave = T_AccessPrivilegeJournal.CreateFromScratch(dict_journal);
            AccessPrivilegeJournal.insert(db.getConn(), db.getPs(), journalEntryToSave);
            // modify AccessPrivilegeJournal table END

            db.afterSqlExecution(true);

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("User created.");
            jsonResponse.setData(auth.toString());
        } catch (InvalidOperationException e) {
            db.afterSqlExecution(false);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AuthenticationException e) {
            db.afterSqlExecution(false);

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {
            db.afterSqlExecution(false);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }

    /**
     * Finish user registration
     * Replace verification code with password hash
     */
    public final JsonResponse finishRegistration(final Auth auth) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            Auth authDb = retrieveAuthByEmail(auth.getUser().getEmail());
            if (authDb == null) {
                jsonResponse.setMessage("User with this email does not exist.");
                throw new AuthenticationException("User with this email does not exist.");
            }
            if (!authDb.getPassword().equals(auth.getVerificationcode())) {
                jsonResponse.setMessage("Verification code does not match.");
                throw new AuthenticationException("Verification code does not match.");
            }

            // modify Hash table END
            Dictionary dict_hash = new Hashtable();
            dict_hash.put(T_Hash.DBNAME_VALUE, auth.getPassword());
            dict_hash.put(T_Hash.DBNAME_USER_ID, auth.getUser().getUserID());

            T_Hash t_hash = T_Hash.CreateFromScratch(dict_hash);
            Hash.insert(db.getConn(), db.getPs(), t_hash);
            // modify Hash table END

            jsonResponse.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.setMessage("Registration complete.");
            jsonResponse.setData(auth.toString());
        } catch (AuthenticationException e) {

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }

    public final JsonResponse authenticateUser(final Auth auth, HttpSession session) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            Auth authDb = retrieveAuthByEmail(auth.getUser().getEmail());
            if (authDb == null) {
                jsonResponse.setMessage("User with this email does not exist.");
                throw new AuthenticationException("User with this email does not exist.");
            }
            if (!authDb.getPassword().equals(auth.getPassword())) {
                jsonResponse.setMessage("Password does not match.");
                throw new AuthenticationException("Password does not match.");
            }

            session.setAttribute("email", authDb.getUser().getEmail()); // TODO session attrs to be decided after layouts
            session.setAttribute("user", authDb.getUser());

            jsonResponse.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.setMessage("Login successful.");
            jsonResponse.setData(auth.toString());
        } catch (AuthenticationException e) {

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }
}
