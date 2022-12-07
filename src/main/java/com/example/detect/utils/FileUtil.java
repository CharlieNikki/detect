package com.example.detect.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.example.detect.constant.Sign.RETURN_MESSAGE_FAIL;

public class FileUtil {

    /**
     * 文件下载
     */
    @SneakyThrows
    public static String fileDownload(MultipartFile file) {

        StringBuilder imagesPath = new StringBuilder();
        String newFileName;

        if (file != null) {
            // 获取文件后缀
            String suffixName = ImageUtil.getImagePath(file);
            // 生成新文件的名称(UUID+时间戳)
            newFileName = ImageUtil.getNewFileName(suffixName);
            // 保存文件
            File f = new File(ImageUtil.getNewImagePath(newFileName));
            // 将对象存入本地磁盘
            boolean state = ImageUtil.saveImage(file, f);
            // 存入本地磁盘成功
            if (state) {
                imagesPath.append(newFileName).append(",");
            } else {
                return RETURN_MESSAGE_FAIL;
            }
        }
        return String.valueOf(imagesPath.deleteCharAt(imagesPath.length() - 1));
    }
}
