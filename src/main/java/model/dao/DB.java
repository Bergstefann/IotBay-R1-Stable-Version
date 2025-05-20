package model.dao;

import java.sql.Connection;

public abstract class DB {
    protected final String driver = "org.sqlite.JDBC";
    // Use system property or fall back to a default
    protected final String URL = "jdbc:sqlite:" + 
        System.getProperty("database.path", 
            "C:/Users/thoma/OneDrive/Documents/IotBay R1 Stable Version/src/main/database/iotbay_data.db");
    protected Connection conn;
    
    public abstract Connection openConnection();
    public abstract void closeConnection() throws java.sql.SQLException;
}