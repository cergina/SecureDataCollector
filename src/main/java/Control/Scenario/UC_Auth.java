package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.*;
import Model.Database.Tables.Table.*;
import Model.Web.Auth;
import Model.Web.JsonResponse;
import Model.Web.User;
import View.Support.CustomExceptions.AuthenticationException;
import View.Support.CustomExceptions.InvalidOperationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Use Case class for authentication
 */
public class UC_Auth {
    private DbProvider db;

    public UC_Auth(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public List<T_Address> retrieveAllAddress() { // TODO remove this method later
        List<T_Address> arr = new ArrayList<T_Address>();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            arr = I_Address.retrieveAll(db.getConn(), db.getPs(), db.getRs());

            db.afterOkSqlExecution();
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
        }

        return arr;
    }

    // PUBLIC METHODS

    /**
     * Create new user
     * NEEDS TO BE A TRANSACTION - doing inserts
     * @param auth User data
     * @return final Api response body
     */
    public final @NotNull JsonResponse createUser(@NotNull final Auth auth, @NotNull final Integer adminID) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            if (retrieveAuthByEmail(auth.getUser().getEmail()) != null) {
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

            T_User t_user = T_User.CreateFromScratch(dict_user);
            if (!t_user.IsTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_User.insert(db.getConn(), db.getPs(), t_user);
            // modify User table END



            int createdUserID = I_User.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());

            // modify Hash table START
            Dictionary dict_hash = new Hashtable();
            dict_hash.put(T_Hash.DBNAME_VALUE, auth.getVerificationcode()); // save verification instead of password hash
            dict_hash.put(T_Hash.DBNAME_USER_ID, createdUserID);

            T_Hash hashToInsert = T_Hash.CreateFromScratch(dict_hash);
            I_Hash.insert(db.getConn(), db.getPs(), hashToInsert);
            // modify Hash table END

            // modify AccessPrivilegeJournal table START
            java.util.Date date = new java.util.Date();
            java.sql.Date dateCreated = new java.sql.Date(date.getTime());
            int privilege = auth.getIsadmin() ?
                    T_AccessPrivilegeJournal.ACCESS_PRIVILEGE_ID_ADMIN : T_AccessPrivilegeJournal.ACCESS_PRIVILEGE_ID_USER;

            Dictionary dict_journal = new Hashtable();
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_CREATED_AT, dateCreated); // TODO generate date within SQL
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_USER_ID, createdUserID);
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID, privilege);
            dict_journal.put(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID, adminID);

            T_AccessPrivilegeJournal journalEntryToSave = T_AccessPrivilegeJournal.CreateFromScratch(dict_journal);
            I_AccessPrivilegeJournal.insert(db.getConn(), db.getPs(), journalEntryToSave);
            // modify AccessPrivilegeJournal table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("User created.");
            jsonResponse.setData(auth);
        } catch (InvalidOperationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AuthenticationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }

    /**
     * Finish user registration
     * NEEDS TO BE A TRANSACTION - doing inserts
     * @param auth User data that contains email, password hash, verification code
     * @return final Api response body
     */
    public final @NotNull JsonResponse finishRegistration(@NotNull final Auth auth) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            Auth authDb = retrieveAuthByEmail(auth.getUser().getEmail());
            if (authDb == null) {
                jsonResponse.setMessage("User with this email does not exist.");
                throw new AuthenticationException("User with this email does not exist.");
            }
            int hashCount = I_Hash.countHashesForUser(db.getConn(), db.getPs(), db.getRs(), authDb.getUser().getUserID());
            if (hashCount > 1) {
                jsonResponse.setMessage("User already registered.");
                throw new AuthenticationException("User already registered.");
            }
            if (!authDb.getPassword().equals(auth.getVerificationcode())) {
                jsonResponse.setMessage("Verification code does not match.");
                throw new AuthenticationException("Verification code does not match.");
            }

            // modify Hash table END
            Dictionary dict_hash = new Hashtable();
            dict_hash.put(T_Hash.DBNAME_VALUE, auth.getPassword()); // replace verification code with password hash
            dict_hash.put(T_Hash.DBNAME_USER_ID, authDb.getUser().getUserID());

            T_Hash t_hash = T_Hash.CreateFromScratch(dict_hash);
            I_Hash.insert(db.getConn(), db.getPs(), t_hash);
            // modify Hash table END

            // After SQL SQL execution
            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.setMessage("Registration complete.");
            jsonResponse.setData(authDb);
        } catch (AuthenticationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }

    /**
     * Login user
     * DOES NOT NEED TO BE A TRANSACTION - only retrieving stuff
     * @param auth User data that contains email, password hash
     * @return final Api response body
     */
    public final @NotNull JsonResponse authenticateUser(@NotNull final Auth auth) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
            db.beforeSqlExecution(false);

            Auth authDb = retrieveAuthByEmail(auth.getUser().getEmail());

            if (authDb == null) {
                jsonResponse.setMessage("User with this email does not exist.");
                throw new AuthenticationException("User with this email does not exist.");
            }

            if (!authDb.getPassword().equals(auth.getPassword())) {
                jsonResponse.setMessage("Password does not match.");
                throw new AuthenticationException("Password does not match.");
            }

            // retrieve user privilege
            T_AccessPrivilegeJournal t_accessPrivilegeJournal = I_AccessPrivilegeJournal.retrieveValidForUser(db.getConn(), db.getPs(), db.getRs(), authDb.getUser().getUserID());
            authDb.setIsadmin(t_accessPrivilegeJournal.getA_AccessPrivilegeID() == T_AccessPrivilegeJournal.ACCESS_PRIVILEGE_ID_ADMIN);


            // After SQL execution - ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.setMessage("Login successful.");
            jsonResponse.setData(authDb);

        } catch (AuthenticationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }

    /***
     * Method that is responsible for putting log of ip address into database and map it onto userid from session
     * NEEDS TO BE A TRANSACTION - doing inserts
     * @param userId
     * @param ipAddress
     */
    public void LogLoginIntoTheDatabase(int userId, String ipAddress) {
        try {
            db.beforeSqlExecution(true);

            Dictionary tmpDict = new Hashtable();

            java.util.Date date = new java.util.Date();
            tmpDict.put(T_LoginLog.DBNAME_LOGGEDAT, new java.sql.Date(date.getTime()));
            tmpDict.put(T_LoginLog.DBNAME_SRCIP, ipAddress);
            tmpDict.put(T_LoginLog.DBNAME_USERID, userId);

            I_LoginLog.insert(db.getConn(), db.getPs(), T_LoginLog.CreateFromScratch(tmpDict));

            db.afterOkSqlExecution();
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
        }
    }


    // PRIVATE METHODS

    /**
     * Retrieve user and password hash
     * @param email User to search for
     * @return {@link Auth} instance, null if email not in database
     */
    private @NotNull Auth retrieveAuthByEmail(@NotNull final String email) throws SQLException {

        T_User t_user = I_User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), email);
        if (t_user == null) { // isnt email in db?
            return null;
        }

        User user = new User();
        user.setBeforetitle(t_user.getA_BeforeTitle());
        user.setFirstname(t_user.getA_FirstName());
        user.setMiddlename(t_user.getA_MiddleName());
        user.setLastname(t_user.getA_LastName());
        user.setPhone(t_user.getA_Phone());
        user.setEmail(t_user.getA_Email());
        user.setResidence(t_user.getA_PermanentResidence());
        user.setUserID(t_user.getA_pk());

        T_Hash t_hash = I_Hash.retrieveLatest(db.getConn(), db.getPs(), db.getRs(), t_user.getA_pk());

        Auth auth = new Auth();
        auth.setUser(user);
        auth.setPassword(t_hash.getA_Value());

        return auth;
    }
}
