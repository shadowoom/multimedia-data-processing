package com.i2r.utils;

import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class GenerateRelevantFile{

    private Transcripts transcripts;

    private static final String ffmpegPath = "../ffmpeg/bin/ffmpeg";

    public GenerateRelevantFile(Transcripts transcripts) {
        this.transcripts = transcripts;
    }

    public void fileGeneration() throws IOException {
        // variables declaration
        String parentFolderPath = "../../dialogue-data/";

        String audioGenerationCommand, videoGenerationCommand;

        List<Transcript> transcriptList = transcripts.getTranscripts();

        Collections.sort(transcriptList, comparator);
        for(int i = 0; i < transcriptList.size(); i++) {
            transcriptList.get(i).setSequence(String.valueOf(i+1));
            transcriptList.get(i).setName("utterance_" + transcriptList.get(i).getSequence());
        }

        log.info("Start generation data process -----------------------------------------------------------------------");

        // setup input files path
        log.info("Start setting up input file path---------------------------------------------------------------------");
        String inputAudioFilePath = transcripts.getInputPath()+".wav";
        log.info("Input audio file path: {}", inputAudioFilePath);
        String inputVideoFilePath = transcripts.getInputPath()+".mp4";
        log.info("Input video file path: {}", inputVideoFilePath);
        log.info("Done Setting up input file path----------------------------------------------------------------------");

        // setup dialogue output folder path
        log.info("Start setting output audio and video folder path for dialogue data-----------------------------------");
        File outputAudioDirectoryPath =  new File(parentFolderPath + "audio/" + transcripts.getId());
        File outputVideoDirectoryPath  = new File(parentFolderPath + "video/" + transcripts.getId());
        File outputTextDirectoryPath = new File(parentFolderPath + "text/" + transcripts.getId());
        if(!outputAudioDirectoryPath.exists()) {
            outputAudioDirectoryPath.mkdir();
        }
        if(!outputVideoDirectoryPath.exists()) {
            outputVideoDirectoryPath.mkdir();
        }
        if(!outputTextDirectoryPath.exists()) {
            outputTextDirectoryPath.mkdir();
        }
        transcripts.setAudioOutputLocation(outputAudioDirectoryPath.getCanonicalPath());
        transcripts.setVideoOutputLocation(outputVideoDirectoryPath.getCanonicalPath());
        log.info("Done setting output audio and video folder path for dialogue data------------------------------------");

        for(Transcript transcript : transcriptList) {
            log.info("Start processing object: {} ", transcript);

            log.info("Start setting output audio and video file path for dialogue data for {} -------------------------", transcript.getName());
            String outputAudioFilePath = outputAudioDirectoryPath.getPath() + "/" + transcript.getName() +".wav";
            log.info("output audio file path: {}", outputAudioFilePath);
            String outputVideoFilePath = outputVideoDirectoryPath.getPath() + "/" + transcript.getName() +".mp4";
            log.info("output video file path: {}", outputVideoFilePath);
            log.info("Done setting output audio and video file path for dialogue data for {} --------------------------", transcript.getName());

            // setup emotion output files path
            log.info("Start setting output audio and video file path for emotion data for {} --------------------------", transcript.getName());
            String language = transcript.getLanguage();
            String sentiment = transcript.getSentiment();
            switch (language) {
                case "english":
                    parentFolderPath = "../../english/";
                    break;
                case "chinese":
                    parentFolderPath = "../../chinese/";
                    break;
                case "mixture":
                    parentFolderPath ="../../mixture/";
                    break;
            }
            switch (sentiment) {
                case "happiness":
                    parentFolderPath = parentFolderPath + "emotion-data/happiness/";
                    break;
                case "neutral":
                    parentFolderPath = parentFolderPath + "emotion-data/neutral/";
                    break;
                case "sadness":
                    parentFolderPath = parentFolderPath + "emotion-data/sadness/";
                    break;
                case "surprise":
                    parentFolderPath = parentFolderPath + "emotion-data/surprise/";
                    break;
                case "disgust":
                    parentFolderPath = parentFolderPath + "emotion-data/disgust/";
                    break;
                case "anger":
                    parentFolderPath = parentFolderPath + "emotion-data/anger/";
                    break;
                case "fear":
                    parentFolderPath = parentFolderPath + "emotion-data/fear/";
                    break;
            }

            File emotionOutputAudioDirectoryPath =  new File(parentFolderPath + "audio/" + transcripts.getId());
            File emotionOutputVideoDirectoryPath = new File(parentFolderPath + "video/" + transcripts.getId());
            File emotionOutputTextDirectoryPath = new File(parentFolderPath + "text/" + transcripts.getId());
            if(!emotionOutputAudioDirectoryPath.exists()) {
                emotionOutputAudioDirectoryPath.mkdir();
            }
            if(!emotionOutputVideoDirectoryPath.exists()) {
                emotionOutputVideoDirectoryPath.mkdir();
            }
            if(!emotionOutputTextDirectoryPath.exists()) {
                emotionOutputTextDirectoryPath.mkdir();
            }
            String emotionOutputAudioFilePath = emotionOutputAudioDirectoryPath.getCanonicalPath() + "/" +  transcript.getName() +".wav";
            log.info("output audio file path: {}", emotionOutputAudioFilePath);

            String emotionOutputVideoFilePath = emotionOutputVideoDirectoryPath.getCanonicalPath() + "/" +  transcript.getName()+".mp4";
            log.info("output video file path: {}", emotionOutputVideoFilePath);

            String emotionOutputTextFilePath = emotionOutputTextDirectoryPath.getCanonicalPath() + "/" +  transcript.getName() + ".xml";
            log.info("output text file path: {}", emotionOutputTextFilePath);
            transcript.setAudioOutputLocation(emotionOutputAudioFilePath);
            transcript.setVideoOutputLocation(emotionOutputVideoFilePath);
            log.info("Done setting output audio and video file path for emotion data-----------------------------------");

            log.info("Start audio segementation for {} ----------------------------------------------------------------", transcript.getName());
            // generate audio files
            // command format ffmpeg -i file.mkv -ss 00:00:20 -to 00:00:40 -c copy file-2.mkv
            audioGenerationCommand = ffmpegPath  + " -i " + inputAudioFilePath + " -ss " + transcript.getStartTime()
                        + " -to " + transcript.getEndTime() + " -c copy " + outputAudioFilePath
                        + " " + emotionOutputAudioFilePath;
            log.info("generate audio file command: {}", audioGenerationCommand);
            Runtime rt = Runtime.getRuntime();
            Process p1;
            try{
                p1 = rt.exec(audioGenerationCommand);
            } catch (IOException e) {
                log.error("problems with generating audio files for {} error: {} ", transcript.getName(), e);
            }
            log.info("Done audio segementation for {} -----------------------------------------------------------------", transcript.getName());

            log.info("Start video segementation- for {} ---------------------------------------------------------------", transcript.getName());
            // generate video files
            videoGenerationCommand = ffmpegPath + " -y -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
                    + " -to " + transcript.getEndTime() + " -c:v libx264 -preset superfast -an " + outputVideoFilePath
                    + " " + emotionOutputVideoFilePath;
            log.info("generate video file command: {}", videoGenerationCommand);
            Process p2;
            try {
                p2 = rt.exec(videoGenerationCommand);
            } catch (IOException e) {
                log.error("problems with generating video files for {} error: {} ", transcript.getName(), e);
            }
            log.info("Done video segementation for {} -----------------------------------------------------------------", transcript.getName());

            log.info("Start individual transcript file generation for {} ----------------------------------------------", transcript.getName());
            XMLMapper.objToXML(emotionOutputTextFilePath, transcript);
            log.info("Done individual transcript file generation for {} -----------------------------------------------", transcript.getName());
        }

        log.info("Start full transcript file generation----------------------------------------------------------------");
        // generate text data files
        for(Transcript transcript : transcriptList) {
            transcript.setVideoOutputLocation(outputVideoDirectoryPath.getCanonicalPath());
            transcript.setAudioOutputLocation(outputAudioDirectoryPath.getCanonicalPath());
        }
        String outputTextFilePath = outputTextDirectoryPath.getPath() + "/" + transcripts.getId() + ".xml";
        try {
            XMLMapper.listToXML(outputTextFilePath, transcriptList);
        } catch (Exception e) {
            log.error("problems with generating full text file, error: {} ", e);
        }
        log.info("Done full transcript file generation-----------------------------------------------------------------");
        log.info("Done generation data process ------------------------------------------------------------------------");
    }

    private Comparator<Transcript> comparator = new Comparator<Transcript>() {
        @Override
        public int compare(Transcript o1, Transcript o2) {
            return o1.getEndTime().compareTo(o2.getEndTime());
        }
    };

}
