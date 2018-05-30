package com.i2r.utils;

import com.i2r.object.Transcript;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class GenerateRelevantFile {

    private List<Transcript> transcriptList;

    private static final String ffmpegPath = "../ffmpeg/bin/ffmpeg";

    public GenerateRelevantFile(List<Transcript> transcriptList) {
        this.transcriptList = transcriptList;
    }

    public void fileGeneration() {
        // variables declaration
        String parentFolderPath = "../../dialogue-data/";
        String languge, sentiment;
        String inputAudioFilePath, inputVideoFilePath;
        File outputAudioDirectoryPath, outputVideoDirectoryPath, outputTextDirectoryPath;
        String outputAudioFilePath, outputVideoFilePath, outputTextFilePath;
        String audioGenerationCommand, videoGenerationCommand;
        Process pr1;
        Process pr2;
        ReentrantLock lock = new ReentrantLock();


        // generate dialogue data
        log.info("Start generation dialogue data process ------------------------------------");
        Collections.sort(transcriptList, comparator);

        for(int i = 0; i < transcriptList.size(); i++) {
            transcriptList.get(i).setSequence(String.valueOf(i+1));
            log.info("start processing object: {} ", transcriptList.get(i));
            // setup input and output filepath
            inputAudioFilePath = transcriptList.get(i).getInputPath()+".wav";
            log.info("input audio file path: {}", inputAudioFilePath);
            inputVideoFilePath = transcriptList.get(i).getInputPath()+".mp4";
            log.info("input video file path: {}", inputVideoFilePath);

            outputAudioDirectoryPath =  new File(parentFolderPath + "wav/" + transcriptList.get(i).getName());
            outputVideoDirectoryPath  = new File(parentFolderPath + "video/" + transcriptList.get(i).getName());
            if(!outputAudioDirectoryPath.exists()) {
                outputAudioDirectoryPath.mkdir();
            }
            if(!outputVideoDirectoryPath.exists()) {
                outputVideoDirectoryPath.mkdir();
            }

            outputAudioFilePath = outputAudioDirectoryPath.getPath() + "/" +  transcriptList.get(i).getSequence() +".wav";
            log.info("output audio file path: {}", outputAudioFilePath);

            outputVideoFilePath = outputVideoDirectoryPath.getPath() + "/" +  transcriptList.get(i).getSequence()+".mp4";
            log.info("output video file path: {}", outputVideoFilePath);

            Runtime rt = Runtime.getRuntime();

            try {
                // generate data files
                audioGenerationCommand = ffmpegPath + " -i " + inputAudioFilePath + " -ss " + transcriptList.get(i).getStartTime()
                        + " -to " + transcriptList.get(i).getEndTime() + " -c copy " + outputAudioFilePath;
                lock.lock();
                pr1 = rt.exec(audioGenerationCommand);
                log.info("generate audio file command: {}", audioGenerationCommand);
                videoGenerationCommand = ffmpegPath + " -i " + inputVideoFilePath + " -ss " + transcriptList.get(i).getStartTime()
                        + " -to " + transcriptList.get(i).getEndTime() + " -c copy " + outputVideoFilePath;
                pr2 = rt.exec(videoGenerationCommand);
                log.info("generate video file command: {}", videoGenerationCommand);
                transcriptList.get(i).setOutputAudioPath(outputAudioFilePath);
                transcriptList.get(i).setOutputVideoPath(outputVideoFilePath);
                lock.unlock();
            } catch (Exception e) {
                log.error("problems with generating audio/video files for {} error: {} ", transcriptList.get(i).getInputPath(), e);
            }
            log.info("finish generating emotion data files for object: {}", transcriptList.get(i));
        }

        // generate text data files
        outputTextDirectoryPath = new File(parentFolderPath + "text/" + transcriptList.get(0).getName());
        if(!outputTextDirectoryPath.exists()) {
            outputTextDirectoryPath.mkdir();
        }
        outputTextFilePath = outputTextDirectoryPath.getPath() + "/" + transcriptList.get(0).getName() + ".xml";
        try {
            XMLMapper.listToXML(outputTextFilePath, transcriptList);
        } catch (Exception e) {
            log.error("problems with generating text files for {} error: {} ", transcriptList.get(0).getName(), e);
        }
        log.info("end generation dialogue data process ------------------------------------");
        log.info("--------------------------------------------------------------------------");

        // generate emotion data
        log.info("Start generation emotion data process ------------------------------------");
        for(Transcript transcript : transcriptList) {
            languge = transcript.getLanguage();
            sentiment = transcript.getSentiment();
            parentFolderPath = "";
            switch (languge) {
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
            log.info("start processing object: {} ", transcript);
            // setup input and output filepath
            inputAudioFilePath = transcript.getInputPath()+".wav";
            log.info("input audio file path: {}", inputAudioFilePath);
            inputVideoFilePath = transcript.getInputPath()+".mp4";
            log.info("input video file path: {}", inputVideoFilePath);
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
            }
            outputAudioDirectoryPath =  new File(parentFolderPath + "wav/" + transcript.getName());
            outputVideoDirectoryPath = new File(parentFolderPath + "video/" + transcript.getName());
            outputTextDirectoryPath = new File(parentFolderPath + "text/" + transcript.getName());
            if(!outputAudioDirectoryPath.exists()) {
                outputAudioDirectoryPath.mkdir();
            }
            if(!outputVideoDirectoryPath.exists()) {
                outputVideoDirectoryPath.mkdir();
            }
            if(!outputTextDirectoryPath.exists()) {
                outputTextDirectoryPath.mkdir();
            }
            outputAudioFilePath = outputAudioDirectoryPath.getPath() + "/" +  transcript.getId() +".wav";
            log.info("output audio file path: {}", outputAudioFilePath);

            outputVideoFilePath = outputVideoDirectoryPath.getPath() + "/" +  transcript.getId()+".mp4";
            log.info("output video file path: {}", outputVideoFilePath);

            outputTextFilePath = outputTextDirectoryPath.getPath() + "/" +  transcript.getId() + ".xml";
            log.info("output text file path: {}", outputTextFilePath);
            Runtime rt = Runtime.getRuntime();
            try {
                // generate data files
                audioGenerationCommand = ffmpegPath + " -i " + inputAudioFilePath + " -ss " + transcript.getStartTime()
                        + " -to " + transcript.getEndTime() + " -c copy " + outputAudioFilePath;
                lock.lock();
                pr1 = rt.exec(audioGenerationCommand);
                log.info("generate audio file command: {}", audioGenerationCommand);
                videoGenerationCommand = ffmpegPath + " -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
                        + " -to " + transcript.getEndTime() + " -c copy " + outputVideoFilePath;
                pr2 = rt.exec(videoGenerationCommand);
                log.info("generate video file command: {}", videoGenerationCommand);
                // generate text data files
                transcript.setOutputAudioPath(outputAudioFilePath);
                transcript.setOutputVideoPath(outputVideoFilePath);
                XMLMapper.objToXML(outputTextFilePath, transcript);
                lock.unlock();
            } catch (Exception e) {
                log.warn("problems with generating files for {} error: {} ", transcript.getInputPath(), e);
            }
            log.info("finish generating emotion data files for object: {}", transcript);
        }
    }

    private Comparator<Transcript> comparator = new Comparator<Transcript>() {
        @Override
        public int compare(Transcript o1, Transcript o2) {
            return o1.getEndTime().compareTo(o2.getEndTime());
        }
    };

}
