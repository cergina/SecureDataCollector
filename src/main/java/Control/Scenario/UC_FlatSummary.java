package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.GeneralAccessibility;
import Model.Database.Interaction.*;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.*;
import Model.Web.Project;
import Model.Web.Projects;
import Model.Web.Sensor;
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
    public Flat getFlatSummary(@NotNull final Integer flatId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_Flat t_flat = get_TFlat_ById(flatId);
        if (t_flat == null)
            return null;

        List<T_ControllerUnit> t_controllerUnits = getAll_Controllers_ForFlat(flatId);
        List<ControllerUnit> controllerUnits = new ArrayList<>();
        for (T_ControllerUnit t_controllerUnit : t_controllerUnits) {
            controllerUnits.add(buildControllerUnit(t_controllerUnit));
        }

        CentralUnit centralUnit = null;
        if (!controllerUnits.isEmpty()) {
            T_CentralUnit t_centralUnit = get_TCentralUnit_ById(t_controllerUnits.get(0).getA_CentralUnitID());
            centralUnit = new CentralUnit(
                    t_centralUnit.getA_Uid(),
                    t_centralUnit.getA_FriendlyName(),
                    t_centralUnit.getA_SimNO(),
                    t_centralUnit.getA_Imei(),
                    t_centralUnit.getA_Zwave()
            );
        }

        T_Address t_address = get_TAddress_ById(t_flat.getA_AddressID());
        Address address = new Address(
                t_address.getA_Country(),
                t_address.getA_City(),
                t_address.getA_Street(),
                t_address.getA_HouseNO(),
                t_address.getA_ZIP()
        );

        Flat flat = new Flat(
                t_flat.getA_pk(),
                t_flat.getA_ApartmentNO(),
                address,
                centralUnit,
                controllerUnits
        );

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return flat;
    }

    public ControllerUnit get_ControllerUnit_ByUid(@NotNull final Integer controllerUnitUid) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_ControllerUnit t_controllerUnit = get_TControllerUnit_ByUid(controllerUnitUid);
        if (t_controllerUnit == null) {
            return null;
        }

        ControllerUnit controllerUnit = buildControllerUnit(t_controllerUnit);

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return controllerUnit;
    }

    public ControllerUnit get_ControllerUnit(@NotNull final Integer controllerUnitId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_ControllerUnit t_controllerUnit = get_TControllerUnit_ById(controllerUnitId);
        if (t_controllerUnit == null) {
            return null;
        }

        ControllerUnit controllerUnit = buildControllerUnit(t_controllerUnit);

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return controllerUnit;
    }

    private @NotNull ControllerUnit buildControllerUnit(@NotNull final T_ControllerUnit t_controllerUnit) {

        List<T_Sensor> t_sensors = getAll_Sensors_ForController(t_controllerUnit.getA_pk());
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
        return new ControllerUnit(
                t_controllerUnit.getA_pk(),
                t_controllerUnit.getA_Uid(),
                t_controllerUnit.getA_DipAddress(),
                t_controllerUnit.getA_Zwave(),
                sensors
        );
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

    private @NotNull List<T_ControllerUnit> getAll_Controllers_ForFlat(@NotNull final Integer flatId) {
        List<T_ControllerUnit> arr = new ArrayList<>();

        try {
            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), flatId, DB_DO_NOT_USE_THIS_FILTER);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    private @NotNull List<T_Sensor> getAll_Sensors_ForController(@NotNull final Integer controllerId) {
        List<T_Sensor> arr = new ArrayList<>();

        try {
            arr = I_Sensor.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, controllerId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    private int getMeasuredLast30Days(@NotNull final Integer sensorId) {
        int value = 0;

        try {
            value = I_Measurements.measuredLast30DaysForSensor(db.getConn(), db.getPs(), db.getRs(), sensorId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return value;
    }

    private int getMeasuredTotal(@NotNull final Integer sensorId) {
        int value = 0;

        try {
            T_Measurement t_measurement = I_Measurements.retrieveNewest(db.getConn(), db.getPs(), db.getRs(), sensorId);

            // in case no measurement found for sensor Id
            if (t_measurement == null) {
                return value;
            }

            value = t_measurement.getA_AccumulatedValue();
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return value;
    }

    private T_Address get_TAddress_ById(@NotNull final Integer id) {
        T_Address t = null;

        try {
            t = I_Address.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_CentralUnit get_TCentralUnit_ById(@NotNull final Integer id) {
        T_CentralUnit t = null;

        try {
            t = I_CentralUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_ControllerUnit get_TControllerUnit_ById(@NotNull final Integer id) {
        T_ControllerUnit t = null;

        try {
            t = I_ControllerUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_ControllerUnit get_TControllerUnit_ByUid(@NotNull final Integer uid) {
        T_ControllerUnit t = null;

        try {
            t = I_ControllerUnit.retrieveByUid(db.getConn(), db.getPs(), db.getRs(), uid);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_Flat get_TFlat_ById(@NotNull final Integer id) {
        T_Flat t = null;

        try {
            t = I_Flat.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
