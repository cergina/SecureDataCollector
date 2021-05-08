package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.*;
import Model.Web.Sensor;
import Model.Web.ControllerUnit;

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
            E_SensorType sensorType = null;
            try {
                sensorType = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(E_SensorType.class), t_sensor.getA_SensorTypeID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Integer unitAmount = getUnitAmountOfSensor(sensorType);
            int realMeasuredLast30Days = getRealValueFromTicks(measuredLast30Days, unitAmount);
            int measuredTotal = getMeasuredTotal(t_sensor.getA_pk(), db);
            int realMeasuredTotal = getRealValueFromTicks(measuredTotal, unitAmount);
            Sensor sensor = new Sensor(
                    t_sensor.getA_Input(),
                    t_sensor.getA_Name(),
                    realMeasuredLast30Days,
                    realMeasuredTotal
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

    //TODO duplikatny kod z UC_Graph, treba prerobit tak ze sa odstrani metoda z UC_Graph, az na to ze tu treba static metodu kvoli metode buildControllerUnit
    public static Integer getUnitAmountOfSensor(E_SensorType sensorType) {
        String measuredIn = sensorType.getA_MeasuredIn();
        String[] splitMeasuredIn = measuredIn.split("_");
        return Integer.parseInt(splitMeasuredIn[0]);
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

    //this method calculates the real measured amount of the utility from the amount that one tick represents and amount of ticks
    protected static int getRealValueFromTicks(@NotNull final Integer value, @NotNull final Integer tickMultiplier){
        return value*tickMultiplier;
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
