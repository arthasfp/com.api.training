import utils.PropertiesReader;

import java.util.Properties;

public class JiraProperties {
    private String projectKey;
    private String jiraHost;
    private String apiPath;
    private String issuePath;
    private String jiraUser;
    private String jiraPassword;

    public JiraProperties() {
        String resourcePath = "CICD1config.properties";
        if(null != System.getProperty("mcsmTestEnvironment")){
            resourcePath = System.getProperty("mcsmTestEnvironment") + "config.properties";
        }
        PropertiesReader propertiesReader = new PropertiesReader();
        Properties properties = propertiesReader.getPropertiesByPath(resourcePath);
        this.jiraUser = propertiesReader.resolvePropertyValue(properties, "jiraUser", "jira.user");
        this.jiraPassword = propertiesReader.resolvePropertyValue(properties, "jiraPassword", "jira.password");
        this.jiraHost = propertiesReader.resolvePropertyValue(properties, "jiraHost", "jira.host");
        this.projectKey = propertiesReader.resolvePropertyValue(properties, "jiraProjectKey", "jira.project.key");
        this.apiPath = properties.getProperty("jira.api.path");
        this.issuePath = properties.getProperty("jira.issue.path");
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getJiraHost() {
        return jiraHost;
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getIssuePath() {
        return issuePath;
    }

    public String getJiraUser() {
        return jiraUser;
    }

    public String getJiraPassword() {
        return jiraPassword;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public void setJiraUser(String jiraUser) {
        this.jiraUser = jiraUser;
    }

    public void setJiraPassword(String jiraPassword) {
        this.jiraPassword = jiraPassword;
    }
}
