package com.i2r.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class VideoToAudio {

    private static final String ffmpegPath = "../ffmpeg/bin/ffmpeg";

    public static void convertVideoToAudio(String videoPath) {
        log.info("Convert video: {}" , videoPath);
        String newWavPath = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.lastIndexOf(".")) + ".wav";
        String command = ffmpegPath + " -i "+ videoPath + " " +
                videoPath.substring(0, videoPath.lastIndexOf("/") + 1) + newWavPath;
        log.info("Conversion command: {}" , command);
        try {
            Runtime rt = Runtime.getRuntime();
            log.info("Start conversion process");
            Process pr = rt.exec(command);
            log.info("Conversion completed successfully, find new wav file at {}", newWavPath);
        } catch (IOException e) {
            log.error("Conversion error: {}", e);
        }
    }
}
