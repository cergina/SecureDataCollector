package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.*;
import Model.Database.Tables.Enum.E_CommType;
import Model.Web.CommType;
import Model.Web.JsonResponse;
import View.Support.CustomExceptions.AlreadyExistsException;
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
            db.beforeSqlExecution(true);

            if (I_CommType.retrieveByName(db.getConn(), db.getPs(), db.getRs(), commType.getName()) != null) {
                jsonResponse.setMessage("Communication type with this name already exists.");
                throw new AlreadyExistsException("Communication type with this name already exists.");
            }

            // modify Commtype table START
            Dictionary dict_commtype = new Hashtable();
            dict_commtype.put(E_CommType.DBNAME_NAME, commType.getName());

            E_CommType e_commType = E_CommType.CreateFromScratch(dict_commtype);
            if (!e_commType.IsEnumTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_CommType.insert(db.getConn(), db.getPs(), e_commType);
            // modify Commtype table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Communication type created.");
            jsonResponse.setData(commType);
        } catch (InvalidOperationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }
}
