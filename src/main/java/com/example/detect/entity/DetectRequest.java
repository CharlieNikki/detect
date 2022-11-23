package com.example.detect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetectRequest {

    private Integer id;
    private String projectName;
    private String leaderName;
    private String leaderPhone;
    private String projectDetail;
    private String projectCompany;
    private String location;
    private String remark;
    private MultipartFile image;
    private Integer dataStatus;
    private String detectRequestDate;
}
