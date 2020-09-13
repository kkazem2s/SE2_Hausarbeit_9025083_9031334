package org.bonn.se.hausarbeit.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.windows.CreatedOffersWindow;
import org.bonn.se.hausarbeit.gui.windows.NewOfferWindow;
import org.bonn.se.hausarbeit.gui.windows.ShowBookingWindow;
import org.bonn.se.hausarbeit.model.dao.AutoDAO;
import org.bonn.se.hausarbeit.model.dto.User;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TopPanel extends HorizontalLayout {

    User user = null;

    public TopPanel(User user) {
        this.user = user;
        this.setSizeFull();

        ThemeResource themeResource = new ThemeResource("images/Logo.png");
        Image logo = new Image(null, themeResource);
        logo.setWidth("100px");

        Label headLabel = new Label("CarLook Ltd. - <i> Autohaus war gestern </i>", ContentMode.HTML);
        headLabel.setSizeUndefined();

        this.addComponent(logo);
        this.addComponent(headLabel);
        this.setComponentAlignment(headLabel, Alignment.MIDDLE_LEFT);

        Button customer = new Button("Reservierte Autos" , VaadinIcons.CAR);
        Button seller = new Button("Erstellte Anzeigen" , VaadinIcons.NEWSPAPER);
        Button seller2 = new Button("Neue Anzeige erstellen", VaadinIcons.PENCIL);

        customer.addClickListener(e -> {
            try {
                if (AutoDAO.getInstance().getBookedCars() == null) {
                    Notification.show("Sie haben bisher keine Autos reserviert!" , Notification.Type.WARNING_MESSAGE);
                } else {
                    UI.getCurrent().addWindow(new ShowBookingWindow());
                }
            } catch (DatabaseException | SQLException ex) {
                Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        seller.addClickListener(e -> {
            if (AutoDAO.getInstance().createdCars() == null) {
                Notification.show("Bisher haben Sie keine Anzeigen erstellt!", Notification.Type.WARNING_MESSAGE);
            } else {
                UI.getCurrent().addWindow(new CreatedOffersWindow());
            }
        });
        seller2.addClickListener(e -> {
            UI.getCurrent().addWindow(new NewOfferWindow());
        });

        if (user.getRole().equals("customer")) {
            this.addComponent(customer);
            this.setComponentAlignment(customer , Alignment.MIDDLE_RIGHT);
        } else {
            if (user.getRole().equals("seller")) {
                this.addComponent(seller);
                this.addComponent(seller2);
                this.setComponentAlignment(seller , Alignment.MIDDLE_RIGHT);
                this.setComponentAlignment(seller2 , Alignment.MIDDLE_RIGHT);
            } else {
                System.out.println(user.getRole());
            }
        }

        Button buttonLogOut = new Button("Logout",VaadinIcons.LEVEL_DOWN_BOLD);
        buttonLogOut.addClickListener(e -> {
            LoginControl.logoutUser();
        });
        this.addComponent(buttonLogOut);
        this.setComponentAlignment(buttonLogOut, Alignment.MIDDLE_RIGHT);
    }
}
