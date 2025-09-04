Feature: Validate server behavior for Java socket programming scenarios
  Background:
    Given the API base URL 'http://localhost:8080'
    And the socket server is configured to listen on port 8080
    And the server supports multiple client connections
    And the content type is 'application/json'

  Scenario: ServerSocket Initialization
    Given the server configuration specifies port 8080
    When I start the server
    Then the server should initialize a ServerSocket successfully
    And the server should start listening on port 8080
    And the server should not throw any errors during initialization

  Scenario: Handle Multiple Client Connections
    Given the server is running and listening on port 8080
    When I start 5 client instances and connect them to the server
    Then all clients should successfully establish connections
    And each client should independently send and receive messages
    And no conflicts between clients should occur

  Scenario: Broadcast Messages to All Clients
    Given the server has at least 3 connected clients
    When Client A sends a message with content 'Hello from A'
    Then all connected clients should receive 'Hello from A'
    And Client A should optionally receive an echo of its own message

  Scenario: Independent Client Communication
    Given the server has 5 connected clients
    When Client A sends a message 'Message from A'
    And Client B sends a different message 'Message from B'
    Then the server should independently handle each message without interference
    And each client should receive the respective messages with no delay

  Scenario: Handle Client Disconnection
    Given the server has 2 connected clients
    When one client disconnects from the server
    Then the server should clean up resources for the disconnected client
    And the remaining client should continue functioning without interruptions

  Scenario: Client Connection Timeout Handling
    Given the server has at least 1 connected client
    When a client remains inactive for 30 seconds
    Then the server should not crash or hang
    And it should continue accepting new client connections

  Scenario: Performance Under Load
    Given the server is operational in a performance test environment
    When 100 clients connect simultaneously to the server
    Then the server should maintain acceptable response times
    And resource usage (CPU, memory) should remain within defined thresholds

  Scenario: Resource Cleanup
    Given the server is running and accepting connections
    When a client connection abruptly disconnects due to network failure
    Then the server should clean up sockets and streams for the failed client
    And the server logs should confirm proper exception handling
    And other clients should remain unaffected

  Scenario: Scalability Test
    Given the server is deployed in a scalable test environment
    When clients incrementally connect to the server (10, 50, 100, 200 clients)
    Then the server should scale efficiently without significant performance degradation
    And the server should remain reliable until reaching maximum capacity
