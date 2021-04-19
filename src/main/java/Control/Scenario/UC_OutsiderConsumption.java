package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_ControllerUnit;
import Model.Web.thymeleaf.ControllerUnit;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;

/**
 * Use Case class for ConsumptionViewServlet
 */
public class UC_OutsiderConsumption {
    private DbProvider db;

    public UC_OutsiderConsumption(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public ControllerUnit get_ControllerUnit_ByUid(@NotNull final Integer controllerUnitUid) {
        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.beforeSqlExecution(false);

        T_ControllerUnit t_controllerUnit = get_TControllerUnit_ByUid(controllerUnitUid);
        if (t_controllerUnit == null) {
            return null;
        }

        ControllerUnit controllerUnit = Shared_Uc.buildControllerUnit(t_controllerUnit, db);

        // ATTEMPT to eliminate WEBSERVLET only falling asleep of connections
        db.afterOkSqlExecution();

        return controllerUnit;
    }


    private T_ControllerUnit get_TControllerUnit_ByUid(@NotNull final Integer uid) {
        T_ControllerUnit t = null;

        try {
            t = I_ControllerUnit.retrieveByUid(db.getConn(), db.getPs(), db.getRs(), uid);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
