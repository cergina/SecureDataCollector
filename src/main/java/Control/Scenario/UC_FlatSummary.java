package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.GeneralAccessibility;
import Model.Database.Interaction.*;
import Model.Database.Tables.Table.*;
import Model.Web.Project;
import Model.Web.Projects;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

/**
 * Use Case class for FlatSummaryView
 */
public class UC_FlatSummary {
    private DbProvider db;

    public UC_FlatSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final Projects allProjectsForUser(int userID) {
        Projects projects = new Projects();

        try {
            db.beforeSqlExecution();

            ArrayList<T_Project_user> arr = I_ProjectUser.retrieveAllForUser(db.getConn(), db.getPs(), db.getRs(), userID);
            ArrayList<Project> temp = new ArrayList<Project>();

            for (T_Project_user  tpu: arr) {
                Project project = new Project();

                T_Project tp = I_Project.retrieve(db.getConn(), db.getPs(), db.getRs(), tpu.getA_ProjectID());

                project.setId(tp.getA_pk());
                project.setName(tp.getA_Name());
                project.setCreatedat(tp.getA_CreatedAt());
                project.setDeletedat(tp.getA_DeletedAt());

                temp.add(project);
            }

            db.afterOkSqlExecution();

            // dont forget to set data that was inserted into json
            projects.setProjects(temp);

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return projects;
    }

    /***
     * Returns null if person has no right to see measurements for that flat or it does not exist
     * @param userId from sesssion
     * @param flatId from request
     * @return
     */
    public ArrayList<T_Measurement> getAllMeasurementsForFlat(int userId, int flatId) {
        if (doesUserHaveRightToSeeProjectBelongingToFlat(userId, flatId) == false) {
            return null;
        }

        //
        // has right to see flat, return empty at the very least
        //
        ArrayList<T_Measurement> measurementsArr = new ArrayList<T_Measurement>();

        // Retrieve all sensor that belong to the controllers that belong to the flat
        ArrayList<T_ControllerUnit> controllerArr = getAllControllersForFlat(flatId);
        ArrayList<T_Sensor> sensorsArr = new ArrayList<T_Sensor>();

        for (T_ControllerUnit tcu: controllerArr
             ) {
            sensorsArr.addAll(getAllSensorsForController(tcu.getA_pk()));
        }

        // add all measurements to resulting array
        for(T_Sensor ts: sensorsArr) {
            measurementsArr.addAll(getAllMeasurementsForSensor(ts.getA_pk()));
        }

        return measurementsArr;
    }

    /***
     * Temporary //TODO remove
     * @param userId
     * @param flatId
     * @return
     */
    public boolean temporaryCheckToSeeIfHasRightToSee(int userId, int flatId) {
        return doesUserHaveRightToSeeProjectBelongingToFlat(userId, flatId);
    }

    /// PRIVATES

    /***
     * Important function, do not change
     * Verifies rights for user's right to view flat (that belongs to certain project)
      * @param userId from session
     * @param flatId flat that is attempting to see
     * @return
     */
    private boolean doesUserHaveRightToSeeProjectBelongingToFlat(int userId, int flatId) {
        boolean toReturn = false;

        try {
            db.beforeSqlExecution();

            if (null != GeneralAccessibility.doesUserHaveRightToAccessFlat(db.getConn(), db.getPs(), db.getRs(), userId, flatId)) {
                toReturn = true;
            }

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return toReturn;
    }

    private ArrayList<T_ControllerUnit> getAllControllersForFlat(int flatId) {
        ArrayList<T_ControllerUnit> arr = new ArrayList<T_ControllerUnit>();

        try {
            db.beforeSqlExecution();

            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), flatId, DB_DO_NOT_USE_THIS_FILTER);

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return arr;
    }

    private ArrayList<T_Sensor> getAllSensorsForController(int controllerId) {
        ArrayList<T_Sensor> arr = new ArrayList<T_Sensor>();

        try {
            db.beforeSqlExecution();

            arr = I_Sensor.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, controllerId);

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return arr;
    }

    private ArrayList<T_Measurement> getAllMeasurementsForSensor(int sensorId) {
        ArrayList<T_Measurement> arr = new ArrayList<T_Measurement>();

        try {
            db.beforeSqlExecution();

            arr = I_Measurements.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER);

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return arr;
    }
}
