package Model.Web.thymeleaf;

/**
 * Thymeleaf model
 */
public class CentralUnit {

    public Integer UID;
    public String friendlyName;
    public String simNO;
    public String imei;
    public String zwave;

    public CentralUnit(Integer UID, String friendlyName, String simNO, String imei, String zwave) {
        this.UID = UID;
        this.friendlyName = friendlyName;
        this.simNO = simNO;
        this.imei = imei;
        this.zwave = zwave;
    }
}
