**Automation Test Plan for MTU Quota Enforcement – `POST /v1/files` API (End-to-End Test)**

---

## 1. **Test Objective**
To validate the behavior of the `POST /v1/files` endpoint under MTU (Message Transfer Unit) quota limitations — particularly the error handling mechanism when the quota is exceeded.

---

## 2. **Scope of the Test**
This test covers a complete end-to-end flow for uploading a file when a client exceeds their allocated **daily** or **monthly** MTU quota. The test ensures the system returns an appropriate error response and avoids processing or storing the file.

---

## 3. **Test Scenario: Quota Exceeded Response Validation**

| Test Case ID | TC_QUOTA_001 |
|--------------|--------------|
| **Title** | Upload file exceeding daily MTU quota |
| **API Endpoint** | `POST /v1/files` |
| **Preconditions** | Client is authenticated. Daily quota is already fully consumed. |
| **Input** | Valid file upload request with appropriate headers |
| **Expected Status Code** | `429 Too Many Requests` |
| **Expected Error Code** | `QUOTA_EXCEEDED` |
| **Expected Message** | "Exceeded the daily usage limit. Further access is blocked until reset time" |
| **Postconditions** | File upload is rejected, and no changes occur in usage data. |

---

## 4. **Test Steps**

1. **Set Up Test Environment:**
   - Create a test client (e.g., `test-client-quota-full`).
   - Set quota values:
     - Daily limit: `300000 MTUs`
     - Monthly limit: `1000000 MTUs`
   - Simulate usage:
     - Set `currentUsage` to `300001` for daily (already exceeded).

2. **Prepare Request:**
   - Construct a `POST /v1/files` request with required file data.
   - Include valid Authorization token and headers.

3. **Trigger API Call:**
   - Execute the POST request.

4. **Validate Response:**
   - HTTP Status Code = 429
   - Response body should match:
     ```json
     {
       "error": {
         "code": "QUOTA_EXCEEDED",
         "message": "Exceeded the daily usage limit. Further access is blocked until reset time",
         "details": {
           "clientName": "CLC",
           "quotaType": "daily",
           "currentUsage": 300001,
           "limit": 300000,
           "nextResetTime": "2025-01-16T00:00:00Z"
         }
       }
     }
     ```

5. **Postconditions:**
   - Ensure file is not stored or processed.
   - Quota values remain unchanged.

---

## 5. **Automation Design**

- **Language:** Java
- **Framework:** Cucumber with REST-assured
- **Tag:** `@QuotaExceeded`

### Sample Feature File Snippet
```gherkin
Feature: MTU Quota Enforcement

  @QuotaExceeded
  Scenario: File upload when daily quota is already exceeded
    Given the client "CLC" has exceeded the daily MTU quota
    When the client attempts to upload a file
    Then the response status should be 429
    And the error code should be "QUOTA_EXCEEDED"
    And the message should state "Exceeded the daily usage limit. Further access is blocked until reset time"
```

---

## 6. **Test Data**

| Parameter        | Value                         |
|------------------|-------------------------------|
| Client ID        | CLC                           |
| Daily Limit      | 300000                        |
| Current Usage    | 300001                        |
| Monthly Limit    | 1000000                       |
| Token            | Stored in secure config       |
| File             | `example.pdf` (any valid file) |

---

## 7. **Execution Flow**

- Configure usage quota directly in MongoDB or via setup script.
- Run automation via Maven:
  ```bash
  mvn test -Dcucumber.filter.tags=@QuotaExceeded
  ```
- Results will be validated via assertions.

---

## 8. **Reports and Logs**

- **Report Tool:** Allure / Cucumber HTML
- **Logs:** Stored at `/logs/quota-tests/` for debugging

---

## 9. **Dependencies**

- Java 17+
- REST-assured
- Cucumber JVM
- Maven
- Access to MongoDB or Admin API to configure quota

---

## 10. **Assumptions & Risks**

| Assumption | Risk if Broken |
|------------|----------------|
| Daily/Monthly quota accurately enforced | False test results or skipped quota rejection |
| Client token is active | Authentication failure |
| System returns expected 429 payload format | Assertion mismatch |

---

## 11. **Conclusion**
This test ensures that the `POST /v1/files` API correctly enforces **daily quota** restrictions and returns a structured 429 error response when usage exceeds limits. The scenario is aligned with real quota behavior and is ready for client review and demo.

✅ Recommended for inclusion in regression pipeline and API compliance tests.
