package Model.Web.Specific;

import Model.Web.PrettyObject;
import Model.Web.Sensor;
import com.google.gson.annotations.Expose;

import java.util.List;

public class GraphSingleFlat extends PrettyObject {

    // PARAMETERS
    @Expose
    private List<String> dates;

    @Expose
    private List<Sensor> sensors;

    //EMPTY CONSTRUCTOR
    public GraphSingleFlat() {}

    //EMPTY CONSTRUCTOR
    public GraphSingleFlat(List<String> dates, List<Sensor> sensors)
    {
        this.dates = dates;
        this.sensors = sensors;
    }

    // GETTERS and SETTERS
    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
