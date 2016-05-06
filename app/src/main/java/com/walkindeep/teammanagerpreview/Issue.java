package com.walkindeep.teammanagerpreview;

/**
 * Created by jiahao on 2016-05-05.
 */
public class Issue {
    private int projectId;
    private int trackerId;
    private String status_id;
    private int assigned_to_id;
    private String priority_id;
    private String description;
    private String subject;
    private String parent_issue_id;
    private int watcher_user_ids;

    public Issue(String subject, String description) {
        this.subject = subject;
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public int getWatcher_user_ids() {
        return watcher_user_ids;
    }

    public void setWatcher_user_ids(int watcher_user_ids) {
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

    public int getAssigned_to_id() {
        return assigned_to_id;
    }

    public void setAssigned_to_id(int assigned_to_id) {
        this.assigned_to_id = assigned_to_id;
    }
}