package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

import java.util.List;

public class ProjectCreation extends PrettyObject {

    // PARAMETERS
    @Expose
    private String project_name;

    @Expose
    private String required_email;

    @Expose
    private List<String> additional_emails;

    // GETTERS and SETTERS
    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getRequired_email() {
        return required_email;
    }

    public void setRequired_email(String required_email) {
        this.required_email = required_email;
    }

    public List<String> getAdditional_emails() {
        return additional_emails;
    }

    public void setAdditional_emails(List<String> additional_emails) {
        this.additional_emails = additional_emails;
    }
}
