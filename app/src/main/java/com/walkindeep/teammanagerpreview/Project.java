package com.walkindeep.teammanagerpreview;

/**
 * Created by jiahao on 2016-05-05.
 */
public class Project {
    private String name;
    private String identifier;
    private String description;
    private String tracker_ids;
    private String parent_id;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getTracker_ids() {
        return tracker_ids;
    }

    public void setTracker_ids(String tracker_ids) {
        this.tracker_ids = tracker_ids;
    }
}
