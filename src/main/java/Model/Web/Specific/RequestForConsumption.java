package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class RequestForConsumption extends PrettyObject { // TODO ma byt pouzity normalne controller model, kde sa pouzije iba jeden atribut, nie zbytocne vytvarat dalsi modelovy objekt, model neznamena robit triedu per use case
    // PARAMETERS
    @Expose
    private Integer uid;

    // GETTERS AND SETTERS
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
