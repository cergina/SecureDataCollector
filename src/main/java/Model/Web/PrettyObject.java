package Model.Web;

import com.google.gson.GsonBuilder;

/**
 * Api model - serializable Gson object
 */
public abstract class PrettyObject {

    /*
    Java object to JSON string
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(this);
    }

    /*
    Java object to pretty formatted JSON string
     */
    public String toStringPretty() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

    /*
    JSON string to Java object
     */
    public static PrettyObject parse(String str, Class<?> cls){
        return (PrettyObject) new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(str, cls);
    }
}
