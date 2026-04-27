package AutomationWEBSITE_JIP.page;


import AutomationWEBSITE_JIP.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class TicketPage extends BaseTest {

    private WebDriver driver;

    public TicketPage(WebDriver driver) {
        this.driver = driver;
    }

//**Complaint & Request Locator**//

//Complaint Locator
   By ticketSidebarDropdown = By.xpath("//a[contains(@class,'menu-item') and .//span[contains(text(),'Komplain')]]");
   By complaintSidebar = By.xpath("//a[normalize-space()='Komplain']");
   By requestSidebar = By.xpath("//a[normalize-space()='Permintaan']");
   By wrongCredAlert = By.xpath("//div[contains(@class,'ant-notification-notice-error')]");

//**LOGIN METHOD ACTION**//
//Login page validation
    public void validateLoginPage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailForm)).isDisplayed();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordForm)).isDisplayed();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
        wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeWordingCms)).isDisplayed();
    }

    public void validateAlertLoginPage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputUsernameAlert)).isDisplayed();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputPasswordAlert)).isDisplayed();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    // ===== EMPTY FIELD ALERT (INLINE) =====
    public void validateInlineAlertForEmptyField(String username, String password) {

        if (username.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputUsernameAlert));
            Assert.assertTrue(isElementDisplayed(inputUsernameAlert),
                    "Username inline alert should be displayed");
        }

        if (password.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputPasswordAlert));
            Assert.assertTrue(isElementDisplayed(inputPasswordAlert),
                    "Password inline alert should be displayed");
        }

        // Pastikan TIDAK ada push notification
        Assert.assertFalse(
                isElementDisplayed(wrongCredAlert),
                "Wrong credential notification should NOT be displayed"
        );
    }

public void validateWrongCredentialAlert() {
    try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(wrongCredAlert));
        Assert.assertTrue(
                isElementDisplayed(wrongCredAlert),
                "Expected wrong credential notification to be displayed"
        );
    } catch (TimeoutException e) {
        Assert.fail("Wrong credential notification did NOT appear");
    }
}

//Login form action method
    public void inputUsername(String username){
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailForm)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailForm)).sendKeys(username);
    }

    public void inputPassword(String password){
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordForm)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordForm)).sendKeys(password);
    }

    public void clickLoginButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).click();
    }

    public void triggerEmptyPasswordValidation() {
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordForm)
        );
        password.click();
        password.sendKeys("a");
        password.sendKeys(Keys.BACK_SPACE); // sekarang kosong tapi "touched"
    }
}