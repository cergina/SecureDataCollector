package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Projects extends PrettyObject{

    // PARAMETERS
    @Expose
    private ArrayList<Project> projects;

    public Projects() {}

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
