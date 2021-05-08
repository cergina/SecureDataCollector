package Model.Database.Interaction.ComplexInteractions;

import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.*;
import Model.Web.CentralUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Others {
    // just for SQL table naming for problem section
    public static final String OTHER_SELF_ADDRESS_ID = "self_address_ID";



    // FOR PROBLEMS - its a complex view BASE NAME COMPLEX_PROBLEM relevant is the part after
    public static String COMPLEX_PROBLEM_REASON = "Problem";
    public static String COMPLEX_PROBLEM_INPUT = T_Sensor.DBNAME_INPUT;
    public static String COMPLEX_PROBLEM_SENSORNAME = "SensorName";
    public static String COMPLEX_PROBLEM_FO_PHONE = "FlatOwnerPhone";
    public static String COMPLEX_PROBLEM_FO_FIRSTNAME = "FlatOwnerFirstName";
    public static String COMPLEX_PROBLEM_FO_LASTNAME = "FlatOwnerLastName";
    public static String COMPLEX_PROBLEM_ADDR_COUNTRY = T_Address.DBNAME_COUNTRY;
    public static String COMPLEX_PROBLEM_ADDR_CITY = T_Address.DBNAME_CITY;
    public static String COMPLEX_PROBLEM_ADDR_STREET = T_Address.DBNAME_STREET;
    public static String COMPLEX_PROBLEM_ADDR_NO = T_Address.DBNAME_HOUSENO;
    public static String COMPLEX_PROBLEM_APARTMENT = T_Flat.DBNAME_APARTMENTNO;
    public static String COMPLEX_PROBLEM_PROJ_NAME = "ProjectName";
    public static String COMPLEX_PROBLEM_PROJ_OWNER_MAIL = "ProjectMail";
    public static String COMPLEX_PROBLEM_PROJ_OWNER_NAME = "ProjectOwnerLastName";

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param buildingId
     * @return
     * @throws SQLException
     */
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

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param projectId
     * @param addressId
     * @param country
     * @param city
     * @param street
     * @param houseNo
     * @return
     * @throws SQLException
     */
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


    /***
     * FOR PROBLEMATIC VIEW IN ONE SQL REQUEST
     * @param conn
     * @param ps
     * @param rs
     * @param sensorId
     * @param controllerId
     * @return
     * @throws SQLException
     */
    public static Dictionary<String, String> get_ComplexInformationAboutControllerAndSensor(Connection conn, PreparedStatement ps, ResultSet rs, Integer sensorId, Integer controllerId) throws SQLException {

        Assurance.idCheck(sensorId);
        Assurance.idCheck(controllerId);

        /* SELECT ts.Input, ts.Name AS SensorName, tfo.Phone AS FlatOwnerPhone, tfo.FirstName AS FlatOwnerFirstName,tfo.LastName AS FlatOwnerLastName, ta.Country, ta.City, ta.Street, ta.HouseNO,  tf.ApartmentNO, tp.Name AS ProjectName, tu.Email AS ProjectMail, tu.LastName AS ProjectOwnerLastName FROM dcs.controllerunit tctrl  JOIN dcs.flat tf ON tf.ID=FlatID JOIN dcs.building tb ON tb.ID=tf.BuildingID JOIN dcs.project tp ON tp.ID=tb.ProjectID JOIN dcs.project_user tpu ON tpu.ProjectID=tb.ProjectID JOIN dcs.user tu ON tu.ID=tpu.UserID JOIN dcs.flatowner_flat tfof ON tfof.FlatID=tctrl.FlatID JOIN dcs.flatowner tfo ON tfo.ID=tfof.FlatOwnerID JOIN dcs.address ta ON ta.ID=tb.AddressID JOIN dcs.sensor ts ON ts.ControllerUnitID=tctrl.ID WHERE tctrl.ID=1 AND ts.ID=1 LIMIT 1; */
        ps = conn.prepareStatement(
                "SELECT " +
                        "ts.Input, ts.Name AS SensorName, " +
                        "tfo.Phone AS FlatOwnerPhone, tfo.FirstName AS FlatOwnerFirstName, tfo.LastName AS FlatOwnerLastName, " +
                        "ta.Country, ta.City, ta.Street, ta.HouseNO,  " +
                        "tf.ApartmentNO, " +
                        "tp.Name AS ProjectName, " +
                        "tu.Email AS ProjectMail, tu.LastName AS ProjectOwnerLastName " +
                        "FROM dcs.controllerunit tctrl  " +
                            "JOIN dcs.flat tf ON tf.ID=FlatID " +
                            "JOIN dcs.building tb ON tb.ID=tf.BuildingID " +
                            "JOIN dcs.project tp ON tp.ID=tb.ProjectID " +
                            "JOIN dcs.project_user tpu ON tpu.ProjectID=tb.ProjectID " +
                            "JOIN dcs.user tu ON tu.ID=tpu.UserID " +
                            "JOIN dcs.flatowner_flat tfof ON tfof.FlatID=tctrl.FlatID " +
                            "JOIN dcs.flatowner tfo ON tfo.ID=tfof.FlatOwnerID " +
                            "JOIN dcs.address ta ON ta.ID=tb.AddressID " +
                            "JOIN dcs.sensor ts ON ts.ControllerUnitID=tctrl.ID " +
                        "WHERE tctrl.ID=? AND ts.ID=? LIMIT 1;"
        );

        int col = 0;
        ps.setInt(++col, controllerId);
        ps.setInt(++col, sensorId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        // if nothing
        if (!rs.isBeforeFirst()) {
            return null;
        }

        // process
        rs.next();

        Dictionary dict = new Hashtable();

        // just addition by key value pair for problem view
        dict.put(COMPLEX_PROBLEM_INPUT, rs.getString(COMPLEX_PROBLEM_INPUT));
        dict.put(COMPLEX_PROBLEM_SENSORNAME, rs.getString(COMPLEX_PROBLEM_SENSORNAME));
        dict.put(COMPLEX_PROBLEM_FO_PHONE, rs.getString(COMPLEX_PROBLEM_FO_PHONE));
        dict.put(COMPLEX_PROBLEM_FO_FIRSTNAME, rs.getString(COMPLEX_PROBLEM_FO_FIRSTNAME));
        dict.put(COMPLEX_PROBLEM_FO_LASTNAME, rs.getString(COMPLEX_PROBLEM_FO_LASTNAME));
        dict.put(COMPLEX_PROBLEM_ADDR_COUNTRY, rs.getString(COMPLEX_PROBLEM_ADDR_COUNTRY));
        dict.put(COMPLEX_PROBLEM_ADDR_CITY, rs.getString(COMPLEX_PROBLEM_ADDR_CITY));
        dict.put(COMPLEX_PROBLEM_ADDR_STREET, rs.getString(COMPLEX_PROBLEM_ADDR_STREET));
        dict.put(COMPLEX_PROBLEM_ADDR_NO, rs.getString(COMPLEX_PROBLEM_ADDR_NO));
        dict.put(COMPLEX_PROBLEM_APARTMENT, rs.getString(COMPLEX_PROBLEM_APARTMENT));
        dict.put(COMPLEX_PROBLEM_PROJ_NAME, rs.getString(COMPLEX_PROBLEM_PROJ_NAME));
        dict.put(COMPLEX_PROBLEM_PROJ_OWNER_MAIL, rs.getString(COMPLEX_PROBLEM_PROJ_OWNER_MAIL));
        dict.put(COMPLEX_PROBLEM_PROJ_OWNER_NAME, rs.getString(COMPLEX_PROBLEM_PROJ_OWNER_NAME));

        return dict;
    }




}
