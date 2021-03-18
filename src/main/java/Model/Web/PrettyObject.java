package Model.Web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class PrettyObject {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String toStringPretty() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
