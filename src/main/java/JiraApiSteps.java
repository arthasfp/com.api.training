
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requests.HttpRequestsBasicAuth;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static constants.HeaderConstants.AUTHORIZATION;
import static constants.HeaderConstants.AUTHORIZATION_TYPE;
import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

public class JiraApiSteps {
    private static final Logger logger = LoggerFactory.getLogger(JiraApiSteps.class);
    private static RequestSpecification requestSpecification;
    private HttpRequestsBasicAuth httpRequestsBasicAuth;
    private JiraProperties jiraProperties;

    public void setJiraProperties(JiraProperties jiraProperties) {
        this.jiraProperties = jiraProperties;
    }

    public JiraApiSteps() {
        httpRequestsBasicAuth = new HttpRequestsBasicAuth();
        jiraProperties = new JiraProperties();
    }

    private RequestSpecification getRequestSpecification() {
      String encoded = Base64.getEncoder().encodeToString((jiraProperties.getJiraUser() + ":" + jiraProperties.getJiraPassword()).getBytes(StandardCharsets.UTF_8));
        return requestSpecification = new RequestSpecBuilder()
                .addHeader(AUTHORIZATION, AUTHORIZATION_TYPE + encoded)
                .setContentType(ContentType.JSON)
                .setBaseUri(jiraProperties.getJiraHost())
                .setBasePath(jiraProperties.getApiPath())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Add jira issue label.
     * https://community.atlassian.com/t5/Jira-Software-questions/How-do-you-delete-a-label-using-the-REST-API/qaq-p/639469
     *
     * @param issueKey the issue key
     */
    public void addJiraIssueLabel(final String issueKey, final String label) {
        logger.info("Adding label '{}' to the Jira issue {}", label, issueKey);
        String res = "{\"update\": {\"labels\" : [{\"add\":\"" + label + "\"}]}}";
        String result = httpRequestsBasicAuth.doPutRequest(
                jiraProperties.getJiraHost() + jiraProperties.getApiPath() + jiraProperties.getIssuePath() +
                        issueKey, jiraProperties.getJiraUser(), jiraProperties.getJiraPassword(), res);
        logger.debug("Result of adding is: {}", result);
    }


    public String addComment(final String issueIdOrKey, final String res) {
        return given()
                .auth()
                .preemptive()
                .basic(jiraProperties.getJiraUser(),jiraProperties.getJiraPassword())
                .contentType(ContentType.JSON)
                .body(res)
                .log().all()
                .when()
                .post(jiraProperties.getJiraHost()+ jiraProperties.getApiPath()+ jiraProperties.getIssuePath() + issueIdOrKey + "/comment/")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .response()
                .prettyPrint();
    }


// Deserialization example

    public Example addIssueDetails(final String issueIdOrKey) {
        return given()
                .auth()
                .preemptive()
                .basic(jiraProperties.getJiraUser(),jiraProperties.getJiraPassword())
                .contentType(ContentType.JSON)
                .log().all()
                .get(jiraProperties.getJiraHost()+ jiraProperties.getApiPath()+ jiraProperties.getIssuePath() + issueIdOrKey )
             .as(Example.class);
//                .then()
//                .log().ifValidationFails()
//                .statusCode(HttpURLConnection.HTTP_OK)
//                .extract()
//                .response()
//                .prettyPrint();
    }


    public void addCommentExpect(final String issueIdOrKey, final String res) {
                given(getRequestSpecification())
                .body(res)
                .log().all()
                .expect()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .when()
                .post(jiraProperties.getIssuePath() + issueIdOrKey + "/comment/");

    }


    // Multipart example

    public void addAttachment(File file, String issueIdOrKey) {
        given(getRequestSpecification())
                .header("X-Atlassian-Token", "no-check")
                .contentType("multipart/form-data")
                .multiPart("file", file, "text/html")
                .config(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE))).contentType("multipart/form-data; charset=UTF-8")
                .post(jiraProperties.getIssuePath() + issueIdOrKey + "/attachments/")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .response()
                .prettyPrint();
    }

    // With RequestSpecification example

    public void getJiraIssue(String issueIdOrKey){
        given(getRequestSpecification())
                .get(jiraProperties.getIssuePath() +  issueIdOrKey)
                .then()
                .statusCode(HttpURLConnection.HTTP_OK);
    }

    // Expect style example

    public Response getJiraIssueEx(String issueIdOrKey) {
        return given()
                .auth()
                .preemptive()
                .basic(jiraProperties.getJiraUser(), jiraProperties.getJiraPassword())
                .expect()
                .statusCode(HttpURLConnection.HTTP_OK)
                .when()
                .get(jiraProperties.getIssuePath() + issueIdOrKey);
    }

}