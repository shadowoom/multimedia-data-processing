package com.i2r.data;

import com.i2r.object.Transcript;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * multimedia-data-processing
 * com.i2r.data
 * Created by Zhang Chen
 * 6/1/2018
 */

@Slf4j
public class AudioCuttingService implements Runnable {

    private String startTime;

    private String endTime;

    private String inputFilePath;

    private String outputFilePath;

    private static final String ffmpegPath = "../ffmpeg/bin/ffmpeg";

    @Override
    public void run(){
        // command format ffmpeg -i file.mkv -ss 00:00:20 -to 00:00:40 -c copy file-2.mkv
        String audioGenerationCommand = ffmpegPath  + " -i " + inputFilePath + " -ss " + startTime
                + " -to " + endTime + " -c copy " + outputFilePath;
        log.info("generate audio file command: {}", audioGenerationCommand);
        Process process = null;
        Runtime rt = Runtime.getRuntime();
        try{
            process = rt.exec(audioGenerationCommand);
        } catch (IOException e) {
            log.error("problems with generating audio files for {} error: {} ", outputFilePath, e);
        }
    }

}
