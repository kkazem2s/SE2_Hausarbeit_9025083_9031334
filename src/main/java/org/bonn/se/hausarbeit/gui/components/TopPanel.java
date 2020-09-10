package org.bonn.se.hausarbeit.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.gui.windows.CreatedOffersWindow;
import org.bonn.se.hausarbeit.gui.windows.NewOfferWindow;
import org.bonn.se.hausarbeit.gui.windows.ShowBookingWindow;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TopPanel extends HorizontalLayout {

    User user = null;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        User user = ((MyUI) UI.getCurrent()).getUser();

        if (user != null) {
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            if (user.getRole().equals("customer")) {
                setUpCustomer();
            } else {
                //setUpSeller();
            }
        }
    }
    public void setUpCustomer() {

    }
    public TopPanel(User user) {
        this.user = user;
        this.setSizeFull();

        ThemeResource themeResource = new ThemeResource("images/Logo.png");
        Image logo = new Image(null, themeResource);
        logo.setWidth("100px");

        Label headlabel = new Label("CarLook Ltd. - <i> Autohaus war gestern </i>", ContentMode.HTML);
        headlabel.setSizeUndefined();

        this.addComponent(logo);
        this.addComponent(headlabel);
        this.setComponentAlignment(headlabel, Alignment.MIDDLE_LEFT);

        Button customer = new Button("Reservierte Autos" , VaadinIcons.CAR);
        Button seller = new Button("Erstellte Anzeigen" , VaadinIcons.NEWSPAPER);
        Button seller2 = new Button("Neue Anzeige erstellen", VaadinIcons.PENCIL);

        customer.addClickListener(e -> {
            try {
                UI.getCurrent().addWindow(new ShowBookingWindow());
            } catch (DatabaseException | SQLException ex) {
                Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        seller.addClickListener(e -> {
           UI.getCurrent().addWindow(new CreatedOffersWindow());
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
                System.out.println(user.getRole().toString());
            }
        }

        Button buttonLogOut = new Button("Logout",VaadinIcons.LEVEL_DOWN_BOLD);
        buttonLogOut.addClickListener(e -> {
            LoginControl.logoutUser();
        });
        this.addComponent(buttonLogOut);
        this.setComponentAlignment(buttonLogOut, Alignment.MIDDLE_RIGHT);
        //this.setSizeFull();
    }
}
