package Model.Web.thymeleaf;

import Model.Web.Address;

import java.util.List;

/**
 * Thymeleaf model
 */
public class Building {

    public Integer id;
    public Address address;


    public Building(Integer id, Address address) {
        this.id = id;
        this.address = address;
    }
}
