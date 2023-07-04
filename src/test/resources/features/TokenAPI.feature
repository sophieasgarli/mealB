Feature: Token API

@TokenAPI @regression
  Scenario: Get token
    Given User creates request data with "mealbemp1" and "Test123!"
    And User submits request to TOKEN api
    When User validates if the status code is 200
    Then User validates response schema 