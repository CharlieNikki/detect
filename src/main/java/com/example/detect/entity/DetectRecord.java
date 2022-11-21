package com.example.detect.entity;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

public class DetectRecord {

    private Integer id;
    private String description;
    private String date;
    private Integer projectId;
    private Integer detectPersonId;

    @Override
    public String toString() {
        return "DetectRecord{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", projectId=" + projectId +
                ", detectPersonId=" + detectPersonId +
                ", image=" + image +
                '}';
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    private Object image;

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
}
