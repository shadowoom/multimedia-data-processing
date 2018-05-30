package com.i2r.Common;

/**
 * multimedia-data-processing
 * com.i2r.Common
 * Created by Zhang Chen
 * 5/30/2018
 */
public enum Language {

    ENGLISH(1, "english"),
    CHINESE(2, "chinese"),
    MIXTURE(3, "mixture");

    private final int code;
    private final String language;

    Language(int code, String language) {
        this.code = code;
        this.language = language;
    }

    public int getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public static String getLanguage(int code) {
        switch (code) {
            case 1:
                return ENGLISH.getLanguage();
            case 2:
                return CHINESE.getLanguage();
            case 3:
                return MIXTURE.getLanguage();
                default:
                    return ENGLISH.getLanguage();
        }
    }

}
