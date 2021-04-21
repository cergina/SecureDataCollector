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
 * Use Case class for FlatSummaryView
 */
public class UC_FlatSummary {
    private DbProvider db;

    public UC_FlatSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final List<Project> allProjectsForUser(int userID) { // TODO move to different UC
        List<Project> temp = new ArrayList<>();

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        try {
            List<T_Project_user> arr = I_ProjectUser.retrieveAllForUser(db.getConn(), db.getPs(), db.getRs(), userID);


            for (T_Project_user  tpu: arr) {
                Project project = new Project();

                T_Project tp = I_Project.retrieve(db.getConn(), db.getPs(), db.getRs(), tpu.getA_ProjectID());

                project.setId(tp.getA_pk());
                project.setName(tp.getA_Name());
                project.setCreatedat(tp.getA_CreatedAt());
                project.setDeletedat(tp.getA_DeletedAt());

                temp.add(project);
            }


            db.afterOkSqlExecution();

        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return temp;
    }

    public int countNumberOfControllersForCentralUnit(@NotNull final Integer centralUnitId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        List<T_ControllerUnit> t_controllerUnits = getAll_TControllers_ForCentralUnit(centralUnitId);

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return t_controllerUnits.size();
    }

    /***
     * @param flatId from request
     * @return {@link Flat}, null if it does not exist
     */
    public Flat getFlatSummary(@NotNull final Integer flatId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
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

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return flat;
    }

    public ControllerUnit get_ControllerUnit(@NotNull final Integer controllerUnitId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_ControllerUnit t_controllerUnit = get_TControllerUnit_ById(controllerUnitId);
        if (t_controllerUnit == null) {
            return null;
        }

        ControllerUnit controllerUnit = Shared_Uc.buildControllerUnit(t_controllerUnit, db);

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return controllerUnit;
    }

    public CentralUnit get_CentralUnit(@NotNull final Integer centralUnitId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
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


        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return centralUnit;
    }

    public CentralUnit get_CentralUnitWithFlats(@NotNull final Integer centralUnitId) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
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
            arr = I_Flat.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), centralUnit.getBuildingId());
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


        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
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

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
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
            T_Building b = I_Building.retrieve(db.getConn(), db.getPs(), db.getRs(), buildingId);
            t = I_Address.retrieve(db.getConn(), db.getPs(), db.getRs(), b.getA_AddressID());
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_CentralUnit get_TCentralUnit_ById(@NotNull final Integer id) {
        T_CentralUnit t = null;

        try {
            t = I_CentralUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }

    private T_ControllerUnit get_TControllerUnit_ById(@NotNull final Integer id) {
        T_ControllerUnit t = null;

        try {
            t = I_ControllerUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }



    private T_Flat get_TFlat_ById(@NotNull final Integer id) {
        T_Flat t = null;

        try {
            t = I_Flat.retrieve(db.getConn(), db.getPs(), db.getRs(), id);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
