package org.bonn.se.hausarbeit.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.control.exceptions.NoSuchUserOrPassword;
import org.bonn.se.hausarbeit.services.db.JDBCConnection;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginControl {
    public static void checkAuthentication(String login, String password) throws NoSuchUserOrPassword, DatabaseException {

        ResultSet set = null;

        try {
            //DB-Zugriff
            Statement statement = JDBCConnection.getInstance().getStatement();

            set = statement.executeQuery("SELECT * "
                    + "FROM CarLookDB.user "
                    + "WHERE CarLookDB.user.email = \'" + login + "\'"
                    + " AND CarLookDB.user.password = \'" + password + "\'");

        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }

        User user = null;

        try {
            if (set.next()) {
                user = new User();
                user.setUserID(set.getInt(1));
                user.setEmail(set.getString(2));
                user.setPassword(set.getString(3));
                user.setFirstname(set.getString(4));
                user.setLastname(set.getString(5));
                user.setRole(set.getString(6));
            }
            else {
                throw new NoSuchUserOrPassword();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

        //VaadinSession session = UI.getCurrent().getSession();
        //session.setAttribute(Roles.CURRENT_USER, user);

        ((MyUI) UI.getCurrent()).setUser(user);
        UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
    }

    public static void logoutUser() {
        UI.getCurrent().close();
        UI.getCurrent().getPage().setLocation("/login");
    }
}
