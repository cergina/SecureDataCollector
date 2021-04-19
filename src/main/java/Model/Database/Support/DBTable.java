/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */
package Model.Database.Support;

public interface DBTable {

    boolean IsTableOkForDatabaseEnter();

    boolean WasTableWithdrawedCorrectlyFromDatabase();

    String InfoPrintAllColumns();

}
