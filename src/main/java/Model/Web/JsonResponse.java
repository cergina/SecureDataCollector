package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - universal response
 */
public class JsonResponse extends PrettyObject {

    // PARAMETERS
    @Expose
    private Integer status;
    @Expose
    private String message;
    @Expose
    private Object data; // custom response data will go here

    // empty constructor for Gson
    public JsonResponse() {}

    // GETTERS
    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
    // SETTERS
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
