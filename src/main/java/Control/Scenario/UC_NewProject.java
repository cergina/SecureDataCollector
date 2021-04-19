package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Project;
import Model.Database.Interaction.I_ProjectUser;
import Model.Database.Interaction.I_User;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_Project;
import Model.Database.Tables.T_Project_user;
import Model.Database.Tables.T_User;
import Model.Web.JsonResponse;
import Model.Web.Specific.ProjectCreation;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.CreationException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class UC_NewProject {
    private DbProvider db;

    public UC_NewProject(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse createNewProject(@NotNull final ProjectCreation projectCreation) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            // do not create project that already exists
            if (get_TProject_ByName(projectCreation.getProject_name()) != null) {
                jsonResponse.setMessage("Project with this  name already exists.");
                throw new AlreadyExistsException("Project with this  name already exists.");
            }

            // remove elements that are "" or null
            removeEmptyElements(projectCreation);

            // check whether all emails provided are already user's
            if (isProjectNameAndEmailsValid(projectCreation) == false) {
                jsonResponse.setMessage("Some fields are incorrect or some required are missing.");
                throw new CreationException("Some fields are incorrect or some required are missing.");
            }

            // Get all user's that need addition to Project_User table -- check if they exist
            List<T_User> listOfUsers = getAll_TUsers_ThatWillBeOwners(projectCreation);
            if (listOfUsers == null) {
                jsonResponse.setMessage("All e-mails have to be already registered in the project in advance.");
                throw new CreationException("All e-mails have to be already registered in the project in advance.");
            }

            // Create a new project and project_user connections
            insertProjectAndProjectUsersIntoDatabase(projectCreation, listOfUsers);

            // Success
            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Project created.");
        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");

        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Project with this name already exists.");
        }

        return jsonResponse;
    }

    // Privates
    private void insertProjectAndProjectUsersIntoDatabase(@NotNull final ProjectCreation projectCreation, @NotNull final List<T_User> listOfUsers) throws SQLException{
        // Insert new Project
        I_Project.insert(db.getConn(), db.getPs(), T_Project.CreateFromScratch(projectCreation.getProject_name()));

        int latestId = InteractionWithDatabase.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());

        // Insert new Connections between project and user
        for (T_User user:
             listOfUsers) {
            Dictionary dict = new Hashtable();

            dict.put(T_Project_user.DBNAME_PROJECTID, latestId);
            dict.put(T_Project_user.DBNAME_USERID, user.getA_pk());

            I_ProjectUser.insert(db.getConn(), db.getPs(), T_Project_user.CreateFromScratch(dict));
        }
    }

    private List<T_User> getAll_TUsers_ThatWillBeOwners(@NotNull final ProjectCreation projectCreation) throws SQLException{
        List<T_User> list = new ArrayList<>();

        // process required email
        T_User requiredUser = I_User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), projectCreation.getRequired_email());

        if (requiredUser == null)
            return null;

        list.add(requiredUser);

        // process emails of additional users
        for (String email:
             projectCreation.getAdditional_emails()) {
            T_User tempUser = I_User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), email);

            // never allow null (all emails inserted have to be present)
            if (tempUser == null)
                return null;

            list.add(tempUser);
        }

        return list;
    }


    /***
     * Project name has to be valid, emails have to be valid: basic requirements
     * @param projectCreation
     * @return
     */
    private boolean isProjectNameAndEmailsValid(@NotNull final ProjectCreation projectCreation) {

        // Valid project name
        if (projectCreation == null || projectCreation.getProject_name().equals(""))
            return false;

        // All emails are valid
        String required = projectCreation.getRequired_email();

        boolean requiredPresent = EmailValidator.getInstance().isValid(required);

        for (String em:
                projectCreation.getAdditional_emails()) {
            if (EmailValidator.getInstance().isValid(em) == false)
                return false;
        }

        return requiredPresent;
    }

    private void removeEmptyElements(@NotNull final ProjectCreation projectCreation) {
        projectCreation.getAdditional_emails().removeIf(em -> (em == null || em.equals("")));
    }

    private T_Project get_TProject_ByName(@NotNull final String name) {
        T_Project t = null;

        try {
            t = I_Project.retrieveByName(db.getConn(), db.getPs(), db.getRs(), name);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
