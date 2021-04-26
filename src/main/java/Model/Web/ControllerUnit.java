package Model.Web;

import Model.Web.Sensor;

import java.util.List;

/**
 * Api model - controller unit
 */
public class ControllerUnit {

    public Integer id;
    public Integer UID;
    public String dipAddress;
    public String zwave;
    public List<Sensor> sensors;
    public Integer flatId;
    public Integer centralUnitId;

    public ControllerUnit(Integer id, Integer UID, String dipAddress, String zwave, List<Sensor> sensors, Integer flatId, Integer centralUnitId) {
        this.id = id;
        this.UID = UID;
        this.dipAddress = dipAddress;
        this.zwave = zwave;
        this.sensors = sensors;
        this.flatId = flatId;
        this.centralUnitId = centralUnitId;
    }
}
