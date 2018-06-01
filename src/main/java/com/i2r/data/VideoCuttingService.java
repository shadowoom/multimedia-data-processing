package com.i2r.data;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * multimedia-data-processing
 * com.i2r.data
 * Created by Zhang Chen
 * 6/1/2018
 */

@Slf4j
public class VideoCuttingService implements Runnable {

    private String startTime;

    private String endTime;

    private String inputFilePath;

    private String outputFilePath;

    private static final String ffmpegPath = "../ffmpeg/bin/ffmpeg";

    @Override
    public void run(){
        String videoGenerationCommand = ffmpegPath + " -i " + inputFilePath + " -ss " + startTime
                + " -to " + endTime + " -c:v libx264 -c:a aac -an " + outputFilePath;
        log.info("generate audio file command: {}", videoGenerationCommand);
        Process process = null;
        Runtime rt = Runtime.getRuntime();
        try{
            process = rt.exec(videoGenerationCommand);
        } catch (IOException e) {
            log.error("problems with generating audio files for {} error: {} ", outputFilePath, e);
        }
    }

}
