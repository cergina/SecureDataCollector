package Model.Web.thymeleaf;

import Model.Database.Tables.Table.T_Flat;

import java.util.List;

/**
 * Thymeleaf model
 */
public class Flat {

    public int id;
    public String apartmentNO;
    public Address address;
    public CentralUnit centralUnit;
    public List<ControllerUnit> controllerUnits;

    // temp
    public int addressId;

    public Flat(int id, String apartmentNO, Address address, CentralUnit centralUnit, List<ControllerUnit> controllerUnits) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.address = address;
        this.centralUnit = centralUnit;
        this.controllerUnits = controllerUnits;
    }

    // TODO this is nonsense to have here but i dont understand it we have so many types already...
    public Flat(T_Flat t) {
        this.id = t.getA_pk();
        this.apartmentNO = t.getA_ApartmentNO();
        this.addressId = t.getA_AddressID();
    }
}
