package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.*;
import Model.Database.Tables.*;
import Model.Web.Address;
import Model.Web.Building;
import Model.Web.Project;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_ProjectListing {
    private DbProvider db;

    public UC_ProjectListing(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public @NotNull final List<Project> allProjectsForUser(@NotNull final Integer userID) {
        List<Project> projects = new ArrayList<>();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            for (T_Project_user tpu : I_ProjectUser.retrieveAllForUser(db.getConn(), db.getPs(), db.getRs(), userID)) {

                T_Project tp = I_Project.retrieve(db.getConn(), db.getPs(), db.getRs(), tpu.getA_ProjectID());
                Project project = FillEntityFromTable(tp);
                projects.add(project);
            }

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return projects;
    }


    public @NotNull final List<Project> allProjects() {
        List<Project> projects = new ArrayList<>();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            for (T_Project tp : I_Project.retrieveAll(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Project.class))) {
                Project project = FillEntityFromTable(tp);
                projects.add(project);
            }

            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return projects;
    }

    public final boolean isUserInProject(@NotNull final Integer projectID, @NotNull final Integer userID) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);
        try {
            return null != I_ProjectUser.retrieveByProjectIdAndUserId(db.getConn(), db.getPs(), db.getRs(), projectID, userID);
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }
        return false;
    }

    public final Project specificProject(@NotNull final Integer projectID) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            T_Project tp = I_Project.retrieve(db.getConn(), db.getPs(), db.getRs(), projectID);
            Project project = FillEntityFromTable(tp);
            List<Building> buldings = new ArrayList<>();
            for (T_Building tb : I_Building.retrieveByProjectId(db.getConn(), db.getPs(), db.getRs(), projectID)) {
                T_Address ta = I_Address.retrieve(db.getConn(), db.getPs(), db.getRs(), tb.getA_AddressID());
                Address address = new Address(ta.getA_Country(), ta.getA_City(), ta.getA_Street(), ta.getA_HouseNO(), ta.getA_ZIP());
                Building building = new Building(tb.getA_pk(), address);
                buldings.add(building);
            }
            project.setBuildings(buldings);

            db.afterOkSqlExecution();

            return project;
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }
        return null;
    }

    private Project FillEntityFromTable(T_Project t) {
        return new Project(t.getA_pk(), t.getA_Name(), t.getA_CreatedAt(), t.getA_DeletedAt());
    }
}
