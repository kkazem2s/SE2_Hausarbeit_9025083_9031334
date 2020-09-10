package org.bonn.se.hausarbeit.gui.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.model.dao.AutoDAO;
import org.bonn.se.hausarbeit.model.dto.Car;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreatedOffersWindow extends Window {
    private Car selected = null;
    public CreatedOffersWindow() {
        super("Ihre erstellen Anzeigen:");
        center();
        setWidth("750px");
        setHeight("500px");

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        if (AutoDAO.getInstance().createdCars() == null) {
            Label label = new Label("Bisher haben Sie keine Autos eingestellt!");
            content.addComponent(label);
        } else {
            Grid<Car> grid = new Grid<>();
            grid.setSizeFull();
            grid.setHeightMode(HeightMode.UNDEFINED);
            SingleSelect<Car> selection = grid.asSingleSelect();
            grid.addSelectionListener(event -> {
                selected = selection.getValue();
            });

            grid.setItems(AutoDAO.getInstance().createdCars());

            grid.addColumn(Car::getCarID).setCaption("CarID");
            grid.addColumn(Car::getBrand).setCaption("Marke");
            grid.addColumn(Car::getModel).setCaption("Modell");
            grid.addColumn(Car::getYear).setCaption("Baujahr");
            grid.addColumn(Car::getDescription).setCaption("Beschreibung");

            content.addComponent(grid);

            Button delete = new Button("löschen");
            content.addComponent(delete);
            content.setComponentAlignment(delete , Alignment.MIDDLE_CENTER);

            delete.addClickListener(e -> {
                try {
                    int i = selection.getValue().getCarID();
                    System.out.println(i);
                    finish();
                    UI.getCurrent().removeWindow(this);
                    AutoDAO.getInstance().deleteCarByID(selected.getCarID());
                } catch (DatabaseException | SQLException ex) {
                    //Notification.show("Etwas ist schief gelaufen!" , Notification.Type.ERROR_MESSAGE);
                }
            });
        }
    }
    private void finish() {
        Notification notification = new Notification("Sie haben ihre Anzeige erfolgreich entfernt!", Notification.Type.ASSISTIVE_NOTIFICATION);
        notification.setPosition(Position.MIDDLE_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
        UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
    }
}
