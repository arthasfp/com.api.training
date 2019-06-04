import org.testng.annotations.Test;

import java.io.File;

public class Examples {
    JiraApiSteps jiraApiSteps = new JiraApiSteps();

    @Test
    public void userAddsCommentToJira() {
        String res = "{\"body\": \"note\"}";
        jiraApiSteps.addComment("104958", res);
    }

    @Test
    public void userAddsAttachmentJira() {
        String pathToFile ="D:\\comglobalapitraining\\src\\main\\resources\\attachment.txt";
        jiraApiSteps.addAttachment(new File(pathToFile), "104958");
    }

    @Test
    public void getInstanceWithJiraIssueDetails() {
        Example issueDetails = jiraApiSteps.addIssueDetails("104958");
    }


    @Test
    public void getJiraIssue() {
        jiraApiSteps.getJiraIssue("104958");
    }
}
