package com.example.detect.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ImageUtil {

    // 存放图片的绝对路径(linux)
    public static final String SAVE_IMAGE_PATH = "/usr/local/detect/images/";
    //public static final String SAVE_IMAGE_PATH = "E:/images/";

    /**
     * 返回文件后缀
     */
    public static String getImagePath(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int index = fileName.indexOf(".");
        return fileName.substring(index, fileName.length());
    }

    /**
     * 保存图片
     */
    @SneakyThrows
    public static boolean saveImage(MultipartFile multipartFile, File file) {

        // 查看文件是否存在，不存在则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            // 使用此方法必须要绝对路径且文件夹必须已存在，否则会报错
            multipartFile.transferTo(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 新文件名
     */
    public static String getNewFileName(String suffix) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        return date + UUID.randomUUID().toString().replaceAll("-","") +  suffix;
    }

    /**
     * 返回图片保存地址
     */
    public static String getNewImagePath(String name) {
        return SAVE_IMAGE_PATH + name;
    }
}
