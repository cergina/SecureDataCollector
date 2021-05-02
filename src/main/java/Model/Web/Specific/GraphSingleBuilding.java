package Model.Web.Specific;
import Model.Web.Flat;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

import java.util.List;

public class GraphSingleBuilding extends PrettyObject {

    // PARAMETERS
    @Expose
    private List<String> dates;

    @Expose
    private List<Flat> flats;

    //EMPTY CONSTRUCTOR
    public GraphSingleBuilding() {}

    //EMPTY CONSTRUCTOR
    public GraphSingleBuilding(List<String> dates, List<Flat> flats)
    {
        this.dates = dates;
        this.flats = flats;
    }

    // GETTERS and SETTERS
    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlats(List<Flat> flats) {
        this.flats = flats;
    }

}
