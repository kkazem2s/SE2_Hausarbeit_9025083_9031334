package org.bonn.se.hausarbeit.model.dao;

import com.vaadin.ui.UI;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dto.Car;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.db.JDBCConnection;
import org.postgresql.util.PSQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoDAO extends AbstractDAO {
    public static AutoDAO dao = null;

    private AutoDAO() {

    }

    public static AutoDAO getInstance() {
        if (dao == null) {
            dao = new AutoDAO();
        }
        return dao;
    }
    public List<Car> getAuto(String str) {
        Statement statement = this.getStatement();

        ResultSet rs = null;

        try {
            rs = statement.executeQuery(
                    "SELECT * FROM CarLookDB.car");
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (rs == null) {
            return null;
        }
        List<Car> liste = new ArrayList<Car>();
        Car car = null;

        try {
            //System.out.println(rs.getString(1));
            while(rs.next()) { //Marke, Modell, Baujahr, Beschreibung
                if (rs.getString(3).contains(str) || rs.getString(4).contains(str) || rs.getString(5).contains(str) || rs.getString(6).contains(str)) {
                    car = new Car();
                    car.setCarID(rs.getInt(1));
                    car.setSellerID(rs.getInt(2));
                    car.setBrand(rs.getString(3));
                    car.setDescription(rs.getString(4));
                    car.setModel(rs.getString(5));
                    car.setYear(rs.getInt(6));
                    liste.add(car);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return liste;
    }
    public void insertCar(User user, Car car) throws DatabaseException, SQLException {
        ResultSet set = null;

        try {
            //DB-Zugriff
            Statement statement = JDBCConnection.getInstance().getStatement();

            set = statement.executeQuery("INSERT INTO \"carlookdb\".car (userid, brand, description, model, year) " +
                    "VALUES ('" + user.getUserID() + "', '" + car.getBrand() + "', '" + car.getDescription() + "', '" + car.getModel() + "', '" + car.getYear() + "') " +
                    "RETURNING *");
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }

    public List<Car> createdCars() {
        Statement statement = this.getStatement();

        ResultSet rs = null;

        Integer id = ((MyUI) UI.getCurrent()).getUser().getUserID();

        try {
            rs = statement.executeQuery(
                    "SELECT * FROM \"carlookdb\".car WHERE \"carlookdb\".car.userid = \'" + id + "\'");
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (rs == null) {
            return null;
        }
        List<Car> liste = new ArrayList<Car>();
        Car car = null;

        try {
            //System.out.println(rs.getString(1));
            while(rs.next()) { //Marke, Modell, Baujahr, Beschreibung
                 car = new Car();
                 car.setCarID(rs.getInt(1));
                 car.setSellerID(rs.getInt(2));
                 car.setBrand(rs.getString(3));
                 car.setDescription(rs.getString(4));
                 car.setModel(rs.getString(5));
                 car.setYear(rs.getInt(6));
                 liste.add(car);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return liste;
    }

    public void deleteCarByID(int id) throws SQLException, DatabaseException {
        System.out.println(id);
        //Statement statement = JDBCConnection.getInstance().getStatement();
        try {
            deleteAllBooking(id);
            JDBCConnection.getInstance().getStatement().executeQuery("DELETE FROM carlookdb.car WHERE carlookdb.car.carid = \'" + id + "\'");
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }
    public void deleteAllBooking(int id) throws SQLException, DatabaseException {
        try {
            JDBCConnection.getInstance().getStatement().executeQuery("DELETE FROM carlookdb.reservation WHERE carlookdb.reservation.carid = \'" + id + "\'");
        } catch (SQLException ex) {
            JDBCConnection.getInstance().getStatement().executeQuery("DELETE FROM carlookdb.car WHERE carlookdb.car.carid = \'" + id + "\'");
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }
    public void deleteBooking(int id) throws SQLException, DatabaseException {
        try {
            JDBCConnection.getInstance().getStatement().executeQuery("DELETE FROM carlookdb.reservation WHERE carlookdb.reservation.carid = \'" + id + "\' AND  carlookdb.reservation.userid = \'" + ((MyUI) UI.getCurrent()).getUser().getUserID() + "\'");
        } catch (SQLException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }
    public void bookCar(User u, Car c) throws SQLException, DatabaseException {
        try {
            JDBCConnection.getInstance().getStatement().executeQuery("INSERT INTO carlookdb.reservation (carid, userid) " +
                    "VALUES ('" + c.getCarID() + "', '" + u.getUserID() + "') " +
                    "RETURNING *");
        } catch (SQLException | DatabaseException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
    }
    public List<Car> getBookedCars() throws SQLException, DatabaseException{
        ResultSet rs = null;

        List<Car> list = new ArrayList<Car>();
        Car car = null;

        try {
             rs = JDBCConnection.getInstance().getStatement().executeQuery("SELECT * FROM carlookdb.car, carlookdb.reservation WHERE carlookdb.reservation.userid = \'" + ((MyUI) UI.getCurrent()).getUser().getUserID() + "\' " +
                     "AND carlookdb.reservation.carid = carlookdb.car.carid");
        } catch (SQLException | DatabaseException ex) {
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
        }
        while(rs.next()) { //Marke, Modell, Baujahr, Beschreibung
            car = new Car();
            car.setCarID(rs.getInt(1));
            car.setSellerID(rs.getInt(2));
            car.setBrand(rs.getString(3));
            car.setDescription(rs.getString(4));
            car.setModel(rs.getString(5));
            car.setYear(rs.getInt(6));
            list.add(car);
        }
        return list;
    }
}
