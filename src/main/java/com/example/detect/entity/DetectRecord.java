package com.example.detect.entity;

import java.sql.Blob;

public class DetectRecord {

    private Integer id;
    private String description;
    private String date;
    private Integer projectId;
    private Integer detectPersonId;
    private Blob image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getDetectPersonId() {
        return detectPersonId;
    }

    public void setDetectPersonId(Integer detectPersonId) {
        this.detectPersonId = detectPersonId;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
