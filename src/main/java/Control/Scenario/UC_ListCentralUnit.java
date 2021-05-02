package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.Others;
import Model.Database.Tables.T_Address;
import Model.Database.Tables.T_Building;
import Model.Web.CentralUnit;
import Model.Web.JsonResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UC_ListCentralUnit {
    private final DbProvider db;

    public UC_ListCentralUnit(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse assignCentralUnitToBuilding(CentralUnit centralUnit) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            // we have id of centralUnit and buildingId we wish it to be in


            db.afterOkSqlExecution();

        } catch (Exception e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Central Unit already exists.");
        }

        return jsonResponse;
    }

    /***
     * same country, city, street.
     * house no and zip can be different.
     * @param buildingID
     */
    public final List<CentralUnit> centralUnitsOnTheSameAddressNameAsBuilding(@NotNull final Integer buildingID) {
        // central units will have the classic stuff and description that resembles address of building its in
        List<CentralUnit> centralsAtSameAddress = new ArrayList<>();

        try {
            db.beforeSqlExecution(true);

            // find out projectId, addressId and address fields for current building id
            Map<String, Object> map = Others.get_ProjectId_AddressID_AddressColumns_ForCurrentBuildingId(db.getConn(), db.getPs(), db.getRs(), buildingID);

            if (!map.isEmpty()) {
                String Country = (String)map.get(T_Address.DBNAME_COUNTRY);
                String City = (String)map.get(T_Address.DBNAME_CITY);
                String Street = (String)map.get(T_Address.DBNAME_STREET);
                String HouseNo = (String)map.get(T_Address.DBNAME_HOUSENO);
                Integer projectId = (Integer)map.get(T_Building.DBNAME_PROJECT_ID);
                Integer addressId = (Integer)map.get(Others.OTHER_SELF_ADDRESS_ID);

                // get list of central units by filtering and using project Id
                centralsAtSameAddress = Others.getAll_CentralUnitsThatShareAddressButNotHouseNo(db.getConn(), db.getPs(), db.getRs(), projectId, addressId, Country, City, Street, HouseNo);
            }

            db.afterOkSqlExecution();
        } catch (SQLException sqle) {
            db.afterExceptionInSqlExecution(sqle);
        }

        return centralsAtSameAddress;

    }
}
