package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;


public class Sensor extends PrettyObject {
    // PARAMETERS
    @Expose
    private int id;
    @Expose
    private String input;
    @Expose
    private String name;
    @Expose
    private Integer sensorTypeID;
    @Expose
    private Integer controllerUnitID;
    @Expose
    private List<Measurement> measurements;
    @Expose
    private List<Integer> dataArray;


    // empty constructor for Gson
    public Sensor() {
    }

    public Sensor(int id, String input, String name, Integer sensorTypeID, Integer controllerUnitID, List<Measurement> measurements, List<Integer> dataArray) {
        this.id = id;
        this.input = input;
        this.name = name;
        this.sensorTypeID = sensorTypeID;
        this.controllerUnitID = controllerUnitID;
        this.measurements = measurements;
        this.dataArray = dataArray;
    }

    // GETTERS and SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public Integer getSensorTypeID() {
        return sensorTypeID;
    }

    public void setSensorTypeID(Integer sensorTypeID) {
        this.sensorTypeID = sensorTypeID;
    }

    public Integer getControllerUnitID() {
        return controllerUnitID;
    }

    public void setControllerUnitID(Integer controllerUnitID) {
        this.controllerUnitID = controllerUnitID;
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
