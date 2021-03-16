package Model.Database.Tables;

import java.util.ArrayList;

public class DbEntity {
    private static DbEntity reference = new DbEntity();
    public static String[] TABLE_CODENAMES = {
        "Reference", "Table", "CodeNames"
    };

    public ArrayList<String> GenerateHtmlTableRow_FromDbRow() {
        ArrayList<String> arr = new ArrayList<String>();

        return arr;
    }

    public String[] GetTableCodeNames() {
        String[] str = {};

        return str;
    }
}
