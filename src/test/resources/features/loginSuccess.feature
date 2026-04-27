@Login
Feature: Login with valid credentials
  Scenario: Login with valid credentials
    Given user is launch the website
    And user click form and input username with "superadmin2"
    And user click form and input password with "password"
    When user click login button
    Then user is on CMS dashboard
