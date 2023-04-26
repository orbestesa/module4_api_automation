@regression @acceptance @boards
Feature: Boards
  Background:
    Given I have a valid key and token

  Scenario: Gets Boards
    When I sen a request to GET boards endpoint "members/me/boards"
    Then The response status code should be 200
    And The response should be contain "boards" field

  @createBoard @deleteBoard
  Scenario: Gets a board
    When I send a request to GET a board endpoint "boards/{id}"
    Then The response status code should be 200
    And The response should have a field "name" with value "{name}"

  @deleteBoard
  Scenario: Creates a board
    When I send a request to POST a boards endpoint "boards" with name "example-test2023-post"
    Then The response status code should be 200
    And The response should be contain "comments" field
    And The response should have a field "name" with value "example-test2023-post"

  @createBoard @deleteBoard
  Scenario: Update a board
    When I sen a request to PUT a boards endpoint "boards/{id}" with name "example-test2023-put"
    Then The response status code should be 200
    And The response should be contain "prefs" field
    And The response should have a field "name" with value "example-test2023-put"

  @createBoard
  Scenario: Delete a board
    When I send a request to DELETE a boards endpoint "boards/{id}"
    Then The response status code should be 200
    And The response should be contain "{\"_value\":null}" field
