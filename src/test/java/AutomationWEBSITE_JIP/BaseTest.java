package AutomationWEBSITE_JIP;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    public static WebDriver driver;
    public static WebDriverWait wait;

    public static byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void setupDriver(){
        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public static void openUrl(String url) {
        driver.get(url);
    }

    public static void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}