package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.GeneralAccessibility;
import Model.Database.Interaction.*;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.*;
import Model.Web.Address;
import Model.Web.CentralUnit;
import Model.Web.Project;
import Model.Web.thymeleaf.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

/**
 * Use Case class for BuildingSummaryView
 */
public class UC_BuildingSummary {
    private DbProvider db;

    public UC_BuildingSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    /***
     * @param buildingId from request
     * @return {@link Building}, null if it does not exist
     */
    public Building getBuildingSummary(@NotNull final Integer buildingId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_Building t_building = null;
        try {
            t_building = I_Building.retrieve(db.getConn(), db.getPs(), db.getRs(), buildingId);
            if (t_building == null) {
                db.afterOkSqlExecution();
                return null;
            }
            // prepare info about address
            T_Address t_address = get_TAddress_ByBuildingId(buildingId);
            Address address = new Address(
                    t_address.getA_Country(),
                    t_address.getA_City(),
                    t_address.getA_Street(),
                    t_address.getA_HouseNO(),
                    t_address.getA_ZIP()
            );

            // put all together
            Building building = new Building(t_building.getA_pk(), address);

            // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
            db.afterOkSqlExecution();
            return building;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /***
     * Verifies rights for user's right to view building (that belongs to certain project)
     * @param userId from session
     * @param buildingId building that the user is attempting to see
     * @return
     */
    public boolean doesUserHaveRightToSeeBuilding(@NotNull Integer userId, @NotNull Integer buildingId) {
        boolean hasRight = true;

        //TODO Dokodit overenie ci moze user vidiet budovu

        return hasRight;
    }

    private T_Address get_TAddress_ByBuildingId(@NotNull final Integer buildingId) {
        T_Address t = null;

        try {
            T_Building b = I_Building.retrieve(db.getConn(), db.getPs(), db.getRs(), buildingId);
            t = I_Address.retrieve(db.getConn(), db.getPs(), db.getRs(), b.getA_AddressID());
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}