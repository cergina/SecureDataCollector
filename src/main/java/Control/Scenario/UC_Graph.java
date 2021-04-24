package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.*;
import Model.Web.JsonResponse;
import Model.Web.Measurement;
import Model.Web.Sensor;
import Model.Web.Specific.GraphSingleFlat;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class UC_Graph {
    private final DbProvider db;

    public UC_Graph(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse dataForGraph(@NotNull final GraphSingleFlat graph) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
        jsonResponse.setMessage("Graph data sent");
        jsonResponse.setData(graph);
        return jsonResponse;
    }

    public List<String> getDatesAsLabelsOfLast30Days() {
        List<String> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate before = today.minusDays(29);
        while (!before.isAfter(today)) {
            dates.add(before.getDayOfMonth() + "." + before.getMonthValue() + ".");
            before = before.plusDays(1);
        }

        for (int i = 0; i < dates.size(); i++) {
            System.out.println(dates.get(i));
        }
        return dates;
    }

    public List<Date> getDatesOfLast30Days() {
        List<Date> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate before = today.minusDays(29);

        while (!before.isAfter(today)) {
            dates.add(java.sql.Date.valueOf(before));
            before = before.plusDays(1);
        }

        for (int i = 0; i < dates.size(); i++) {
            System.out.println(dates.get(i));
        }
        return dates;
    }


    private @NotNull List<T_ControllerUnit> getAllControllersForFlat(@NotNull Integer flatId) {
        List<T_ControllerUnit> arr = new ArrayList<>();

        try {
            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, flatId, null);
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

    public List<Sensor> getSensorsForFlat(Integer flatID) {
        List<Sensor> sensors = new ArrayList<>();

        List<T_ControllerUnit> t_controllerUnits = getAllControllersForFlat(flatID);

        for (T_ControllerUnit t_controllerUnit : t_controllerUnits) {
            List<T_Sensor> t_sensors = getAllSensorsForController(t_controllerUnit.getA_pk());
            for (T_Sensor t_sensor : t_sensors) {
                try {
                    List<T_Measurement> t_measurements = I_Measurements.getLast30DaysMeasurements(db.getConn(), db.getPs(), db.getRs(), t_sensor.getA_pk());
                    List<Measurement> measurements = new ArrayList<>();
                    for (T_Measurement t_measurement : t_measurements) {
                        Measurement measurement = new Measurement(t_measurement.getA_AccumulatedValue(), t_measurement.getA_SensorID(), t_measurement.getA_MeasuredAt());
                        measurements.add(measurement);
                    }

                    List<Integer> dataArray = getMeasurementArrayForSensor(t_sensor.getA_pk(), measurements);
                    E_SensorType sensorType = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(E_SensorType.class), t_sensor.getA_SensorTypeID());
                    String unitType = getUnitTypeOfSensor(sensorType);
                    Integer unitAmount = getUnitAmountOfSensor(sensorType);
                    getRealMeasurementValues(dataArray, unitAmount);
                    Sensor sensor = new Sensor(t_sensor.getA_Input(), t_sensor.getA_Name(), t_sensor.getA_ControllerUnitID(), measurements, dataArray, unitType, unitAmount);

                    sensors.add(sensor);
                } catch (SQLException sqle) {
                    CustomLogs.Error(sqle.getMessage());
                }
            }
        }
        return sensors;
    }

    public List<Integer> getMeasurementArrayForSensor(int sensorID, List<Measurement> measurements) {
        try {
            List<Integer> dataArray = new ArrayList<>();
            Integer accumulatedValue30DaysAgo = I_Measurements.getAccumulatedValueOf30DaysAgo(db.getConn(), db.getPs(), db.getRs(), sensorID);

            Map<Date, Integer> hashMap = new HashMap<>();
            for (Measurement measurement : measurements) {
                if(hashMap.containsKey(measurement.getMeasuredAt())){
                    if(hashMap.get(measurement.getMeasuredAt()) < measurement.getValue()-accumulatedValue30DaysAgo){
                        hashMap.put(measurement.getMeasuredAt(), measurement.getValue()-accumulatedValue30DaysAgo);
                    } //efektivnejsie by bolo odpocitavat akumulovanu hodnotu az pri datumoch
                }
                else {
                    hashMap.put(measurement.getMeasuredAt(), measurement.getValue()-accumulatedValue30DaysAgo);
                }
            }

            int fillerValue = 0;

            List<Date> dates = getDatesOfLast30Days();
            for(Date date : dates){
                if(hashMap.containsKey(date)){
                    fillerValue = hashMap.get(date);
                }
                else{
                    hashMap.put(date, fillerValue);
                }
                dataArray.add(fillerValue);
            }

            return dataArray;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Integer getUnitAmountOfSensor(E_SensorType sensorType) {
        String measuredIn = sensorType.getA_MeasuredIn();
        String[] splitMeasuredIn = measuredIn.split("_");
        return Integer.parseInt(splitMeasuredIn[0]);
    }

    public String getUnitTypeOfSensor(E_SensorType sensorType) {
        String measuredIn = sensorType.getA_MeasuredIn();
        String[] splitMeasuredIn = measuredIn.split("_");
        return splitMeasuredIn[1];
    }

    public void getRealMeasurementValues(List<Integer> dataArray, Integer unitAmount){
        for (int i = 0; i < dataArray.size(); i++) {
            int value = dataArray.get(i) * unitAmount;
            dataArray.set(i, value);
        }
    }
}
