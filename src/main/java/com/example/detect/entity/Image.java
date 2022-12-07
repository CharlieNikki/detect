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

    private Integer id;// 图片id
    private String imageName;// 图片名称
    private Integer projectId;// 工程id
    private String date;// 图片存入日期

    public void setImage(Integer projectId, String imageName, String date) {
        this.projectId = projectId;
        this.imageName = imageName;
        this.date = date;
    }
}
