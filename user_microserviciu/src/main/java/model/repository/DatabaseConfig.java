package model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String DB_URL = "jdbc:mysql://localhost/"; // 127.0.0.1

    private static final String USER = "root";

    private static final String PASSWORD = "root";

    private static final int TIMEOUT = 5;

    private Connection connection;


    public DatabaseConfig(String schema){

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + schema  , USER, PASSWORD);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    }
}