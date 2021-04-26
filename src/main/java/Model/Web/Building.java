package Model.Web;

import java.util.List;

/**
 * Api model - building
 */
public class Building extends PrettyObject { // TODO tento model Expose pre pridavanie budov, nevytvarat novy

    public Integer id;
    public Address address;
    public List<Flat> flats;
    public List<CentralUnit> centralUnits;
    private Integer projectId;

    // empty constructor for Gson
    public Building() {
    }

    public Building(Integer id, Address address) {
        this.id = id;
        this.address = address;
    }

    public Building(Integer id, Address address, List<Flat> flats, List<CentralUnit> centralUnits, Integer projectId) {
        this.id = id;
        this.address = address;
        this.flats = flats;
        this.centralUnits = centralUnits;
        this.projectId = projectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlats(List<Flat> flats) {
        this.flats = flats;
    }

    public List<CentralUnit> getCentralUnits() {
        return centralUnits;
    }

    public void setCentralUnits(List<CentralUnit> centralUnits) {
        this.centralUnits = centralUnits;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
