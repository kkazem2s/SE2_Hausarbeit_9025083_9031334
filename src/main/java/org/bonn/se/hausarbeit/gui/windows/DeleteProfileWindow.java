package org.bonn.se.hausarbeit.gui.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dao.UserDAO;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;

public class DeleteProfileWindow extends Window {

    public DeleteProfileWindow() {
        setCaption("Profil löschen:");
        center();

        setWidth("750px");
        setHeight("220px");

        Button buttonYes = new Button("Ja");
        Button buttonNo = new Button("Nein");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(buttonYes,buttonNo);
        horizontalLayout.setSizeFull();
        buttonYes.setSizeFull();
        buttonNo.setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        Label label = new Label("Möchten Sie ihr Profil endgültig löschen? (Diese Aktion kann nicht Rückgängig gemacht werden!");
        //label.setSizeFull();
        verticalLayout.addComponents(label,horizontalLayout);
        verticalLayout.setSizeFull();
        verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        setContent(verticalLayout);

        buttonYes.addClickListener(e -> {
            verticalLayout.setVisible(false);
            setupDelete();
        });
        buttonNo.addClickListener(e -> {
            UI.getCurrent().removeWindow(this);
            //UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        });

    }

    public void setupDelete() {

        VerticalLayout verticalLayout = new VerticalLayout();
        Label label = new Label("Bitte geben Sie erneut ihr Passwort ein:");
        PasswordField passwordField = new PasswordField();
        passwordField.setCaption("Passwort:");
        passwordField.setSizeFull();
        Button buttonSure = new Button("Profil endgültig löschen");
        verticalLayout.addComponent(label);
        verticalLayout.setComponentAlignment(label , Alignment.MIDDLE_CENTER);
        verticalLayout.addComponent(passwordField);
        verticalLayout.addComponent(buttonSure);
        verticalLayout.setComponentAlignment(buttonSure , Alignment.MIDDLE_CENTER);
        setContent(verticalLayout);

        buttonSure.addClickListener(e -> {
            if (passwordField.getValue().equals( ((MyUI) UI.getCurrent()).getUser().getPassword()) ) {
                UserDAO userDAO = new UserDAO();
                try {
                    userDAO.delete(((MyUI) UI.getCurrent()).getUser());
                    LoginControl.logoutUserAfterDelete();
                } catch (DatabaseException | SQLException ignored) {
                    Notification.show("Unable to delete User", Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show("Passwörter stimmen nicht überein!" , Notification.Type.ERROR_MESSAGE);
            }
        });
    }
}
