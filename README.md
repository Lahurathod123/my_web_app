**Automation Test Plan for MTU Quota Enforcement – `POST /v1/files` API (End-to-End Test)**

---

## 1. **Test Objective**
To validate and automate one comprehensive end-to-end test scenario for the `POST /v1/files` API that ensures MTU (Monthly Transfer Unit) quota enforcement is functioning correctly — including quota calculation, upload behavior, error response, and system logging.

---

## 2. **Scope of the Test**
This test plan covers the scenario where a file is uploaded by a client who has already consumed most of their quota (measured in MTUs) and attempts to upload a file that exceeds the remaining daily and monthly quota limits.

---

## 3. **Test Scenario: Quota Exceeded Upload**

| Test Case ID | TC_QUOTA_001 |
|--------------|--------------|
| **Title** | Upload file exceeding available MTU quota (daily + monthly) |
| **API Endpoint** | `POST /v1/files` |
| **Preconditions** | Client is authenticated and has only 10 MTUs remaining from daily and monthly quota |
| **Input** | Upload a valid file that consumes 15 MTUs with valid auth headers |
| **Expected Status Code** | `429 Too Many Requests` |
| **Expected Error Code** | `QUOTA_EXCEEDED` |
| **Expected Message** | "Exceeded the daily usage limit. Further access is blocked until reset time" |
| **Postconditions** | File is not stored or processed. Quota remains unchanged. |

---

## 4. **Test Steps**

1. **Setup Test Client:**
   - Configure test client with daily quota: 100 MTUs, monthly quota: 1000 MTUs.
   - Simulate usage: 90 MTUs for daily and 990 MTUs for monthly quota via DB/admin API.

2. **Prepare Payload:**
   - Generate a valid file consuming 15 MTUs (calculated based on size or processing cost).

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
   - Verify backend log entry includes quota rejection with client ID and attempted MTUs.

---

## 5. **Automation Design**

- **Language:** Java
- **Frameworks:** Cucumber + REST-assured
- **Tags:** `@Quota @ExceedLimit`

### Sample Feature Snippet
```gherkin
Feature: MTU Quota Enforcement on Upload

  @Quota @ExceedLimit
  Scenario: Upload file exceeding remaining quota
    Given client has only 10 MTUs left in daily and monthly quota
    When client uploads a file consuming 15 MTUs
    Then the response status should be 429
    And the error message should be "QUOTA_EXCEEDED"
```

---

## 6. **Test Data**

- **Client ID:** `test-client-overquota`
- **Auth Token:** Stored securely in properties file or secrets vault
- **File:** `sample-overquota.pdf` (consumes 15 MTUs)
- **Quota Usage Simulation:** Apply quota via DB setup script or admin API

---

## 7. **Execution Flow**

- Executed via `mvn test -Dcucumber.options="@Quota"`
- Can be triggered from CI/CD (GitHub Actions, Jenkins)
- Uses mock data for quota simulation

---

## 8. **Reporting & Logs**

- **Reports:** Cucumber HTML and/or Allure
- **Logs:** Stored under `/logs/quota-post/`

---

## 9. **Dependencies**

- Java 17+
- REST-assured
- Cucumber JVM
- Maven
- Postman (for manual verification)
- Test DB access or admin API for quota injection

---

## 10. **Risks & Assumptions**

| Risk | Mitigation |
|------|------------|
| Quota not reset between runs | Use isolated clients or reset via admin API |
| Auth token expiration | Use token provider or refresh mechanism |
| Quota consumption logic change | Sync with backend team during API change |

---

## 11. **Conclusion**
This single test case demonstrates enforcement of both **daily and monthly MTU quota logic** for a file upload scenario. It is suitable for client demonstration and automation review and includes authentication, quota handling, error validation, logging, and test coverage readiness.

✅ Ready for stakeholder review and regression suite integration.
