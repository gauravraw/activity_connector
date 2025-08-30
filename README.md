# Activity Connector

## Overview
The **Activity Connector** is a Spring Boot application that interacts with the GitHub API to fetch repository and commit details for a given user. It also implements rate limiting using Bucket4j to control the number of requests allowed per minute.

---

## Endpoints

### 1. **Fetch Repositories and Commits**
- **URL**: `/activity-connector/v1/github`
- **Method**: `GET`
- **Description**: Fetches repositories and their associated commits for a given GitHub user.
- **Query Parameters**:
  - `userName` (required): The GitHub username.
  - `pageNumber` (optional): The page number for paginated results (default: 0).
  - `pageSize` (optional): The number of repositories per page (default: 10).
- **Response**:
  - Returns a list of repositories with their details and associated commits.
- **Example**:

- ---

## Rate Limiting

### Details
The application uses **Bucket4j** to enforce rate limiting on the API.

- **Limit**: 10 requests per minute.
- **Response Code**: `429 Too Many Requests` when the rate limit is exceeded.
- **Error Message**: `Rate limit exceeded. Try again later.`

### Rate limiting
  requests-per-minute: 10

How to Run
Clone the repository.
Configure the application.yml file with the required GitHub API details.
Build and run the application:
mvn spring-boot:run
Access the API at http://localhost:8083/activity-connector.
<hr></hr>
