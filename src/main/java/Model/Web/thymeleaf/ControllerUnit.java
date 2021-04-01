package Model.Web.thymeleaf;

import java.util.List;

/**
 * Thymeleaf model
 */
public class ControllerUnit {

    public Integer UID;
    public String dipAddress;
    public String zwave;
    public List<Sensor> sensors;

    public ControllerUnit(Integer UID, String dipAddress, String zwave, List<Sensor> sensors) {
        this.UID = UID;
        this.dipAddress = dipAddress;
        this.zwave = zwave;
        this.sensors = sensors;
    }
}
