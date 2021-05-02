package Model.Web;

import Model.Database.Tables.T_CentralUnit;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Api model - central unit
 */
public class CentralUnit extends PrettyObject {

    // PARAMETERS
    @Expose
    public Integer id;
    @Expose
    public Integer uid;
    @Expose
    public String dipAddress;
    @Expose
    public String friendlyName;
    @Expose
    public String simNo;
    @Expose
    public String imei;
    @Expose
    public String zwave;
    @Expose
    private Integer buildingId;
    @Expose
    private String description;

    @Expose
    public List<Flat> flats;

    // empty constructor for Gson
    public CentralUnit() {}

    public CentralUnit(T_CentralUnit t, String description) {
        this.id = t.getA_pk();
        this.uid = t.getA_Uid();
        this.dipAddress = t.getA_DipAddress();
        this.friendlyName = t.getA_FriendlyName();
        this.simNo = t.getA_SimNO();
        this.imei = t.getA_Imei();
        this.zwave = t.getA_Zwave();
        this.description = description;
    }

    public CentralUnit(Integer id, Integer uid, String dipAddress, String friendlyName, String simNo, String imei, String zwave) {
        this.id = id;
        this.uid = uid;
        this.dipAddress = dipAddress;
        this.friendlyName = friendlyName;
        this.simNo = simNo;
        this.imei = imei;
        this.zwave = zwave;
    }

    // GETTERS and SETTERS
    public Integer getId() {
        return id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getZwave() {
        return zwave;
    }

    public void setZwave(String zwave) {
        this.zwave = zwave;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlats(List<Flat> flats) {
        this.flats = flats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
