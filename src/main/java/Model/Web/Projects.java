package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Projects extends PrettyObject{

    // PARAMETERS
    @Expose
    private List<Project> projects;

    public Projects() {}

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
