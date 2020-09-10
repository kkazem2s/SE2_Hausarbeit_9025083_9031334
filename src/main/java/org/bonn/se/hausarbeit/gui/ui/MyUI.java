package org.bonn.se.hausarbeit.gui.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.bonn.se.hausarbeit.gui.views.*;
import org.bonn.se.hausarbeit.model.dto.User;
import org.bonn.se.hausarbeit.services.util.Views;

@Theme("mytheme")
@Title("CarLook Ltd.")
@PreserveOnRefresh

public class MyUI extends UI {

    private User user = null;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Navigator navi = new Navigator(this,this);

        navi.addView(Views.MAIN, MainView.class);
        navi.addView(Views.LOGIN, LoginView.class);
        navi.addView(Views.REGISTRATION, RegistrationView.class);

        UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);

    }

    public MyUI getMyUI() {
        return (MyUI) UI.getCurrent();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
    //CarLook Ltd.

}
