package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_ControllerUnit;
import Model.Database.Tables.T_Measurement;
import Model.Database.Tables.T_Sensor;
import Model.Web.Sensor;
import Model.Web.thymeleaf.ControllerUnit;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

/*
*  Make sure it stays NON-public. Only the same package will have access to this
* */
class Shared_Uc {

    //////////////////////////////////////////
    //          Consumption-Flat            //
    //////////////////////////////////////////
    protected static @NotNull ControllerUnit buildControllerUnit(@NotNull final T_ControllerUnit t_controllerUnit, DbProvider db) {

        List<T_Sensor> t_sensors = getAll_TSensors_ForController(t_controllerUnit.getA_pk(), db);
        List<Sensor> sensors = new ArrayList<>();
        for (T_Sensor t_sensor : t_sensors) {
            int measuredLast30Days = getMeasuredLast30Days(t_sensor.getA_pk(), db);
            int mesuredTotal = getMeasuredTotal(t_sensor.getA_pk(), db);

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
                sensors,
                t_controllerUnit.getA_FlatID(),
                t_controllerUnit.getA_CentralUnitID()
        );
    }

    protected static @NotNull List<T_Sensor> getAll_TSensors_ForController(@NotNull final Integer controllerId, DbProvider db) {
        List<T_Sensor> arr = new ArrayList<>();

        try {
            arr = I_Sensor.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, controllerId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    protected static int getMeasuredLast30Days(@NotNull final Integer sensorId, DbProvider db) {
        int value = 0;

        try {
            value = I_Measurements.measuredLast30DaysForSensor(db.getConn(), db.getPs(), db.getRs(), sensorId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return value;
    }

    protected static int getMeasuredTotal(@NotNull final Integer sensorId, DbProvider db) {
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
    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////
}
