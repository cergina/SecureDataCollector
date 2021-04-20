package Model.Web.thymeleaf;

import Model.Web.Address;

import java.util.List;

/**
 * Thymeleaf model
 */
public class Flat {

    public Integer id;
    public String apartmentNO;
    public Address address;
    public List<ControllerUnit> controllerUnits;

    // temp
    public Integer buildingId;

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
