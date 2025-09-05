Feature: Search functionality of the e-commerce website

Background:
  Given the API base URL is set to '<base_url>'
  And the authorization header is '<auth_token>'
  And the content type is 'application/json'

Scenario: Search with valid product keyword
  Given the API endpoint '/search?query=sports+shoes'
  When I send a GET request
  Then the response status should be 200
  And the response body should contain a list of products
  And the response should include filtering and sorting options

Scenario: Search with no matching products
  Given the API endpoint '/search?query=unicorn+lamps'
  When I send a GET request
  Then the response status should be 200
  And the response body should contain a message "No matching products found"
  And the response body should include suggestions for related or popular products

Scenario: Search with a typo in the keyword
  Given the API endpoint '/search?query=sprts+shoes'
  When I send a GET request
  Then the response status should be 200
  And the response body should contain a message "Did you mean sports shoes?"
  And the response body should include a list of products matching the corrected keyword

Scenario: Search with special characters
  Given the API endpoint '/search?query=@#$%^'
  When I send a GET request
  Then the response status should be 200
  And the response body should contain a message "No matching products found" or similar
  And special characters should not break the system's functionality

Scenario: Search using multiple keywords
  Given the API endpoint '/search?query=running+shoes+blue'
  When I send a GET request
  Then the response status should be 200
  And the response body should contain a list of products that match all the entered keywords

Scenario: Search performance under load
  Given a performance testing tool simulating concurrent API hits
  When 1000 users simultaneously send GET requests to '/search?query=random_keywords'
  Then the average response time for all requests should be under 2 seconds
  And the response status for all requests should be 200

Scenario: Search accessibility compliance
  Given the testing environment is using screen readers (e.g., JAWS, NVDA)
  And all interaction is limited to keyboard inputs
  When the user performs a search using the API '/search?query=sports+shoes'
  Then the response status should be 200
  And the system should provide descriptive feedback for API responses through the screen reader

Scenario: Search bar responsiveness on different devices
  Given multiple devices such as desktop, mobile, and tablet
  When the API endpoint '/search?query=sports+shoes' is tested under different resolutions
  Then the response status should be 200 for all devices
  And the response body should contain a fully functional list of products with no missing or misaligned data
