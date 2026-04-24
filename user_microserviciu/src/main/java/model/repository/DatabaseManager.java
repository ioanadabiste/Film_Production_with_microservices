package model.repository;

import java.sql.*;

public class DatabaseManager {

    private static final String SCHEMA = "filmmanagement";
    private static final String TEST_SCHEMA = "test_filmmanagement";

    public static DatabaseConfig getConnectionWrapper(boolean test){
        if(test){
            return new DatabaseConfig(TEST_SCHEMA);
        }else{
            return new DatabaseConfig(SCHEMA);
        }
    }
}