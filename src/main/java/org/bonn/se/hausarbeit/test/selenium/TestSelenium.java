package org.bonn.se.hausarbeit.test.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.bonn.se.hausarbeit.test.util.DriverSettings;

import java.util.concurrent.TimeUnit;

public class TestSelenium {
    private static WebDriver driver = null;

    public TestSelenium() {}

    @BeforeAll
    public static void setUpClass() {
        System.setProperty( DriverSettings.FIREFOXNAME , DriverSettings.FIREFOXPATH);
        System.setProperty( DriverSettings.DRIVERNAME , DriverSettings.DRIVERPATH );
        driver = new FirefoxDriver();
    }

    @Test
    public void testWebApp() {
        // PART 1 - REGISTER AS SELLER

        // Get on Page
        driver.get("http://localhost:8080");
        // Maximize Page
        driver.manage().window().maximize();
        // Click Registration
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[7]/div/div[2]/div/div[5]/div/div[1]/div")).click();
        // Click Seller
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div[2]/div")).click();
        // Enter E-Mail
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-7\"]")).sendKeys("Selenium@Seller.de");
        // Enter Password
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-9\"]")).sendKeys("Selenium2020");
        // Re-Enter Password
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-11\"]")).sendKeys("Selenium2020");
        // Enter FirstName
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-13\"]")).sendKeys("Selenium");
        // Enter LastName
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-15\"]")).sendKeys("Seller");
        // Continue
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/table/tbody/tr[6]/td[3]/div/div[3]/div")).click();

        // PART 2 - LOGIN AS SELLER

        // Wait for Login Page
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // Enter E-Mail for Login (Seller)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-17\"]")).sendKeys("Selenium@Seller.de");
        // Enter Password for Login (Seller)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-19\"]")).sendKeys("Selenium2020");
        // Click Login
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[7]/div/div[2]/div/div[5]/div/div[3]/div")).click();

        // PART 3 - CREATE CAR

        // Click 'Create new Offer'
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[7]/div")).click();
        // Type Brand
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-22\"]")).sendKeys("Selenium");
        // Type Description
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-24\"]")).sendKeys("This is a test car");
        // Type Model
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-26\"]")).sendKeys("Test");
        // Type Year
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-28\"]")).sendKeys("2020");
        // Create Offer
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[9]/div")).click();
        // Logout Seller
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[11]/div")).click();

        // PART 4 - BOOK CREATED CAR

        // Wait for Login Page
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // Enter E-Mail for Login (Customer)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-3\"]")).sendKeys("Selenium@Customer.de");
        // Enter Password for Login (Customer)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-5\"]")).sendKeys("Selenium2020");
        // Click Login
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[7]/div/div[2]/div/div[5]/div/div[3]/div")).click();
        // Find Car
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[3]/div/div[3]/input")).sendKeys("Selenium");
        // Select Car
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[5]/div/div[2]/div[3]/table/tbody/tr/td[1]")).click();
        // Book Car
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[7]/div")).click();

        // PART 5 - DELETE CREATED BOOKING

        // Open Booked-Cars-Window
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[5]/div")).click();
        // Select Booking
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[1]/div/div[3]/table/tbody/tr/td[1]")).click();
        // Click Delete Booking
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[3]/div")).click();
        // Logout as Customer
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[9]/div")).click();

        // PART 6 - DELETE CREATED CAR

        // Wait for Login Page
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // Enter E-Mail for Login (Seller)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-3\"]")).sendKeys("Selenium@Seller.de");
        // Enter Password for Login (Seller)
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-5\"]")).sendKeys("Selenium2020");
        // Click Login
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[7]/div/div[2]/div/div[5]/div/div[3]/div")).click();
        // Click Created Offers
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[5]/div")).click();
        // Select Car
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[1]/div/div[3]/table/tbody/tr/td[1]")).click();
        // Delete Created Car
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[3]/div")).click();

        // PART 7 - DELETE CREATED SELLER PROFILE

        // Click Delete Profile
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[9]/div")).click();
        // Click Yes
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div/div[3]/div/div/div[1]/div")).click();
        // Enter Password
        driver.findElement(By.xpath("//*[@id=\"gwt-uid-9\"]")).sendKeys("Selenium2020");
        // Click Delete
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[3]/div/div/div[5]/div")).click();
    }

    @AfterAll
    public static void tearDownClass() {
        driver.quit();
    }
}

/*

    1. Register as Seller
    2. Login as Seller
    3. Create Car
    4. Logout
    5 Login Customer
        Selenium@Customer.de
        Selenium2020
    6 Book created Car
    7 Delete Booking
    8 Logout Customer
    9 Login Seller
    10 Delete Car
    11. Delete Profile

 */