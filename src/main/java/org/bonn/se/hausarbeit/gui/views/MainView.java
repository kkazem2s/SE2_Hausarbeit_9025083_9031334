package org.bonn.se.hausarbeit.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.control.LoginControl;
import org.bonn.se.hausarbeit.control.exceptions.DatabaseException;
import org.bonn.se.hausarbeit.gui.components.TopPanel;
import org.bonn.se.hausarbeit.gui.ui.MyUI;
import org.bonn.se.hausarbeit.model.dao.AutoDAO;
import org.bonn.se.hausarbeit.model.dto.Car;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.util.Views;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        User user = ((MyUI) UI.getCurrent()).getUser();

        if (user == null) {
            UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
        } else {
            setUp();
        }
    }

    private int anzahl = 0;
    private Car selected = null;

    void setUp() {
        TopPanel panel = new TopPanel(((MyUI) UI.getCurrent()).getUser());
        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        panel.setSizeUndefined();

        final HorizontalLayout horizontalLayout = new HorizontalLayout();

        final Label labelText = new Label("Suche: ");
        Button buttonSuche = new Button("" , VaadinIcons.SEARCH);
        Button buttonBuche = new Button("Reservieren", VaadinIcons.BOOK);
        final TextField textField = new TextField();

        horizontalLayout.addComponent(labelText);
        horizontalLayout.setComponentAlignment(labelText, Alignment.MIDDLE_CENTER);
        horizontalLayout.addComponent(textField);
        horizontalLayout.addComponent(buttonSuche);

        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);

        Grid<Car> grid = new Grid<>();
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);

        SingleSelect<Car> selection = grid.asSingleSelect();

        grid.addSelectionListener(event -> {
            this.selected = selection.getValue();
        });

        /*
        buttonSuche.addClickListener(event -> {
            String marke = textField.getValue();
            List<Car> list = AutoDAO.getInstance().getAuto(marke);
            //List<Auto> list = AutoSearch.getInstance().getAutoByMarke(marke);

            if (marke.equals("")) {
                Notification.show(null, "Bitte Automarke eingeben!", Notification.Type.WARNING_MESSAGE);
            }
            anzahl += 1;

            grid.removeAllColumns();
            grid.setCaption("Treffer für '" + marke + "'\n(Anzahl der Suchen: " + anzahl + ")");

            grid.setItems(list);

            //grid.addColumn(Car::getCarID).setCaption("CarID");
            grid.addColumn(Car::getBrand).setCaption("Marke");
            grid.addColumn(Car::getModel).setCaption("Modell");
            grid.addColumn(Car::getYear).setCaption("Baujahr");
            grid.addColumn(Car::getDescription).setCaption("Beschreibung");

        });
         */

        textField.addValueChangeListener(event -> {
            if (!textField.getValue().equals("")) {
                String marke = textField.getValue();
                List<Car> list = AutoDAO.getInstance().getAuto(marke);

                if (marke.equals("")) {
                    Notification.show(null, "Bitte Automarke eingeben!", Notification.Type.WARNING_MESSAGE);
                }
                anzahl += 1;

                grid.removeAllColumns();
                grid.setCaption("Treffer für '" + marke + "'\n(Anzahl der Suchen: " + anzahl + ")");

                grid.setItems(list);

                grid.addColumn(Car::getBrand).setCaption("Marke");
                grid.addColumn(Car::getModel).setCaption("Modell");
                grid.addColumn(Car::getYear).setCaption("Baujahr");
                grid.addColumn(Car::getDescription).setCaption("Beschreibung");

                if (((MyUI) UI.getCurrent()).getUser().getRole().equals("customer")) {
                    addComponent(buttonBuche);
                    setComponentAlignment(buttonBuche, Alignment.MIDDLE_CENTER);
                }

            } else {
                return;
            }
        });

        buttonBuche.addClickListener(event -> {
            Notification.show("Gebuchtes Auto: " + this.selected.getBrand() + " " + this.selected.getModel() , Notification.Type.WARNING_MESSAGE);
            try  {
                AutoDAO.getInstance().bookCar(((MyUI) UI.getCurrent()).getUser() , this.selected);
            } catch (SQLException | DatabaseException ex) {
                ex.printStackTrace();
                Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
                //throw new DatabaseException("Fehler im SQL-Befehl! Bitte den Programmierer benachrichtigen!");
            }
        });

        addComponent(grid);
    }

}
