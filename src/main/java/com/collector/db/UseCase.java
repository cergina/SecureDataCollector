package com.collector.db;

import Database.Interaction.Entities.Address;
import Database.Support.CustomLogs;
import Database.Tables.T_Address;
import Database.Tables.T_User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UseCase {

    private DbProvider db;

    public UseCase(DbProvider dbProvider) {
        db = dbProvider;
    }

    public ArrayList<T_Address> retrieveAllAddress() {
        try {
            return Address.retrieveAll(db.getConn(), db.getPs(), db.getRs());
        } catch (SQLException e) {
            CustomLogs.Error(e.getMessage());
        }
        return null;
    }

    public T_User retrieveUserByEmail(String email) {
//        try {
//            return // TODO
//        } catch (SQLException e) {
//            CustomLogs.Error(e.getMessage());
//        }
        return null;
    }
}
