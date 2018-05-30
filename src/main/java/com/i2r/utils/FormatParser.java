package com.i2r.utils;

import java.util.List;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */
public class FormatParser {

    private String filePath;

    private List<String> filePathList;

    public FormatParser(String filePath) {
        this.filePath = filePath;
    }

    public FormatParser(List<String> filePathList) {
        this.filePathList = filePathList;
    }

}
