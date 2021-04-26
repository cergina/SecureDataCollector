package Model.Web;

import java.util.List;

/**
 * Api model - flat
 */
public class Flat {

    public Integer id;
    public String apartmentNO;
    public Address address; // TODO toto este doladim lebo teraz sa zmenila logika a adresa patri budove
    public List<ControllerUnit> controllerUnits;

    // temp
    public Integer buildingId;

    public Flat(Integer id, String apartmentNO) {
        this.id = id;
        this.apartmentNO = apartmentNO;
    }

    public Flat(Integer id, String apartmentNO, Address address, List<ControllerUnit> controllerUnits) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.address = address;
        this.controllerUnits = controllerUnits;
    }

    public Flat(Integer id, String apartmentNO, Integer buildingId) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.buildingId = buildingId;
    }
}
