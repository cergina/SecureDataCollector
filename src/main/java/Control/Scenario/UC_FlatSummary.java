package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.GeneralAccessibility;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.I_Flat;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.*;
import Model.Web.Address;
import Model.Web.CentralUnit;
import Model.Web.ControllerUnit;
import Model.Web.Flat;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

/**
 * Use Case class for FlatSummaryView
 */
public class UC_FlatSummary {
    private final DbProvider db;

    public UC_FlatSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public int countNumberOfControllersForCentralUnit(@NotNull final Integer centralUnitId) {
        db.beforeSqlExecution(false);

        List<T_ControllerUnit> t_controllerUnits = getAll_TControllers_ForCentralUnit(centralUnitId);

        db.afterOkSqlExecution();

        return t_controllerUnits.size();
    }

    /***
     * @param flatId from request
     * @return {@link Flat}, null if it does not exist
     */
    public Flat getFlatSummary(@NotNull final Integer flatId) {
        db.beforeSqlExecution(false);

        T_Flat t_flat = get_TFlat_ById(flatId);
        if (t_flat == null) {
            db.afterOkSqlExecution();
            return null;
        }

        // get controllers from database, but allow returning of flat with no controllers
        List<T_ControllerUnit> t_controllerUnits = getAll_TControllers_ForFlat(flatId);
        List<ControllerUnit> controllerUnits = new ArrayList<>();

        if (t_controllerUnits.isEmpty() == false) {
            for (T_ControllerUnit t_controllerUnit : t_controllerUnits) {
                controllerUnits.add(Shared_Uc.buildControllerUnit(t_controllerUnit, db));
            }
        }

        // prepare info about address
        T_Address t_address = get_TAddress_ByBuildingId(t_flat.getA_BuildingID());
        Address address = new Address(
                t_address.getA_pk(),
                t_address.getA_Country(),
                t_address.getA_City(),
                t_address.getA_Street(),
                t_address.getA_HouseNO(),
                t_address.getA_ZIP()
        );

        // put all together
        Flat flat = new Flat(
                t_flat.getA_pk(),
                t_flat.getA_ApartmentNO(),
                address,
                controllerUnits
        );

        db.afterOkSqlExecution();

        return flat;
    }

    public ControllerUnit get_ControllerUnit(@NotNull final Integer controllerUnitId) {
        db.beforeSqlExecution(false);

        T_ControllerUnit t_controllerUnit = get_TControllerUnit_ById(controllerUnitId);
        if (t_controllerUnit == null) {
            return null;
        }

        ControllerUnit controllerUnit = Shared_Uc.buildControllerUnit(t_controllerUnit, db);

        db.afterOkSqlExecution();

        return controllerUnit;
    }

    public CentralUnit get_CentralUnit(@NotNull final Integer centralUnitId) {
        db.beforeSqlExecution(false);

        T_CentralUnit t = get_TCentralUnit_ById(centralUnitId);
        if (t == null) {
            db.afterFailedSqlExecution();
            return null;
        }

        // pair
        CentralUnit centralUnit = new CentralUnit();

        centralUnit.setId(t.getA_pk());
        centralUnit.setUid(t.getA_Uid());
        centralUnit.setDip(t.getA_DipAddress());
        centralUnit.setFriendlyName(t.getA_FriendlyName());
        centralUnit.setSimNo(t.getA_SimNO());
        centralUnit.setImei(t.getA_Imei());
        centralUnit.setZwave(t.getA_Zwave());
        centralUnit.setBuildingId(t.getA_BuildingID());

        db.afterOkSqlExecution();

        return centralUnit;
    }

    public CentralUnit get_CentralUnitWithFlats(@NotNull final Integer centralUnitId) {
        db.beforeSqlExecution(false);

        T_CentralUnit t_centralUnit = get_TCentralUnit_ById(centralUnitId);
        if (t_centralUnit == null) {
            return null;
        }

        // pair
        CentralUnit centralUnit = new CentralUnit();
        centralUnit.setId(t_centralUnit.getA_pk());
        centralUnit.setUid(t_centralUnit.getA_Uid());
        centralUnit.setDip(t_centralUnit.getA_DipAddress());
        centralUnit.setFriendlyName(t_centralUnit.getA_FriendlyName());
        centralUnit.setSimNo(t_centralUnit.getA_SimNO());
        centralUnit.setImei(t_centralUnit.getA_Imei());
        centralUnit.setZwave(t_centralUnit.getA_Zwave());
        centralUnit.setBuildingId(t_centralUnit.getA_BuildingID());


        // fill with flats of setting address Id
        List<T_Flat> arr;
        try {
            arr = I_Flat.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), centralUnit.getBuildingId(), null);
        } catch (SQLException e) {
            db.afterFailedSqlExecution();
            return null;
        }

        // convert T_Flats into Flats
        List<Flat> list = new ArrayList<>();
        for (T_Flat t: arr
             ) {
            Flat temp = new Flat(t.getA_pk(), t.getA_ApartmentNO(), t.getA_BuildingID());

            list.add(temp);
        }

        centralUnit.setFlats(list);

        db.afterOkSqlExecution();

        return centralUnit;
    }

    /***
     * Verifies rights for user's right to view flat (that belongs to certain project)
      * @param userId from session
     * @param flatId flat that is attempting to see
     * @return
     */
    public boolean doesUserHaveRightToSeeProjectBelongingToFlat(@NotNull Integer userId, @NotNull Integer flatId) {
        boolean hasRight = false;

        db.beforeSqlExecution(false);

        try {
            hasRight = (null != GeneralAccessibility.doesUserHaveRightToAccessFlat(db.getConn(), db.getPs(), db.getRs(), userId, flatId));

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return hasRight;
    }

    /// PRIVATES
    private @NotNull List<T_ControllerUnit> getAll_TControllers_ForFlat(@NotNull final Integer flatId) {
        List<T_ControllerUnit> arr = new ArrayList<>();

        try {
            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), DB_DO_NOT_USE_THIS_FILTER, flatId, null);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }

    private @NotNull List<T_ControllerUnit> getAll_TControllers_ForCentralUnit(@NotNull final Integer centralUnitId) {
        List<T_ControllerUnit> arr = new ArrayList<>();

        try {
            arr = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), centralUnitId, DB_DO_NOT_USE_THIS_FILTER, null);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return arr;
    }


    private T_Address get_TAddress_ByBuildingId(@NotNull final Integer buildingId) {
        T_Address t = null;

        try {
            T_Building b = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Building.class), buildingId);
            t = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Address.class), b.getA_AddressID());
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_CentralUnit get_TCentralUnit_ById(@NotNull final Integer id) {
        T_CentralUnit t = null;

        try {
            t = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_CentralUnit.class), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_ControllerUnit get_TControllerUnit_ById(@NotNull final Integer id) {
        T_ControllerUnit t = null;

        try {
            t = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_ControllerUnit.class), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }



    private T_Flat get_TFlat_ById(@NotNull final Integer id) {
        T_Flat t = null;

        try {
            t = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Flat.class), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
