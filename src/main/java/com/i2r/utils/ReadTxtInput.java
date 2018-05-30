package com.i2r.utils;

import com.i2r.Common.Language;
import com.i2r.object.Transcript;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class ReadTxtInput {

    private String inputPath;

    private String inputName;

    public ReadTxtInput(String inputPath, String inputName) {
        this.inputPath = inputPath;
        this.inputName = inputName;
    }

    public List<Transcript> read() throws IOException {
        List<Transcript> transcriptList = new ArrayList<>();
        log.info("Read file {} ", inputPath);
        File file = new File(inputPath);
        log.info("Start reading in file: ");
        if(file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                log.info("line text: {} ", line);
                String[] temp = line.split("\t");
                String uuid = UUID.randomUUID().toString().replace("-","");
                String speaker = temp[0];
                String startTime = temp[1];
                String endTime =  temp[2];
                String duration = temp[3];
                String text = temp[4].substring(temp[4].indexOf(")") + 1);
                String sentiment = temp[4].substring(1, temp[4].indexOf("-"));
                String languageCode = temp[4].substring(temp[4].indexOf("-")+ 1, temp[4].indexOf("-")+ 2);
                String language = Language.getLanguage(Integer.parseInt(languageCode));
                String path = inputPath.substring(0, inputPath.lastIndexOf("."));
                Transcript transcript = new Transcript();
                transcript.setId(uuid);
                transcript.setSpeaker(speaker);
                transcript.setName(inputName);
                transcript.setStartTime(startTime);
                transcript.setEndTime(endTime);
                transcript.setText(text);
                transcript.setSentiment(sentiment);
                transcript.setLanguage(language);
                transcript.setDuration(duration);
                transcript.setInputPath(path);
                log.info("read in transcript: {} ", transcript);
                transcriptList.add(transcript);
            }
            fileReader.close();
        }
        return transcriptList;
    }

}
