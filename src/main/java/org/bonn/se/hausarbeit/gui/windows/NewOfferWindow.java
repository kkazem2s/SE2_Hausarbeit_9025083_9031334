package org.bonn.se.hausarbeit.gui.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dao.AutoDAO;
import org.bonn.se.hausarbeit.model.dto.Car;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;

public class NewOfferWindow extends Window {
    public NewOfferWindow() {
        super("Neue Anzeige erstellen:");
        center();
        setWidth("400px");

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        TextField brand = new TextField();
        brand.setCaption("Marke:");
        brand.setSizeFull();
        TextField description = new TextField();
        description.setCaption("Beschreibung");
        description.setSizeFull();
        TextField model = new TextField();
        model.setCaption("Modell:");
        model.setSizeFull();
        TextField year = new TextField();
        year.setCaption("Baujahr");
        year.setSizeFull();

        Button finish = new Button("Anzeige erstellen", VaadinIcons.ARROW_CIRCLE_UP_O);



        finish.addClickListener(e -> {
            if (year.getValue().matches("-?\\d+")) {
                try {
                    Car c = new Car(((MyUI) UI.getCurrent()).getUser().getUserID(), brand.getValue(), description.getValue(), model.getValue(), Integer.parseInt(year.getValue()));
                    AutoDAO.getInstance().insertCar(((MyUI) UI.getCurrent()).getUser(), c);
                    Notification notification = new Notification("Sie ihre Anzeige erfolgreich hinzugefügt!", Notification.Type.ASSISTIVE_NOTIFICATION);
                    UI.getCurrent().removeWindow(this);
                    finish();
                } catch (DatabaseException | SQLException exception) {
                    Notification.show("Bitte korrekte Zahl für ein Jahr eingeben!" , Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show("Bitte korrekte Zahl für ein Jahr eingeben!" , Notification.Type.ERROR_MESSAGE);
            }
        });
        content.addComponents(brand,description,model,year,finish);
        content.setComponentAlignment(finish, Alignment.MIDDLE_CENTER);
    }

    private void finish() {
        Notification notification = new Notification("Sie ihre Anzeige erfolgreich hinzugefügt!", Notification.Type.ASSISTIVE_NOTIFICATION);
        notification.setPosition(Position.MIDDLE_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
        UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
    }
}
