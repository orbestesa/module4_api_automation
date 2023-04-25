Feature: Boards
  Scenario: Gets Boards
    When I sen a request to GET boards endpoint "members/me/boards"
    Then The response status code should be 200
    And The response should be contain "boards" field
    And The response should have a field "name" with value "AT01"