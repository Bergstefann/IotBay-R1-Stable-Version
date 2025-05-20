package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseDAO {
    
    protected Connection conn;
    protected Statement st;
    private static final Logger LOGGER = Logger.getLogger(BaseDAO.class.getName());
    
    public BaseDAO(Connection conn) throws SQLException {
        this.conn = conn;
        if (conn != null) {
            this.st = conn.createStatement();
        } else {
            LOGGER.log(Level.SEVERE, "Database connection is null");
            throw new SQLException("Database connection is null");
        }
    }
    
    public void close() {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error closing statement", ex);
        }
    }
    
    protected boolean executeUpdate(String sql) {
        try {
            st.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing SQL: " + sql, ex);
            return false;
        }
    }
    
    protected String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("'", "''");
    }
}