package com.i2r.utils;

import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 6/2/2018
 */
@Slf4j
public class CSVUtil {

    private static final String SUMMARY_CSV_FILE = "..\\..\\output-data\\summary.csv";

    public static void writeToSummaryCSV(Transcripts transcripts) throws IOException {
        File file = new File(SUMMARY_CSV_FILE);
        CSVPrinter csvPrinter;
        if(!file.exists()) {
            log.info("Create new file: {}", SUMMARY_CSV_FILE);
            file.createNewFile();
            log.info("Successfully created CSV file.");
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SUMMARY_CSV_FILE), StandardCharsets.UTF_8);
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("link", "video_id", "start",
                    "end", "utterance", "transcript", "emotion", "emotion_database_video_location", "emotion_database_audio_location",
                    "dialogue_database_video_location", "dialogue_database_audio_location"));
            for(Transcript transcript : transcripts.getTranscripts()) {
                csvPrinter.printRecord(transcripts.getLink(), transcripts.getId(), transcript.getStartTime(),
                        transcript.getEndTime(), transcript.getName(), transcript.getText(), transcript.getSentiment(), transcript.getVideoOutputLocation(),
                        transcript.getAudioOutputLocation(),
                        transcripts.getVideoOutputLocation(), transcripts.getAudioOutputLocation());
                log.info("Write content line: {}, {}, {}, {}, {}, {], {}, {}, {}, {}, {}",transcripts.getLink(), transcripts.getId(),
                        transcript.getStartTime(), transcript.getEndTime(), transcript.getName(), transcript.getText(), transcript.getSentiment(),
                        transcript.getVideoOutputLocation(), transcript.getAudioOutputLocation(), transcripts.getVideoOutputLocation(), transcripts.getAudioOutputLocation());
            }
            csvPrinter.flush();
            writer.close();
        }
        else {
            log.info("Open file: {}", SUMMARY_CSV_FILE);
            FileWriter fw = new FileWriter(SUMMARY_CSV_FILE, true);
            BufferedWriter writer = new BufferedWriter(fw);
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for(Transcript transcript : transcripts.getTranscripts()) {
                csvPrinter.printRecord(transcripts.getLink(), transcripts.getId(), transcript.getStartTime(),
                        transcript.getEndTime(), transcript.getName(), transcript.getText(), transcript.getSentiment(), transcript.getVideoOutputLocation(),
                        transcript.getAudioOutputLocation(),
                        transcripts.getVideoOutputLocation(), transcripts.getAudioOutputLocation());
                log.info("Write content line: {}, {}, {}, {}, {}, {], {}, {}, {}, {}, {}",transcripts.getLink(), transcripts.getId(),
                        transcript.getStartTime(), transcript.getEndTime(), transcript.getName(), transcript.getText(), transcript.getSentiment(),
                        transcript.getVideoOutputLocation(), transcript.getAudioOutputLocation(), transcripts.getVideoOutputLocation(), transcripts.getAudioOutputLocation());
            }
            csvPrinter.flush();
            writer.close();
            fw.close();
        }
    }

}
