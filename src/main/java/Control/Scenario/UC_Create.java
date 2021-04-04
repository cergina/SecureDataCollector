package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.*;
import Model.Database.Tables.Table.*;
import Model.Web.CommType;
import Model.Web.JsonResponse;
import View.Support.CustomExceptions.InvalidOperationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Use Case class for creating database entries by admin
 */
public class UC_Create {
    private DbProvider db;

    public UC_Create(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse createCommType(final @NotNull CommType commType) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(false);

            // modify Commtype table START
            Dictionary dict_commtype = new Hashtable();
            dict_commtype.put(T_CommType.DBNAME_NAME, commType.getName());

            T_CommType t_commType = T_CommType.CreateFromScratch(dict_commtype);
            if (!t_commType.IsTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_CommType.insert(db.getConn(), db.getPs(), t_commType);
            // modify Commtype table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Communication type created.");
            jsonResponse.setData(commType);
        } catch (InvalidOperationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }
}
