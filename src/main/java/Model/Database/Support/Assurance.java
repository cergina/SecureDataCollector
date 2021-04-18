/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Model.Database.Support;

import java.sql.Date;
import java.sql.SQLException;

public class Assurance {
    // complex types

    // base types
    public static boolean isDateOk(Date value) {
        if (null == value)
            return false;

        return true;
    }

    public static boolean isIntOk(int value) {
        return true;
    }

    public static boolean isFkOk(int value) {
        if (value <= 0)
            return false;

        return true;
    }

    public static boolean isVarcharOk(String value) {
        if (null == value)
            return false;

        return true;
    }

    public static void varcharCheck(String value) throws SQLException {
        if (Assurance.isVarcharOk(value) == false)
            throw new SQLException("Value is not valid for use in database.");
    }

    public static void idCheck(int idToBeChecked) throws SQLException {
        if (idToBeChecked <= 0)
            throw new SQLException("ID is less or equal to 0, which is an invalid Database id.");
    }
}
