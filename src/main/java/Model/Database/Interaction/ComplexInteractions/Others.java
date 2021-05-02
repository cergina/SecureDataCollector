package Model.Database.Interaction.ComplexInteractions;

import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Address;
import Model.Database.Tables.T_Building;
import Model.Database.Tables.T_CentralUnit;
import Model.Web.CentralUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Others {
    public static final String OTHER_SELF_ADDRESS_ID = "self_address_ID";

    public static Map<String, Object> get_ProjectId_AddressID_AddressColumns_ForCurrentBuildingId(Connection conn, PreparedStatement ps, ResultSet rs, Integer buildingId) throws SQLException {
        Assurance.idCheck(buildingId);

        // SELECT budova.ProjectID, adresa.ID as self_address_ID, adresa.Country, adresa.City, adresa.Street, adresa.HouseNo FROM dcs.address adresa INNER JOIN dcs.building budova ON budova.AddressID=adresa.ID  WHERE budova.AddressID=(SELECT budovka.AddressID FROM dcs.building budovka WHERE budovka.ID=1);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "budova.ProjectID, adresa.ID as self_address_ID, adresa.Country, adresa.City, adresa.Street, adresa.HouseNo " +
                        "FROM " + T_Address.DBTABLE_NAME + " adresa " +
                        "INNER JOIN " + T_Building.DBTABLE_NAME + " budova ON budova.AddressID=adresa.ID  " +
                        "WHERE budova.AddressID=(SELECT budovka.AddressID FROM " + T_Building.DBTABLE_NAME + " budovka WHERE budovka.ID=?)"
        );

        int col = 0;
        ps.setInt(++col, buildingId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        Map<String, Object> hashMap = new HashMap<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            hashMap.put(T_Building.DBNAME_PROJECT_ID, new Integer(rs.getInt(T_Building.DBNAME_PROJECT_ID)));
            hashMap.put(OTHER_SELF_ADDRESS_ID, new Integer(rs.getInt(OTHER_SELF_ADDRESS_ID)));
            hashMap.put(T_Address.DBNAME_COUNTRY, rs.getString(T_Address.DBNAME_COUNTRY));
            hashMap.put(T_Address.DBNAME_CITY, rs.getString(T_Address.DBNAME_CITY));
            hashMap.put(T_Address.DBNAME_STREET, rs.getString(T_Address.DBNAME_STREET));
            hashMap.put(T_Address.DBNAME_HOUSENO, rs.getString(T_Address.DBNAME_HOUSENO));
        }

        return hashMap;
    }

    public static List<CentralUnit> getAll_CentralUnitsThatShareAddressButNotHouseNo(Connection conn, PreparedStatement ps, ResultSet rs, Integer projectId, Integer addressId, String country, String city, String street, String houseNo) throws SQLException {
        Assurance.idCheck(projectId);
        Assurance.idCheck(addressId);
        Assurance.varcharCheck(country);
        Assurance.varcharCheck(city);
        Assurance.varcharCheck(street);
        Assurance.varcharCheck(houseNo);

        // SELECT centraly.*, adresy.Street, adresy.HouseNO FROM dcs.centralunit centraly INNER JOIN dcs.building budovy ON budovy.ID=centraly.BuildingID  INNER JOIN dcs.address adresy ON adresy.ID=budovy.AddressID WHERE budovy.ProjectID=1 AND adresy.Country='Slovakia' AND adresy.City='Nitra' AND adresy.Street='Vodná';

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "centraly.*, adresy.Street, adresy.HouseNO FROM " + T_CentralUnit.DBTABLE_NAME + " centraly " +
                            "INNER JOIN " + T_Building.DBTABLE_NAME + " budovy ON budovy.ID=centraly.BuildingID  " +
                            "INNER JOIN " + T_Address.DBTABLE_NAME + " adresy ON adresy.ID=budovy.AddressID " +
                        "WHERE budovy.ProjectID=? AND adresy.Country=? AND adresy.City=? AND adresy.Street=?"
        );

        int col = 0;
        ps.setInt(++col, projectId);
        ps.setString(++col, country);
        ps.setString(++col, city);
        ps.setString(++col, street);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<CentralUnit> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                T_CentralUnit t = I_CentralUnit.FillEntity(rs);

                arr.add(
                        new CentralUnit(t,rs.getString("Street") + " " + rs.getString("HouseNo"))
                );
            }
        }

        return arr;
    }
}
