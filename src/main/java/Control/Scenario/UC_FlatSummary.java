package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Web.User;

import javax.validation.constraints.NotNull;

/**
 * Use Case class for FlatSummaryView
 */
public class UC_FlatSummary {
    private DbProvider db;

    public UC_FlatSummary(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public boolean isLoggedUserAllowedToView(User loggedUser, int projectId) {

        return true;
    }


}
