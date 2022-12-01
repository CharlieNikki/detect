package com.example.detect.utils;

import lombok.SneakyThrows;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_RELATIVE_PATH;

public class SplitByComma {
    /**
     * 用逗号隔开字符串，形成字符串数组
     * @param images
     * @return
     */
    @SneakyThrows
    public static List<String> splitStringByComma(String images) {

        String[] split = images.split(",");
        for (int i = 0; i < split.length; i++) {

            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            split[i] = "http://8.136.84.248:8083" + SAVE_IMAGE_RELATIVE_PATH + split[i];
            //split[i] = "http://localhost:8083" + SAVE_IMAGE_RELATIVE_PATH + split[i];
        }
        return new ArrayList<>(Arrays.asList(split));
    }
}
