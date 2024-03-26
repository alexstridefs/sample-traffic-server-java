# TestAgentApplication API Documentation

Welcome to the API documentation for the TestAgentAppication. This service allows you to configure a server to send traffic to a specified URL at a configurable rate. You can start, stop, and modify this traffic through our RESTful API.

## Base URL

The base URL for the API is:

http://localhost:8080

Replace `localhost` with your server's hostname or IP address when deploying the service.

## Endpoints

### POST /config

Updates the configuration for sending traffic and starts sending traffic to the specified URL at the specified rate.

#### Request Body

- `url` (string): The URL to which the server will send traffic.
- `callsPerSecond` (integer): The rate at which traffic will be sent, in requests per second.

##### Example

```json
{
  "url": "https://example.com/api/target",
  "callsPerSecond": 1
}
```

##### Responses

200 OK: Configuration updated successfully.

### GET /config

Retrieves the current configuration.

##### Responses

200 OK: Successfully retrieved the configuration. The response body contains the current configuration.
404 Not Found: No configuration found.

Example Response
```
{
"url": "https://example.com/api/target",
"callsPerSecond": 1
}
```

### DELETE /config
Stops sending traffic and clears the current configuration.

##### Responses
200 OK: Traffic sending stopped, and configuration cleared.
Error Handling

400 Bad Request: The server could not understand the request due to invalid syntax.
404 Not Found: The requested resource was not found.
500 Internal Server Error: The server encountered an unexpected condition.

# Sample cURL requests

Start traffic:

```
curl -X POST -H "Content-Type: application/json" http://localhost:8080/config -d '{"url": "http://whatever/foo", "callsPerSecond": 1}'
```