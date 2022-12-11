package com.example.detect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Image {

    private String id;// 图片id
    private String imageName;// 图片名称
    private String projectId;// 工程id
    private String date;// 图片存入日期

    public void setImage(String id, String projectId, String imageName, String date) {
        this.id = id;
        this.projectId = projectId;
        this.imageName = imageName;
        this.date = date;
    }
}
