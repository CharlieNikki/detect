package com.example.detect.utils;

import lombok.Cleanup;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileUtil {

    /**
     * 文件下载
     */
    @SneakyThrows
    public static void downloadFile(HttpServletResponse response, String imagePath) {

        if (imagePath == null) {
            System.out.println("图片路径为空！");
            return;
        }
        String[] strArr = imagePath.split(",");
        for (String imgPath : strArr) {
            File file = new File(imgPath);
            if (!file.exists()) {
                return;
            }
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline,imgPath=" + imgPath);
            byte[] buff = new byte[1024];
            @Cleanup BufferedInputStream in = null;
            OutputStream out = null;
            try {
                out = response.getOutputStream();
                in = new BufferedInputStream(Files.newInputStream(new File(imgPath).toPath()));
                int i = in.read(buff);
                while (i != -1) {
                    out.write(buff, 0, buff.length);
                    out.flush();
                    i = in.read(buff);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
