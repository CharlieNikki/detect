package com.example.detect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetectRecord {

    private Integer id;
    private String description;
    private String date;
    private Integer projectId;
    private String detectPersonName;
    private Object image;
    private BigDecimal longitude;
    private BigDecimal latitude;
}
