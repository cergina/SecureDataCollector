package Model.Web;

public class Building extends PrettyObject { // TODO tento model Expose pre pridavanie budov, nevytvarat novy

    public Integer id;
    public Address address;

    // empty constructor for Gson
    public Building() {
    }

    public Building(Integer id, Address address) {
        this.id = id;
        this.address = address;
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
}
