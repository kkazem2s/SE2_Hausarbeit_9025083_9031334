package org.bonn.se.hausarbeit.model.dto;

import java.io.Serializable;

public class Car implements java.io.Serializable {

    private int carID;
    private int sellerID;
    private String brand;
    private String description;
    private String model;
    private int year;

    public Car(){}

    public Car(int carID, int sellerID, String brand, String description, String model, int year) {
        this.carID = carID;
        this.sellerID = sellerID;
        this.brand = brand;
        this.description = description;
        this.model = model;
        this.year = year;
    }
    public Car(int sellerID, String brand, String description, String model, int year) {
        this.sellerID = sellerID;
        this.brand = brand;
        this.description = description;
        this.model = model;
        this.year = year;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
