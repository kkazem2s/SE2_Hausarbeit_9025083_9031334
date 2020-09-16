package org.bonn.se.hausarbeit.gui.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.model.dao.AutoDAO;
import org.bonn.se.hausarbeit.model.dto.Car;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;

public class ShowBookingWindow extends Window {

    private Car selected = null;
    VerticalLayout content = new VerticalLayout();

    public ShowBookingWindow() throws DatabaseException, SQLException {

        this.setCaption("Ihre reservierten Autos:");
        center();
        setWidth("750px");
        setHeight("500px");

        content.setMargin(true);
        setContent(content);

        Grid<Car> grid = new Grid<>();
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);
        SingleSelect<Car> selection = grid.asSingleSelect();
        grid.addSelectionListener(event -> {
            selected = selection.getValue();
        });

        grid.setItems(AutoDAO.getInstance().getBookedCars());

        grid.addColumn(Car::getBrand).setCaption("Marke");
        grid.addColumn(Car::getModel).setCaption("Modell");
        grid.addColumn(Car::getYear).setCaption("Baujahr");
        grid.addColumn(Car::getDescription).setCaption("Beschreibung");

        content.addComponent(grid);

        Button delete = new Button("Löschen");
        content.addComponent(delete);
        content.setComponentAlignment(delete , Alignment.MIDDLE_CENTER);

        delete.addClickListener(e -> {
            try {
                AutoDAO.getInstance().deleteBooking(selected.getCarID());
                finish();
            } catch (DatabaseException | SQLException ex) {
                Notification.show("Etwas ist schief gelaufen!" , Notification.Type.ERROR_MESSAGE);
            }
        });
    }

    private void finish() {
        UI.getCurrent().removeWindow(this);
        Notification notification = new Notification("Sie haben ihre Buchung erfolgreich storniert!", Notification.Type.ASSISTIVE_NOTIFICATION);
        notification.setPosition(Position.MIDDLE_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
        UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
    }
}
