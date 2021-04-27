package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Interaction.I_Flat;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Tables.*;
import Model.Web.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UC_ListBuilding {
    private final DbProvider db;

    public UC_ListBuilding(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final Building specificBuilding(@NotNull final Integer buildingID) {
        Building building = null;

        db.beforeSqlExecution(false);

        try {
            T_Building tb = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Building.class), buildingID);
            if (tb != null) {
                T_Address ta = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Address.class), tb.getA_AddressID());
                Address address = new Address(ta.getA_Country(), ta.getA_City(), ta.getA_Street(), ta.getA_HouseNO(), ta.getA_ZIP());

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
}
