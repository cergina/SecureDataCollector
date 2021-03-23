package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - general response
 */
public class JsonResponse extends PrettyObject {

    @Expose
    private int status;
    @Expose
    private String message;
    @Expose
    private String data;

    public JsonResponse() {} // empty constructor for Gson

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
