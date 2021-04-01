package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.GeneralAccessibility;
import Model.Database.Interaction.*;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.*;
import Model.Web.Project;
import Model.Web.Projects;
import Model.Web.thymeleaf.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

/**
 * Use Case class for FlatSummaryView
 */
public class UC_FlatSummary {
    private DbProvider db;

    public UC_FlatSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final Projects allProjectsForUser(int userID) { // TODO move to different UC
        Projects projects = new Projects();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            List<T_Project_user> arr = I_ProjectUser.retrieveAllForUser(db.getConn(), db.getPs(), db.getRs(), userID);
            List<Project> temp = new ArrayList<Project>();

            for (T_Project_user  tpu: arr) {
                Project project = new Project();

                T_Project tp = I_Project.retrieve(db.getConn(), db.getPs(), db.getRs(), tpu.getA_ProjectID());

                project.setId(tp.getA_pk());
                project.setName(tp.getA_Name());
                project.setCreatedat(tp.getA_CreatedAt());
                project.setDeletedat(tp.getA_DeletedAt());

                temp.add(project);
            }

            // dont forget to set data that was inserted into json
            projects.setProjects(temp);

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return projects;
    }

    /***
     * @param flatId from request
     * @return {@link Flat}, null if it does not exist
     */
    public Flat getFlatSummary(@NotNull Integer flatId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_Flat t_flat = getFlatById(flatId);
        if (t_flat == null) return null;

        List<T_ControllerUnit> t_controllerUnits = getAllControllersForFlat(flatId);
        List<ControllerUnit> controllerUnits = new ArrayList<>();
        for (T_ControllerUnit t_controllerUnit : t_controllerUnits) {
            List<T_Sensor> t_sensors = getAllSensorsForController(t_controllerUnit.getA_pk());
            List<Sensor> sensors = new ArrayList<>();
            for (T_Sensor t_sensor : t_sensors) {
                int measuredLast30Days = getMeasuredLast30Days(t_sensor.getA_pk());
                int mesuredTotal = getMeasuredTotal(t_sensor.getA_pk());

                Sensor sensor = new Sensor(
                        t_sensor.getA_Input(),
                        t_sensor.getA_Name(),
                        measuredLast30Days,
                        mesuredTotal
                );
                sensors.add(sensor);
            }
            ControllerUnit controllerUnit = new ControllerUnit(
                    t_controllerUnit.getA_Uid(),
                    t_controllerUnit.getA_DipAddress(),
                    t_controllerUnit.getA_Zwave(),
                    sensors
            );
            controllerUnits.add(controllerUnit);
        }

        CentralUnit centralUnit = null;
        if (!controllerUnits.isEmpty()) {
            T_CentralUnit t_centralUnit = getCentralUnitById(t_controllerUnits.get(0).getA_CentralUnitID());
            centralUnit = new CentralUnit(
                    t_centralUnit.getA_Uid(),
                    t_centralUnit.getA_FriendlyName(),
                    t_centralUnit.getA_SimNO(),
                    t_centralUnit.getA_Imei(),
                    t_centralUnit.getA_Zwave()
            );
        }

        T_Address t_address = getAddressById(t_flat.getA_AddressID());
        Address address = new Address(
                t_address.getA_Country(),
                t_address.getA_City(),
                t_address.getA_Street(),
                t_address.getA_HouseNO(),
                t_address.getA_ZIP()
        );

        Flat flat = new Flat(
                t_flat.getA_ApartmentNO(),
                address,
                centralUnit,
                controllerUnits
        );

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return flat;
    }

    /***
     * Verifies rights for user's right to view flat (that belongs to certain project)
      * @param userId from session
     * @param flatId flat that is attempting to see
     * @return
     */
    public boolean doesUserHaveRightToSeeProjectBelongingToFlat(@NotNull Integer userId, @NotNull Integer flatId) {
        boolean hasRight = false;

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            hasRight = (null != GeneralAccessibility.doesUserHaveRightToAccessFlat(db.getConn(), db.getPs(), db.getRs(), userId, flatId));

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return hasRight;
    }

    /// PRIVATES

    private @NotNull List<T_ControllerUnit> getAllControllersForFlat(@NotNull Integer flatId) {
        List<T_ControllerUnit> arr = new ArrayList<>();

        try {
            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), flatId, DB_DO_NOT_USE_THIS_FILTER);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    private @NotNull List<T_Sensor> getAllSensorsForController(@NotNull Integer controllerId) {
        List<T_Sensor> arr = new ArrayList<>();

        try {
            arr = I_Sensor.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, controllerId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    private int getMeasuredLast30Days(@NotNull Integer sensorId) {
        int value = 0;

        try {
            value = I_Measurements.measuredLast30DaysForSensor(db.getConn(), db.getPs(), db.getRs(), sensorId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return value;
    }

    private int getMeasuredTotal(@NotNull Integer sensorId) {
        int value = 0;

        try {
            T_Measurement t_measurement = I_Measurements.retrieveNewest(db.getConn(), db.getPs(), db.getRs(), sensorId);
            value = t_measurement.getA_AccumulatedValue();
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return value;
    }

    private T_Address getAddressById(@NotNull Integer id) {
        T_Address t = null;

        try {
            t = I_Address.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_CentralUnit getCentralUnitById(@NotNull Integer id) {
        T_CentralUnit t = null;

        try {
            t = I_CentralUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_Flat getFlatById(@NotNull Integer id) {
        T_Flat t = null;

        try {
            t = I_Flat.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
