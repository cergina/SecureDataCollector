package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Building;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Interaction.I_Flat;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.Assurance;
import Model.Database.Tables.*;
import Model.Web.*;
import Model.Web.Specific.BuildingCreation;
import View.Support.CustomExceptions.AlreadyExistsException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class UC_BuildingListing {
    private final DbProvider db;

    public UC_BuildingListing(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final Building specificBuilding(@NotNull final Integer buildingID) {
        Building building = null;

        db.beforeSqlExecution(false);

        try {
            T_Building tb = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Building.class), buildingID);
            if (tb != null) {
                T_Address ta = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Address.class), tb.getA_AddressID());
                Address address = new Address(ta.getA_pk(), ta.getA_Country(), ta.getA_City(), ta.getA_Street(), ta.getA_HouseNO(), ta.getA_ZIP());

                List<Flat> flats = new ArrayList<>();
                for (T_Flat tf : I_Flat.retrieveByBuildingId(db.getConn(), db.getPs(), db.getRs(), buildingID)) {
                    Flat flat = new Flat(tf.getA_pk(), tf.getA_ApartmentNO());
                    flats.add(flat);
                }

                List<CentralUnit> centralUnits = new ArrayList<>();
                for (T_CentralUnit tc : I_CentralUnit.retrieveByBuildingId(db.getConn(), db.getPs(), db.getRs(), buildingID)) {
                    CentralUnit centralUnit = new CentralUnit(tc.getA_pk(), tc.getA_Uid(), tc.getA_DipAddress(),
                            tc.getA_FriendlyName(), tc.getA_SimNO(), tc.getA_Imei(), tc.getA_Zwave());
                    centralUnits.add(centralUnit);
                }

                building = new Building(tb.getA_pk(), address, flats, centralUnits, tb.getA_ProjectID());
            }

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }
        return building;
    }

    public JsonResponse createNewBuilding(BuildingCreation buildingCreation) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            Assurance.idCheck(buildingCreation.getAddressId());
            Assurance.idCheck(buildingCreation.getProjectId());

            // check if exists
            if (checkExistsInDatabase(buildingCreation))
                throw new AlreadyExistsException("Building with this address already exists.");

            // Creation and Insertion
            insertIntoDatabase(buildingCreation);

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Building at that address created.");
            jsonResponse.setData(buildingCreation);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Building with this address already exists.");
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Sorry, something happened internally.");
        }

        return jsonResponse;
    }

    private boolean checkExistsInDatabase(BuildingCreation buildingCreation) throws SQLException {
        return I_Building.checkIfExists(db.getConn(), db.getPs(), db.getRs(), buildingCreation.getAddressId());
    }

    private void insertIntoDatabase(@NotNull final BuildingCreation buildingCreation) throws SQLException{
        Dictionary dict = new Hashtable();

        dict.put(T_Building.DBNAME_ADDRESS_ID, buildingCreation.getAddressId());
        dict.put(T_Building.DBNAME_PROJECT_ID, buildingCreation.getProjectId());

        I_Building.insert(db.getConn(), db.getPs(), T_Building.CreateFromScratch(dict));
    }
}
