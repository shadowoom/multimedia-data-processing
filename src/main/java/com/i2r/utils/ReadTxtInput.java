package com.i2r.utils;

import com.i2r.Common.Language;
import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
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

    public ReadTxtInput(String inputPath) {
        this.inputPath = inputPath;
    }

    public Transcripts read() throws IOException {

        Transcripts transcripts = new Transcripts();
        String uuid = UUID.randomUUID().toString().replace("-","");
        transcripts.setId(uuid);

        List<Transcript> transcriptList = new ArrayList<>();
        log.info("Read file {} ", inputPath);
        File file = new File(inputPath);
        log.info("Start reading in file: ");
        if(file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String link = bufferedReader.readLine();
            if(link != null) {
                transcripts.setLink(link.trim());
            }
            String path = inputPath.substring(0, inputPath.lastIndexOf("."));
            transcripts.setInputPath(inputPath);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                log.info("line text: {} ", line);
                String[] temp = line.trim().split("\t");
                String speaker = temp[0];
                String startTime = temp[1];
                String endTime =  temp[2];
                String duration = temp[3];
                String text = temp[4].substring(temp[4].indexOf(")") + 1);
                String sentiment = temp[4].substring(1, temp[4].indexOf("-"));
                String languageCode = temp[4].substring(temp[4].indexOf("-")+ 1, temp[4].indexOf("-")+ 2);
                String language = Language.getLanguage(Integer.parseInt(languageCode));
                Transcript transcript = new Transcript();
                transcript.setSpeaker(speaker);
                transcript.setStartTime(startTime);
                transcript.setEndTime(endTime);
                transcript.setText(text);
                transcript.setSentiment(sentiment);
                transcript.setLanguage(language);
                transcript.setDuration(duration);
                transcript.setLink(link);
                log.info("read in transcript: {} ", transcript);
                transcriptList.add(transcript);
            }
            fileReader.close();
        }
        transcripts.setTranscripts(transcriptList);
        return transcripts;
    }

}
