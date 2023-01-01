package modelbasedtesting;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginSystem {

    WebDriver driver;
    private boolean loggedIn = false, loggedOut = true, loginInvalid = false;

    boolean isLoginInvalid() { return loginInvalid; }
    boolean isLoggedIn() { return loggedIn;}

    boolean isLoggedOut() { return loggedOut;}

    void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdriver/chromedriver.exe");
        driver = new ChromeDriver();
    }

    void validLogin() {
        setup();
        driver.get("https://www.marketalertum.com/Alerts/Login");
        WebElement userId = driver.findElement(By.id("UserId"));
        userId.click();
        userId.sendKeys("b96e4c56-188e-4745-b07f-a480e1ae94b1");
        userId.submit();

        String loginTitle = driver.getCurrentUrl();
        Assertions.assertEquals("https://www.marketalertum.com/Alerts/List", loginTitle);

        loggedIn = true;
        loggedOut = false;
        loginInvalid = false;
    }

    void invalidLogin() {
        setup();
        driver.get("https://www.marketalertum.com/Alerts/Login");
        WebElement userId = driver.findElement(By.id("UserId"));
        userId.click();
        userId.sendKeys("this-is-an-invalid-user-id");
        userId.submit();

        String loginTitle = driver.getCurrentUrl();
        Assertions.assertEquals("https://www.marketalertum.com/Alerts/Login", loginTitle);

        loggedIn = false;
        loggedOut = true;
        loginInvalid = true;
    }

    void logOut() {
        setup();
        driver.get("https://www.marketalertum.com/Alerts/List");

        //finding Logout button
        WebElement logOutButton = driver.findElement(By.xpath("/html/body/header/nav/div/div/ul/li[3]/a"));
        logOutButton.click();

        loggedOut = true;
        loggedIn = false;
        loginInvalid = false;
    }

}
