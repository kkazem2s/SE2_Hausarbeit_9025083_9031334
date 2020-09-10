package org.bonn.se.hausarbeit.control.exceptions;

public class NoSuchUserOrPassword extends Exception {
    public NoSuchUserOrPassword() {
        super("Benutzername oder Password ist fehlerhaft!");
    }
    public NoSuchUserOrPassword(String msg) {
        super(msg);
    }
}
