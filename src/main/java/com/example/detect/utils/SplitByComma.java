package com.example.detect.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_RELATIVE_PATH;

public class SplitByComma {

    @Value("${path.url-prefix}")
    public String SAVE_IMAGE_PREFIX;

    //public static String SAVE_IMAGE_PREFIX = "/usr/local/detect/images/";

    /**
     * 用逗号隔开字符串，形成字符串数组
     * @param images
     * @return
     */
    @SneakyThrows
    public List<String> splitStringByComma(String images) {

        String[] split = images.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = SAVE_IMAGE_PREFIX + SAVE_IMAGE_RELATIVE_PATH + split[i];
        }
        return new ArrayList<>(Arrays.asList(split));
    }
}
