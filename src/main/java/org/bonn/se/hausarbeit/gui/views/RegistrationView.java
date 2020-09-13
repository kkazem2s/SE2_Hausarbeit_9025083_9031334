package org.bonn.se.hausarbeit.gui.views;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dao.UserDAO;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.util.Roles;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;

public class RegistrationView extends VerticalLayout implements View {
    private final Panel auswahlPanel = new Panel("Schritt (1/2): Registrieren als");
    private final Panel userCreationPanel = new Panel("Schritt (2/2): Geben Sie Ihre Daten ein");
    private final Binder<User> binder = new Binder<>();
    private String role = "";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        User user = ((MyUI) UI.getCurrent()).getUser();

        if (user != null) {
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            setUpStep1();
        }
    }

    public void setUpStep1() {
        auswahlPanel.setVisible(true);

        this.addComponent(auswahlPanel);

        this.setComponentAlignment(auswahlPanel, Alignment.MIDDLE_CENTER);

        auswahlPanel.setWidth(37, Unit.PERCENTAGE);
        auswahlPanel.setHeight(20, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        HorizontalLayout prevButtonLayout = new HorizontalLayout();
        content.addComponent(buttonLayout);
        content.addComponent(prevButtonLayout);
        content.setComponentAlignment(prevButtonLayout, Alignment.MIDDLE_CENTER);

        auswahlPanel.setContent(content);
        buttonLayout.setSizeFull();

        Button studentButton = new Button("Kunde");
        Button companyButton = new Button("Vertriebler");
        Button backButton = new Button("Zurück", VaadinIcons.ARROW_CIRCLE_LEFT);

        prevButtonLayout.addComponent(backButton);
        prevButtonLayout.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);

        buttonLayout.setSpacing(false);
        buttonLayout.addComponents(studentButton, companyButton);

        studentButton.setWidth(100, Unit.PERCENTAGE);
        companyButton.setWidth(100, Unit.PERCENTAGE);
        backButton.setWidth(30, Unit.PERCENTAGE);

        studentButton.setHeight("100px");
        companyButton.setHeight("100px");
        backButton.setHeight("40px");

        buttonLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

        role = "";
        studentButton.addClickListener(event -> {
            auswahlPanel.setVisible(false);
            role = Roles.CUSTOMER;
            setUpStep2();
        });

        companyButton.addClickListener(event -> {
            auswahlPanel.setVisible(false);
            role = Roles.SELLER;
            setUpStep2();
        });

        backButton.addClickListener(event -> {
            UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
        });
    }

    public void setUpStep2() {
        userCreationPanel.setVisible(true);
        this.addComponent(userCreationPanel);
        this.setComponentAlignment(userCreationPanel, Alignment.MIDDLE_CENTER);
        userCreationPanel.setWidth("550px");

        Button weiterButton1 = new Button("Fortfahren", VaadinIcons.ARROW_RIGHT);
        weiterButton1.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Button backButton = new Button("Zurück", VaadinIcons.ARROW_LEFT);

        FormLayout content = new FormLayout();

        TextField emailField = new TextField("E-Mail Adresse");
        binder.forField(emailField).asRequired(new EmailValidator("Bitte geben Sie eine gültige E-Mail Adresse an"))
                .bind(User::getEmail, User::setEmail);
        emailField.setSizeFull();

        PasswordField passwordField = new PasswordField("Passwort");
        binder.forField(passwordField).asRequired(new org.bonn.se.hausarbeit.services.util.PasswordValidator())
                .bind(User::getPassword, User::setPassword);
        passwordField.setSizeFull();

        PasswordField passwordCheckField = new PasswordField("Passwort wiederholen");
        passwordCheckField.setSizeFull();

        TextField firstnameField = new TextField("Vorname");
        firstnameField.setSizeFull();
        TextField lastnameField = new TextField("Nachname");
        lastnameField.setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponents(backButton, weiterButton1);

        content.addComponents(emailField, passwordField, passwordCheckField, firstnameField, lastnameField, buttonLayout);

        content.setMargin(true);
        userCreationPanel.setContent(content);

        this.setComponentAlignment(userCreationPanel, Alignment.MIDDLE_CENTER);

        backButton.addClickListener(clickEvent -> {
            userCreationPanel.setVisible(false);

            setUpStep1();
        });

        weiterButton1.addClickListener(clickEvent -> {
            if (!passwordField.getValue().equals(passwordCheckField.getValue())) {
                Notification notification = new Notification("Die Passwörter stimmen nicht überein.", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(4000);
                notification.show(Page.getCurrent());
                //return;
            } else {
                User tester = new User(emailField.getValue(), passwordField.getValue(), firstnameField.getValue(), lastnameField.getValue(), role);
                try {
                    UserDAO userDAO = new UserDAO();
                    if (!userDAO.doesEmailExist(emailField.getValue())) {
                        userDAO.create(tester);
                        setUpStep3();
                        //Notification notification = new Notification("Registrierung Erfolgreich!",Notification.Type.TRAY_NOTIFICATION);
                        //notification.setDelayMsec(4000);
                        //UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
                    } else {
                        Notification notification = new Notification("Diese E-Mail-Adresse ist leider bereits registriert!" , Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_CENTER);
                        notification.setDelayMsec(4000);
                        notification.show(Page.getCurrent());
                        return;
                    }
                } catch (SQLException ex) {
                    Notification.show("SQL");
                    return;
                } catch (DatabaseException ex) {
                    Notification.show("DATABASE");
                }
                userCreationPanel.setVisible(false);
            }
        });
    }
    private void setUpStep3() {
        Notification notification = new Notification("Sie haben sich erfolgreich registriert", Notification.Type.ASSISTIVE_NOTIFICATION);
        notification.setPosition(Position.MIDDLE_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
        UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
    }

}
