package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Api model - sensor
 */
public class Sensor extends PrettyObject {

    // public can be accessed by thymeleaf
    @Expose
    public String input;
    @Expose
    public String name;
    public Integer measuredLast30Days;
    public Integer measuredTotal;
    @Expose
    private String sensorTypeName;
    @Expose
    private Integer controllerUnitId;
    @Expose
    private List<Measurement> measurements;
    @Expose
    private List<Integer> dataArray;


    // empty constructor for Gson
    public Sensor() {
    }

    public Sensor(String input, String name, Integer measuredLast30Days, Integer measuredTotal) {
        this.input = input;
        this.name = name;
        this.measuredLast30Days = measuredLast30Days;
        this.measuredTotal = measuredTotal;
    }

    public Sensor(String input, String name, Integer controllerUnitID, List<Measurement> measurements, List<Integer> dataArray) {
        this.input = input;
        this.name = name;
        this.controllerUnitId = controllerUnitID;
        this.measurements = measurements;
        this.dataArray = dataArray;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMeasuredLast30Days() {
        return measuredLast30Days;
    }

    public void setMeasuredLast30Days(Integer measuredLast30Days) {
        this.measuredLast30Days = measuredLast30Days;
    }

    public Integer getMeasuredTotal() {
        return measuredTotal;
    }

    public void setMeasuredTotal(Integer measuredTotal) {
        this.measuredTotal = measuredTotal;
    }

    public String getSensorTypeName() {
        return sensorTypeName;
    }

    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    public Integer getControllerUnitId() {
        return controllerUnitId;
    }

    public void setControllerUnitId(Integer controllerUnitId) {
        this.controllerUnitId = controllerUnitId;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Integer> getDataArray() {
        return dataArray;
    }

    public void setDataArray(List<Integer> dataArray) {
        this.dataArray = dataArray;
    }
}
