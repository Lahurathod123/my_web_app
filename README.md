**Automation Test Plan for MTU Quota Enforcement – `POST /v1/files` API (End-to-End Test)**

---

## 1. **Test Objective**
To validate and automate one comprehensive end-to-end test scenario for the `POST /v1/files` API that ensures MTU (Monthly Transfer Unit) quota enforcement is functioning correctly — including quota calculation, upload behavior, error response, and system logging.

---

## 2. **Scope of the Test**
This test plan covers the scenario where a file is uploaded by a client who has already consumed most of their quota and attempts to upload a file that exceeds the remaining limit.

---

## 3. **Test Scenario: Quota Exceeded Upload**

| Test Case ID | TC_QUOTA_001 |
|--------------|--------------|
| **Title** | Upload file exceeding available quota |
| **API Endpoint** | `POST /v1/files` |
| **Preconditions** | Client is authenticated and has only 10MB quota remaining |
| **Input** | Upload a valid file of size 15MB with valid auth headers |
| **Expected Status Code** | `429 Too Many Requests` |
| **Expected Error Code** | `QUOTA_EXCEEDED` |
| **Expected Message** | `"Exceeded the daily usage limit. Further access is blocked until reset time"` |
| **Postconditions** | File is not stored or processed. Quota remains unchanged. |

---

## 4. **Test Steps**

1. **Setup Test Client:**
   - Configure test client with 100MB quota.
   - Simulate 90MB usage (manually via DB/admin API).

2. **Prepare Payload:**
   - Generate a valid 15MB file (e.g., PDF).

3. **Execute API Call:**
   - Make `POST /v1/files` with above payload and client token.

4. **Validate Response:**
   - Assert HTTP Status = 429
   - Assert body contains:
     ```json
     {
       "error": {
         "code": "QUOTA_EXCEEDED",
         "message": "Exceeded the daily usage limit. Further access is blocked until reset time"
       }
     }
     ```

5. **Log Verification (Optional):**
   - Verify log entry includes quota rejection with client ID.

---

## 5. **Automation Design**

- **Language:** Java
- **Frameworks:** Cucumber + REST-assured
- **Tag:** `@Quota @ExceedLimit`

### Sample Feature Snippet
```gherkin
Feature: MTU Quota Enforcement on Upload

  @Quota @ExceedLimit
  Scenario: Upload file exceeding remaining quota
    Given client has only 10MB quota left
    When client uploads a file of size 15MB
    Then the response status should be 429
    And the error message should be "QUOTA_EXCEEDED"
```

---

## 6. **Test Data**

- **Client ID:** `test-client-overquota`
- **Auth Token:** Stored securely in properties file
- **File:** `sample-large-file.pdf` (15MB)

---

## 7. **Execution Flow**

- Executed via `mvn test -Dcucumber.options="@Quota"`
- Can be triggered from CI/CD tool (GitHub Actions, Jenkins)

---

## 8. **Reporting & Logs**

- **Reports:** Cucumber HTML and/or Allure
- **Logs:** Stored under `/logs/mtu-post/`

---

## 9. **Dependencies**
- Java 17+
- REST-assured
- Cucumber JVM
- Maven
- Postman (for manual comparison if needed)

---

## 10. **Risks & Assumptions**

| Risk | Mitigation |
|------|------------|
| Quota not reset between runs | Use isolated clients or reset via admin API |
| Auth token expiration | Use token provider or refresh mechanism |

---

## 11. **Conclusion**
This single test case demonstrates complete enforcement of quota logic in a realistic scenario and is ready for client walkthrough/demo. It includes authentication, quota validation, error handling, and proper automation support using modern Java tooling.

✅ Ready for review and integration into broader regression suite.
