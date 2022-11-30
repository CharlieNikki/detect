package com.example.detect.utils;

import com.mysql.jdbc.StringUtils;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeProjectDir {

    private String filePath = "src/main/resources/static/upload-dir";
    private String realPath = "src/main/resources/static/upload-dir";

    private Path rootLocation;

    /**
     * 创建上传图片的文件夹
     */
    @SneakyThrows
    public void MkProjectDir(String projectId) {

        if (projectId.trim().equals("")) {
            throw new Exception("上传的文件不能为空");
        }
        if (realPath.contains(projectId)) {
            realPath = realPath;
        } else {
            realPath = filePath + "/" + projectId;
        }
        rootLocation = Paths.get(realPath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
