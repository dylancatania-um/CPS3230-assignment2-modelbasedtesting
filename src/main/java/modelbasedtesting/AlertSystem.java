package modelbasedtesting;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlertSystem {

    WebDriver driver;

    private boolean alertCreated, alertsDeleted, alertsViewed, alertCreationFailed, alertsDeletionFailed ;

    boolean isAlertCreationFailed() { return alertCreationFailed; }

    boolean areAlertsDeletionFailed() { return alertsDeletionFailed; }
    boolean isAlertCreated() { return alertCreated; }
    boolean areAlertsDeleted() { return alertsDeleted; }
    boolean areAlertsViewed() { return alertsViewed; }

    void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com/Alerts/Login");
        WebElement userId = driver.findElement(By.id("UserId"));
        userId.click();
        userId.sendKeys("b96e4c56-188e-4745-b07f-a480e1ae94b1");
        userId.submit();
    }

    void createAlert() throws IOException {
        URL url = new URL ("https://api.marketalertum.com/Alert");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject obj = new JSONObject();
        obj.put("alertType", 6);
        obj.put("heading", "Jumper Windows 11 Laptop");
        obj.put("description", "Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        obj.put("url", "https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        obj.put("imageUrl", "https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");
        obj.put("postedBy", "b96e4c56-188e-4745-b07f-a480e1ae94b1");
        obj.put("priceInCents", 24999);

        //json string
        final String payload = obj.toString();

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

        alertCreated = true;
        alertsViewed = true;
        alertsDeleted = false;
        alertsDeletionFailed = false;
        alertCreationFailed = false;
    }

    void createAlertInvalid() throws IOException {

        URL url = new URL ("https://api.marketalertum.com/Alert");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject obj = new JSONObject();
        obj.put("alertType", 6);
        obj.put("heading", "Jumper Windows 11 Laptop");
        obj.put("description", "Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        obj.put("url", "https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        obj.put("imageUrl", "https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");

        //invalid attributes
        obj.put("postedBy", "this-is-an-invalid-attribute");
        //passing string instead of integer
        obj.put("priceInCents", "this-is-an-invalid-attribute");

        //json string
        final String payload = obj.toString();

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = con.getResponseCode();
        System.out.println("Creating alert failed: response code: " +status);

        alertCreated = false;
        alertsViewed = false;
        alertsDeleted = false;
        alertsDeletionFailed = false;
        alertCreationFailed = true;
    }

    void deleteAlerts() throws IOException {
       URL url;
		try {
			url = new URL("https://api.marketalertum.com/Alert?userId=b96e4c56-188e-4745-b07f-a480e1ae94b1");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();
            System.out.println("alerts deletion response code: " + status);
		}
        catch (IOException e) {
			e.printStackTrace();
		}
        alertCreated = false;
        alertsViewed = true;
        alertsDeleted = true;
        alertsDeletionFailed = false;
        alertCreationFailed = false;
    }

    void deleteAlertsInvalid() throws IOException {
        URL url;
        try {
            url = new URL("https://api.marketalertum.com/Alert?userId=this-is-an-invalid-user-id");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();

            con.disconnect();
            System.out.println("Deleting all alerts failed: response code: " +status);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        alertCreated = false;
        alertsViewed = false;
        alertsDeleted = false;
        alertsDeletionFailed = true;
        alertCreationFailed = false;
    }

    void viewAlerts() {
        setup();

        alertCreated = false;
        alertsViewed = true;
        alertsDeleted = false;
        alertsDeletionFailed = false;
        alertCreationFailed = false;
    }
}
