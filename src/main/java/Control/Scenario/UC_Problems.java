package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.ComplexInteractions.Others;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Measurement;
import Model.Database.Tables.T_Sensor;
import Model.Web.Specific.Problem;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UC_Problems {
    private final DbProvider db;

    public UC_Problems(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public List<Problem> getProblems() {
        List<Problem> problems = new ArrayList<>();

        try {
            db.beforeSqlExecution(false);

            // get all sensors
            List<T_Sensor> list = I_Sensor.retrieveAll(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Sensor.class));


            for (T_Sensor sensor:
                 list) {
                List<T_Measurement> measurements = I_Measurements.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), sensor.getA_pk());

                // mark as problematic due to no received measurements
                if (measurements.isEmpty()) {
                    Dictionary tmpDict = Others.get_ComplexInformationAboutControllerAndSensor(db.getConn(), db.getPs(), db.getRs(),sensor.getA_pk(), sensor.getA_ControllerUnitID());
                    problems.add(new Problem(tmpDict, "No measurements yet"));
                    continue;
                }

                // get last measurement
                Date lastDate = measurements.get(measurements.size() - 1).getA_MeasuredAt();
                Date nowDate = java.sql.Date.valueOf(LocalDate.now());

                int days = (int)TimeUnit.DAYS.convert((nowDate.getTime() - lastDate.getTime()), TimeUnit.MILLISECONDS);

                if (days > 3) {
                    Dictionary tmpDict = Others.get_ComplexInformationAboutControllerAndSensor(db.getConn(), db.getPs(), db.getRs(),sensor.getA_pk(), sensor.getA_ControllerUnitID());
                    problems.add(new Problem(tmpDict, "Last measurement received " + days + " days ago."));
                    continue;
                }

                // Any skipped requests?
                List<Integer> skipped = new ArrayList<>();
                int expectedRequest = 1;
                int maxRequestNo = measurements.get(measurements.size()-1).getA_RequestNo();
                int index = 0;
                for (int expect = 1; expect <= maxRequestNo; expect++) {
                    if (index >= measurements.size()) {
                        break;
                    }

                    if (expect != measurements.get(index).getA_RequestNo()) {
                        skipped.add(expect);
                    } else {
                        ++index;
                    }
                }

                if (skipped.isEmpty() == false) {
                    Dictionary tmpDict = Others.get_ComplexInformationAboutControllerAndSensor(db.getConn(), db.getPs(), db.getRs(),sensor.getA_pk(), sensor.getA_ControllerUnitID());
                    problems.add(new Problem(tmpDict, "Skipped requests " + skipped));
                    continue;
                }

            }


            db.afterOkSqlExecution();
        } catch (Exception e) {
            db.afterExceptionInSqlExecution(e);
        }

        return problems;
    }


}
