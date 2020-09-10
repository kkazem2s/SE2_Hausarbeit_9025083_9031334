package org.bonn.se.hausarbeit.services.db;

import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.services.util.LoginData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCConnection {

    private static JDBCConnection connection = null;

    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de:5432/mschub2s";

    private Connection conn;

    private String login = LoginData.USERNAME;
    private String password = LoginData.PASSWORD;

    public static JDBCConnection getInstance() throws DatabaseException {
        if (connection == null) {
            connection = new JDBCConnection();
        }
        return connection;
    }

    private JDBCConnection() throws DatabaseException {
        this.initConnection();
    }

    public void initConnection() throws DatabaseException{
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.openConnection();
    }

    public void openConnection() throws DatabaseException {
        try {
            Properties props = new Properties();

            props.setProperty("user", login);
            props.setProperty("password", password);

            this.conn = DriverManager.getConnection(this.url, props);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler beim Zugriff auf die Datenbank!\nSichere Verbindung vorhanden?");
        }
    }

    public Statement getStatement() throws DatabaseException {
        try {
            if (this.conn.isClosed()) {
                this.openConnection();
            }
            return this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String sql) throws DatabaseException {
        try {
            if (this.conn.isClosed()) {
                this.openConnection();
            }
            return this.conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
