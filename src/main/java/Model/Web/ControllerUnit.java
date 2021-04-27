package Model.Web;

import Model.Web.Sensor;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Api model - controller unit
 */
public class ControllerUnit extends PrettyObject {

    public Integer id;
    @Expose
    public Integer uid;
    @Expose
    public String dipAddress;
    @Expose
    public String zwave;
    public List<Sensor> sensors;
    @Expose
    public Integer flatId;
    public Integer centralUnitId;

    // empty constructor for Gson
    public ControllerUnit() {}

    public ControllerUnit(Integer id, Integer uid, String dipAddress, String zwave, List<Sensor> sensors, Integer flatId, Integer centralUnitId) {
        this.id = id;
        this.uid = uid;
        this.dipAddress = dipAddress;
        this.zwave = zwave;
        this.sensors = sensors;
        this.flatId = flatId;
        this.centralUnitId = centralUnitId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDipAddress() {
        return dipAddress;
    }

    public void setDipAddress(String dipAddress) {
        this.dipAddress = dipAddress;
    }

    public String getZwave() {
        return zwave;
    }

    public void setZwave(String zwave) {
        this.zwave = zwave;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Integer getFlatId() {
        return flatId;
    }

    public void setFlatId(Integer flatId) {
        this.flatId = flatId;
    }

    public Integer getCentralUnitId() {
        return centralUnitId;
    }

    public void setCentralUnitId(Integer centralUnitId) {
        this.centralUnitId = centralUnitId;
    }
}
