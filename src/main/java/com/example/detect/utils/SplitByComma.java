package com.example.detect.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitByComma {

    public static List<String> splitStringByComma(String images) {

        String[] split = images.split(",");

        return new ArrayList<>(Arrays.asList(split));
    }
}
