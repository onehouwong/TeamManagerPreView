package com.walkindeep.teammanagerpreview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiahao on 2016-05-05.
 */
public class Issue {
    private static List<Issue> issueList = new ArrayList<>();
    private String projectId;
    private String trackerId;
    private String status_name;
    private String assigned_to_id;
    private String priority_id;
    private String description;
    private String subject;
    private String parent_issue_id;
    private String watcher_user_ids;
    private int position;//在cardlist中的位置

    public Issue(String subject, String description) {
        this.subject = subject;
        this.description = description;
    }

    public static List<Issue> getIssueList() {
        return issueList;
    }

    public static Issue getIssue(int position) {
        for (int i = 0; i < issueList.size(); i++) {
            Issue issue = issueList.get(i);
            if (issue.position == position) {
                return issue;
            }
        }
        return null;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getWatcher_user_ids() {
        return watcher_user_ids;
    }

    public void setWatcher_user_ids(String watcher_user_ids) {
        this.watcher_user_ids = watcher_user_ids;
    }

    public String getParent_issue_id() {
        return parent_issue_id;
    }

    public void setParent_issue_id(String parent_issue_id) {
        this.parent_issue_id = parent_issue_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(String priority_id) {
        this.priority_id = priority_id;
    }

    public String getAssigned_to_id() {
        return assigned_to_id;
    }

    public void setAssigned_to_id(String assigned_to_id) {
        this.assigned_to_id = assigned_to_id;
    }

    /*推送到服务端*/
    public void push() {

    }
}
