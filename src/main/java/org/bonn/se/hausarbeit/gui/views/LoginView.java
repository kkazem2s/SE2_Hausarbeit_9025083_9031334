package org.bonn.se.hausarbeit.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.control.exceptions.NoSuchUserOrPassword;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.util.Views;

public class LoginView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        User user = ((MyUI) UI.getCurrent()).getUser();

        if (user != null) {
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            setUp();
        }
    }

    public void setUp() {
        ThemeResource themeResource = new ThemeResource("images/Logo.png");
        Image logo = new Image(null, themeResource);
        logo.setWidth("250px");
        logo.addStyleName("logo");

        Label platzhalterLabel = new Label("&nbsp", ContentMode.HTML);

        Label labelText = new Label("CarLook Ltd. - Autohaus war gestern!");

        this.addComponent(logo);
        this.addComponent(labelText);

        this.setComponentAlignment(logo, Alignment.TOP_CENTER);
        this.setComponentAlignment(labelText, Alignment.TOP_CENTER);
        this.addComponents(platzhalterLabel);
        platzhalterLabel.setHeight("60px");

        final TextField userLogin = new TextField();
        userLogin.setCaption("E-Mail Adresse:");
        userLogin.setSizeFull();
        final PasswordField passwordField = new PasswordField();
        passwordField.setCaption("Passwort:");
        passwordField.setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(userLogin,passwordField);

        Panel panel = new Panel("Bitte Login-Daten eingeben:");
        panel.addStyleName("login");


        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        panel.setContent(layout);

        Button buttonLogin = new Button("Login", VaadinIcons.PAPERPLANE_O);
        Button buttonReg = new Button("Registrieren", VaadinIcons.ARROW_CIRCLE_RIGHT_O);
        HorizontalLayout newh = new HorizontalLayout();
        newh.addComponents(buttonReg,buttonLogin);
        layout.addComponent(newh);
        panel.setSizeUndefined();

        buttonLogin.addClickListener(e -> {
           String login = userLogin.getValue();
           String password = passwordField.getValue();
            try {
                LoginControl.checkAuthentication(login,password);
            } catch (NoSuchUserOrPassword | DatabaseException ex) {
                Notification.show("Fehler","E-Mail oder Passwort falsch", Notification.Type.ERROR_MESSAGE);
                userLogin.setValue("");
                passwordField.setValue("");
            }
        });

        buttonReg.addClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.REGISTRATION);
        });
    }
}
