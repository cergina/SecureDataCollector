package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Project;
import Model.Database.Interaction.I_ProjectUser;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Project;
import Model.Database.Tables.T_Project_user;
import Model.Web.Project;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_ProjectListing {
    private final DbProvider db;

    public UC_ProjectListing(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public @NotNull final List<Project> allProjectsForUser(@NotNull final Integer userID) {
        List<Project> projects = new ArrayList<>();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            for (T_Project_user tpu : I_ProjectUser.retrieveAllForUser(db.getConn(), db.getPs(), db.getRs(), userID)) {

                T_Project tp = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Project.class), tpu.getA_ProjectID());
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

    private Project FillEntityFromTable(T_Project t) {
        return new Project(t.getA_pk(), t.getA_Name(), t.getA_CreatedAt(), t.getA_DeletedAt());
    }
}
