package Model.Web.Specific;

import Model.Web.Flat;
import Model.Web.FlatOwner;
import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class Flat_FlatOwners_Creation extends PrettyObject {
    // Flatowner flat
    @Expose
    FlatOwner owner1;
    @Expose
    FlatOwner owner2;

    // Info about flat
    @Expose
    Flat flat;


    // GETTERS & SETTERS
    public FlatOwner getOwner1() {
        return owner1;
    }

    public void setOwner1(FlatOwner owner1) {
        this.owner1 = owner1;
    }

    public FlatOwner getOwner2() {
        return owner2;
    }

    public void setOwner2(FlatOwner owner2) {
        this.owner2 = owner2;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }
}
