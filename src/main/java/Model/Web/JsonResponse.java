package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - universal response
 */
public class JsonResponse extends PrettyObject {

    @Expose
    private Integer status;
    @Expose
    private String message;
    @Expose
    private Object data; // custom response data will go here

    public JsonResponse() {} // empty constructor for Gson

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
