Feature: API Login System Functional and Non-functional Testing
  Background:
    Given the API base URL is 'http://api.example.com/login'
    And the authorization header is set
    And the content type is 'application/json'

  Scenario: AI Failure Retry Mechanism on Login
    Given the login endpoint '/login' allows AI-related failure simulation
    When I send a POST request with username 'testUser' and password 'testPassword', simulating an AI-related failure
    Then the retry mechanism should be triggered for a second attempt
    When I simulate another AI-related failure on the second attempt
    Then the retry mechanism should be triggered for a third attempt
    When the third attempt is successfully made
    Then the response status should be 200
    And the response body should indicate successful login

  Scenario: No Retry on Invalid Credentials
    Given the login endpoint '/login' accepts username and password
    When I send a POST request with invalid username 'wrongUser' and invalid password 'wrongPassword'
    Then the response status should be 401
    And the response body should include 'Invalid credentials'
    And the retry mechanism should not be triggered

  Scenario: AI Error Detection Performance
    Given the login endpoint '/login' allows AI-related failure simulation
    When I send a POST request with username 'testUser' and password 'testPassword' simulating an AI-related failure
    Then the system should classify the issue as AI-related within 1 second
    And the response body should indicate retry due to AI-related failure

  Scenario: Login Retry Stress Test
    Given the login endpoint '/login' allows AI-related failure simulation for multiple clients
    When 100 concurrent clients send login requests with simulated AI-related failures
    Then the retry mechanism should be triggered for each client
    And the system resource usage should remain within acceptable thresholds
    And each client should successfully log in after retries

  Scenario: Distinct Error Messages for AI Failures and Credentials Failures
    Given the login endpoint '/login' differentiates between AI-related and credential-related failures
    When I send a POST request with username 'testUser' and password 'testPassword' simulating an AI-related failure
    Then the response body should include 'Login failed due to technical issues. Retrying...'
    When I send a POST request with invalid username 'wrongUser' and invalid password 'wrongPassword'
    Then the response body should include 'Invalid username or password. Please try again.'

  Scenario: System Resilience During Login Retries
    Given the login endpoint '/login' allows AI-related failure simulation
    When multiple users send concurrent login requests with up to three simulated AI-related failures each
    Then the system should remain stable and responsive
    And the retry mechanism should complete successfully for all users without crashing

  Scenario: Retry Limit Enforcement
    Given the login endpoint '/login' enforces a retry limit of two additional attempts for AI-related failures
    When I send a POST request with username 'testUser' and password 'testPassword', simulating three consecutive AI-related failures
    Then the retry mechanism should stop after the third failure
    And the response body should include 'Retry limit reached'
    And the server logs should reflect the retry attempt limit was enforced

  Scenario: Proper Log Handling and Masking of Credentials
    Given login endpoint '/login' logs error details and retry attempts
    When I send a POST request with username 'testUser' and password 'testPassword'
    Then the server logs should mask sensitive information such as 'testPassword'
    When AI-related failures are simulated and retried
    Then all sensitive information in the logs should be replaced with '******'
