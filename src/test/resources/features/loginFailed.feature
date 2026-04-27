@login
Feature: Login - Negative Case

  Scenario Outline: login with invalid input
    Given user is launch the website
    And user click form and input username with "<username>"
    And user click form and input password with "<password>"
    When user click login button
    Then user get login error based on input condition

    Examples:
      | username        | password          |
      | adminsuper      | password12345     |
      | admin           | wrongpassword     |
      | wrongadmin      | password          |
      | superadmin2     |                   |
      |                 | password          |


