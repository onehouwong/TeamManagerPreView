package com.walkindeep.teammanagerpreview;

/**
 * Created by jiahao on 2016-05-05.
 */

/**
 * 项目类
 */
public class Project {
    /**
     * 项目名称
     */
    private String name;
    private String identifier;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 跟踪者id
     */
    private String tracker_ids;
    /**
     * 父项目id
     */
    private String parent_id;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return 项目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置项目名称
     *
     * @param name 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return 项目描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置项目描述
     *
     * @param description 项目描述
     */
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
