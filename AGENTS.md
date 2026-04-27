# AGENTS.md - JRP Web Automation Test Suite Guide

## Project Overview
This is a **Selenium + Cucumber BDD (Behavior-Driven Development) test automation framework** for the JIP CMS web application. It uses the **Page Object Model (POM)** pattern combined with Cucumber's Gherkin syntax for test scenarios.

**Target Application**: https://cms-jip.folkatech.com

## Architecture & Key Components

### 1. **Test Execution Flow**
- **TestRunner.java** - Cucumber entry point (extends `AbstractTestNGCucumberTests`)
  - Features loaded from: `src/test/resources/features/**`
  - Glue path (step definitions): `AutomationWEBSITE_JIP` package
  - Reports generated: HTML (`target/report-cucumber.html`), JSON (`target/cucumber-json.json`)

### 2. **Page Object Model (POM) Pattern**
- **BaseTest.java** - Static WebDriver management
  - Single static `WebDriver driver` instance (shared across tests)
  - Static `WebDriverWait wait` (15-second timeout default)
  - Methods: `setupDriver()`, `openUrl()`, `stopDriver()`, `takeScreenshot()`
- **LoginPage.java** - Login page object (extends BaseTest)
  - Element locators use IDs, XPath (e.g., `By.id("login_username")`, `By.xpath("//button[.//span...")`)
  - Methods separated into validation (assert UI elements visible) and action methods (input, click)
  - Explicit waits via `wait.until(ExpectedConditions.visibilityOfElementLocated(...))`

### 3. **Cucumber Integration**
- **CucumberHooks.java** - Lifecycle management
  - `@Before`: Initializes ChromeDriver, navigates to base URL
  - `@After`: Captures screenshot on failure, enforces 10-second pause after every scenario, then closes driver
  - Note: 20-second pause on failure (10s in try block + 10s final pause)
- **LoginSteps.java** - Step definitions (extends BaseTest for driver access)
  - Stores `currentUsername` and `currentPassword` state across steps for conditional assertions
  - Steps use `@Given`, `@And`, `@When`, `@Then` annotations matching `.feature` file text

## Critical Developer Workflows

### Running Tests
```bash
# Run all tests
mvn clean test

# Run specific feature
mvn test -Dtest=TestRunner

# View reports
open target/report-cucumber.html
```

### Build Configuration
- **Maven Compiler**: Java 17
- **Key Dependencies**:
  - Selenium 4.34.0 (WebDriver for Chrome)
  - Cucumber 7.15.0 (BDD framework)
  - TestNG 7.8.0 (test runner)
  - SLF4J 2.0.13 (logging)

## Project-Specific Conventions & Patterns

### 1. **Feature File Naming & Organization**
Features located in `src/test/resources/features/`:
- `loginSuccess.feature` - Positive test case with exact credentials
- `loginFailed.feature` - Negative test cases (Scenario Outline with multiple parameter combinations)
- `checkbalance.feature`, `transfer.feature` - Other functional areas (currently scaffolded)
- **Tag Convention**: Features use lowercase tags (`@Login`, `@login`) for filtering

### 2. **Step Definition Parameter Handling**
```java
// Parameterized steps use {string} placeholders
@And("user click form and input username with {string}")
// Empty strings handled explicitly - null coercion to ""
String currentPassword = password == null ? "" : password;
// State stored as instance variables for cross-step assertions
```

### 3. **Validation vs Action Separation**
- **Validation Methods** (LoginPage): Check UI elements visible, use assertions
  - `validateLoginPage()` - checks all form elements present
  - `validateInlineAlertForEmptyField()` - conditional validation based on input state
  - `validateWrongCredentialAlert()` - wraps timeout exception into assertion failure
- **Action Methods** (LoginPage): Input data, click buttons, interact with driver
  - `inputUsername()`, `inputPassword()`, `clickLoginButton()`

### 4. **Error Handling Patterns**
- **NoSuchElementException** - caught and logged as false (used in `isElementDisplayed()`)
- **TimeoutException** - caught and converted to `Assert.fail()` (see `validateWrongCredentialAlert()`)
- **InterruptedException** - standard pattern: catch and re-interrupt thread

### 5. **Explicit Wait Pattern**
```java
// Standard pattern across LoginPage
wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).methodCall()
// Waits for element visibility BEFORE performing action
```

### 6. **Conditional Assertion Logic**
LoginSteps tracks input state to determine which assertion to run:
```java
if (currentUsername.isEmpty() || currentPassword.isEmpty()) {
    // Expect inline form validation alerts
    loginPage.validateInlineAlertForEmptyField(...)
} else {
    // Expect wrong credential push notification
    loginPage.validateWrongCredentialAlert()
}
```

## Integration Points & External Dependencies

### 1. **Browser Driver**
- Hard-wired to **ChromeDriver** via `new ChromeDriver(options)` in BaseTest
- Uses system ChromeDriver binary (no WebDriverManager - manual driver management required)
- Chrome options instantiated but empty (no special configurations)

### 2. **CMS Application API**
- Endpoint: https://cms-jip.folkatech.com
- Login form: username/password fields (`login_username`, `login_password`)
- Validation behavior: Inline alerts (empty field) + notification (wrong creds)

### 3. **Test Data**
Currently hard-coded in feature files:
- Valid: username="admin", password="password"
- Invalid examples in loginFailed.feature (combinations of wrong/empty values)

## File Structure & Key Paths

```
src/test/
├── java/AutomationWEBSITE_JIP/
│   ├── BaseTest.java           ← WebDriver lifecycle, static instance
│   ├── TestRunner.java         ← Cucumber entry point, report config
│   ├── CucumberHooks.java      ← Before/After hooks, screenshots
│   ├── page/
│   │   └── LoginPage.java      ← Page object, locators, methods
│   └── StepDefinitions/
│       └── LoginSteps.java     ← Gherkin step implementations
└── resources/
    ├── cucumber.properties     ← Currently empty
    └── features/               ← .feature files (Gherkin scenarios)
```

## Common Customization Points

1. **Adding new page objects**: Create `src/test/java/AutomationWEBSITE_JIP/page/NewPage.java` extending BaseTest
2. **Adding step definitions**: Add methods to `LoginSteps.java` or create new class in `StepDefinitions/` package
3. **New features**: Create `.feature` file in `features/` directory, ensure Gherkin steps match step definition methods
4. **Locator updates**: Modify `By` objects in page classes (use IDs when available, XPath as fallback)
5. **Wait timeouts**: Modify `Duration.ofSeconds(15)` in `BaseTest.setupDriver()`
6. **Application URL**: Change URL in `CucumberHooks.beforeAll()` method

## Known Limitations & Notes

- Single-threaded execution (static driver instance prevents parallel test runs)
- No test data externalization (parameters hard-coded in features)
- No WebDriverManager dependency - requires manual driver binary setup
- No cross-browser testing setup (Chrome only)
- Empty pause cycles (10s hardcoded) may slow test execution unnecessarily

