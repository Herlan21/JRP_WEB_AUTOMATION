package AutomationWEBSITE_JIP.StepDefinitions;

import AutomationWEBSITE_JIP.BaseTest;
import AutomationWEBSITE_JIP.page.LoginPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class CreateTicketSteps extends BaseTest {

    private LoginPage loginPage;
    private String currentUsername = "";
    private String currentPassword = "";

    // Initialize Driver
    @Before
    public void setup(){
        loginPage = new LoginPage(driver);

    // Reset State
        currentUsername = "";
        currentPassword = "";
    }

    //** STEP DEFINITON ** //
    @Given("user is launch the website")
    public void userIsLaunchWebsite() {
    }

@And("user click form and input username with {string}")
public void userInputUsernameWithUsername(String username) {
    currentUsername = username;
    loginPage.validateLoginPage();
    if (!username.isEmpty()) {
        loginPage.inputUsername(username);
    }
}

    @And("user click form and input password with {string}")
    public void userInputPasswordWithPassword(String password) {
        currentPassword = password == null ? "" : password;

        if (currentPassword.isEmpty()) {
            // 🔥 trigger validation secara nyata
            loginPage.triggerEmptyPasswordValidation();
        } else {
            loginPage.inputPassword(currentPassword);
        }
    }

    @When("user click login button")
    public void userClickLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("user is on CMS dashboard")
    public void userisondashboard() {
    }

    //  Negative Case
    @Then("user get login error based on input condition")
    public void userGetLoginErrorBasedOnCondition() {

        // CASE 1: EMPTY FIELD
        if (currentUsername.isEmpty() || currentPassword.isEmpty()) {
            loginPage.validateInlineAlertForEmptyField(
                    currentUsername,
                    currentPassword
            );
        }

        // CASE 2: WRONG CREDENTIAL
        else {
            loginPage.validateWrongCredentialAlert();
        }
    }

}