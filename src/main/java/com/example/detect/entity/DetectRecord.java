package com.example.detect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetectRecord {

    private Integer id;
    private String description;
    private String date;
    private Integer projectId;
    private Integer detectPersonId;
    private String image;
}
