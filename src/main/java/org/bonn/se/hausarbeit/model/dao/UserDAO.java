package org.bonn.se.hausarbeit.model.dao;

import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.db.JDBCConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends AbstractDAO {

    public void create(User user) throws DatabaseException, SQLException {
        try {
            Statement statement = JDBCConnection.getInstance().getStatement();

            statement.executeQuery("INSERT INTO \"carlookdb\".user (email, password, firstname, lastname, role) " +
                    "VALUES ('" + user.getEmail() + "', '" + user.getPassword() + "', '" + user.getFirstname() + "', '" + user.getLastname() + "', '" + user.getRole() + "') " +
                    "RETURNING *");
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }

    public boolean doesEmailExist(String mail) throws DatabaseException, SQLException {
        ResultSet set = null;

        try {
            Statement statement = JDBCConnection.getInstance().getStatement();

            set = statement.executeQuery("SELECT * "
                    + "FROM CarLookDB.user "
                    + "WHERE CarLookDB.user.email = \'" + mail + "\'");
        }  catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
        try {
            while (set.next()) {
                String test = set.getString(2);
                if (test.equals(mail)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
