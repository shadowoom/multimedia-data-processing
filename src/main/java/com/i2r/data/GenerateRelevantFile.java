package com.i2r.data;

import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
import com.i2r.utils.CSVUtil;
import com.i2r.utils.StreamGobbler;
import com.i2r.utils.XMLMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class GenerateRelevantFile{

    private Transcripts transcripts;

    private static final String ffmpegPath = "../ffmpeg/bin/";
    //private static final String ffmpegPath = "ffmpeg";
    public GenerateRelevantFile(Transcripts transcripts) {
        this.transcripts = transcripts;
    }

    public void fileGeneration() throws IOException {
        // variables declaration
        String parentFolderPath = "../../output-data/dialogue-data/";

        List<Transcript> transcriptList = transcripts.getTranscripts();

        ReentrantLock lock = new ReentrantLock();

        Collections.sort(transcriptList, comparator);
        for(int i = 0; i < transcriptList.size(); i++) {
            transcriptList.get(i).setSequence(String.valueOf(i+1));
            transcriptList.get(i).setName("utterance_" + transcriptList.get(i).getSequence());
        }

        log.info("\nStart generation data process -----------------------------------------------------------------------\n");

        // setup input files path
        log.info("\nStart setting up input file path---------------------------------------------------------------------\n");
        String inputAudioFilePath = transcripts.getInputPath()+".wav";
        log.info("\nInput audio file path: {}\n", inputAudioFilePath);
        String inputVideoFilePath = transcripts.getInputPath()+".mp4";
        log.info("\nInput video file path: {}\n", inputVideoFilePath);
        log.info("\nDone Setting up input file path----------------------------------------------------------------------\n");

        // setup dialogue output folder path
        log.info("\nStart setting output audio and video folder path for dialogue data-----------------------------------\n");
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
        transcripts.setAudioOutputLocation(outputAudioDirectoryPath.getCanonicalPath().substring(
                outputAudioDirectoryPath.getCanonicalPath().indexOf("output-data")).replace("\\", "/"));
        transcripts.setVideoOutputLocation(outputVideoDirectoryPath.getCanonicalPath().substring(
                outputVideoDirectoryPath.getCanonicalPath().indexOf("output-data")).replace("\\", "/"));
        log.info("\nDone setting output audio and video folder path for dialogue data------------------------------------\n");

        for(Transcript transcript : transcriptList) {
            log.info("\nStart processing object: {}\n", transcript);

            log.info("\nStart setting output audio and video file path for dialogue data for {} -------------------------\n", transcript.getName());
            String outputAudioFilePath = outputAudioDirectoryPath.getPath() + "/" + transcript.getName() +".wav";
            log.info("\noutput audio file path: {}\n", outputAudioFilePath);
            String outputVideoFilePath = outputVideoDirectoryPath.getPath() + "/" + transcript.getName() +".mp4";
            log.info("\noutput video file path: {}\n", outputVideoFilePath);
            log.info("\nDone setting output audio and video file path for dialogue data for {} --------------------------\n", transcript.getName());

            // setup emotion output files path
            log.info("\nStart setting output audio and video file path for emotion data for {} --------------------------\n", transcript.getName());
            String language = transcript.getLanguage();
            String sentiment = transcript.getSentiment();
            switch (language) {
                case "english":
                    parentFolderPath = "../../output-data/english/";
                    break;
                case "chinese":
                    parentFolderPath = "../../output-data/chinese/";
                    break;
                case "mixture":
                    parentFolderPath ="../../output-data/mixture/";
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
            String emotionOutputAudioFilePath = emotionOutputAudioDirectoryPath.getPath() + "/" +  transcript.getName() +".wav";
            log.info("\noutput audio file path: {}\n", emotionOutputAudioFilePath);

            String emotionOutputVideoFilePath = emotionOutputVideoDirectoryPath.getPath() + "/" +  transcript.getName()+".mp4";
            log.info("\noutput video file path: {}\n", emotionOutputVideoFilePath);

            String emotionOutputTextFilePath = emotionOutputTextDirectoryPath.getPath() + "/" +  transcript.getName() + ".xml";
            log.info("\noutput text file path: {}\n", emotionOutputTextFilePath);
            transcript.setAudioOutputLocation(emotionOutputAudioDirectoryPath.getCanonicalPath().substring(
                    emotionOutputAudioDirectoryPath.getCanonicalPath().indexOf("output-data")).replace("\\", "/")
                    + "/" + transcript.getName() + ".wav");
            transcript.setVideoOutputLocation(emotionOutputVideoDirectoryPath.getCanonicalPath().substring(
                    emotionOutputVideoDirectoryPath.getCanonicalPath().indexOf("output-data")).replace("\\", "/")
                    + "/" + transcript.getName() + ".mp4");
            log.info("\nDone setting output audio and video file path for emotion data-----------------------------------\n");

            log.info("\nStart audio segementation for {} ----------------------------------------------------------------\n", transcript.getName());
            // generate audio files
            // command format ffmpeg -i file.mkv -ss 00:00:20 -to 00:00:40 -c copy file-2.mkv
//            String audioGenerationCommand1 = ffmpegPath  + " -i " + inputAudioFilePath + " -ss " + transcript.getStartTime()
//                       + " -to " + transcript.getEndTime() + " -c copy " + outputAudioFilePath;
//            String audioGenerationCommand2  = ffmpegPath  + " -i " + inputAudioFilePath + " -ss " + transcript.getStartTime()
//                    + " -to " + transcript.getEndTime() + " -c copy " + emotionOutputAudioFilePath;

            //setup the process builder
            // create process to segment audio files

            String[] audioGenerationCommand1 = {ffmpegPath + "ffmpeg.exe", "-i", inputAudioFilePath, "-ss", transcript.getStartTime(),
            "-to", transcript.getEndTime(), "-c", "copy", outputAudioFilePath};
            log.info("\ngenerate audio file command 1: {}\n", Arrays.toString(audioGenerationCommand1));
            ProcessBuilder pb = new ProcessBuilder(audioGenerationCommand1);
            pb.redirectErrorStream(true);
            pb.directory(new File(System.getProperty("user.dir")));
            Process proc = pb.start();
            InputStreamReader isr = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            //wait to start the next file
            try {
                if(proc.waitFor() != 0){
                    log.info("\nprocess: {} was interupted\n", Arrays.toString(audioGenerationCommand1));
                    for(String l : lines){
                        log.info(l);
                    }
                }
            } catch (InterruptedException e) {
                log.info("\nprocess: {} was interupted\n", Arrays.toString(audioGenerationCommand1));
                e.printStackTrace();
            } finally {
                br.close();
                isr.close();
            }

            String[] audioGenerationCommand2 = {ffmpegPath + "ffmpeg.exe", "-i", inputAudioFilePath, "-ss", transcript.getStartTime(),
                    "-to", transcript.getEndTime(), "-c", "copy", emotionOutputAudioFilePath};
            log.info("\ngenerate audio file command 2: {}\n", Arrays.toString(audioGenerationCommand2));
            ProcessBuilder pb2 = new ProcessBuilder(audioGenerationCommand2);
            pb2.redirectErrorStream(true);
            pb2.directory(new File(System.getProperty("user.dir")));
            Process proc2 = pb2.start();
            InputStreamReader isr2 = new InputStreamReader(proc2.getInputStream());
            BufferedReader br2 = new BufferedReader(isr2);
            List<String> lines2 = new ArrayList<>();
            String line2;
            while ((line2 = br2.readLine()) != null) {
                lines2.add(line2);
            }
            try {
                if(proc2.waitFor() != 0){
                    log.info("\nprocess: {} was interupted\n", Arrays.toString(audioGenerationCommand2));
                    for(String l : lines2){
                        log.info(l);
                    }
                }
            } catch (InterruptedException e) {
                log.info("\nprocess: {} was interupted\n", Arrays.toString(audioGenerationCommand2));
                e.printStackTrace();
            } finally {
                br2.close();
                isr2.close();
            }

            log.info("\nDone audio segementation for {} -----------------------------------------------------------------\n", transcript.getName());

            log.info("\nStart video segementation for {} ----------------------------------------------------------------\n", transcript.getName());

            // create process to segment video files
            String videoGenerationCommand1 = ffmpegPath + "ffmpeg" + " -y -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
                    + " -to " + transcript.getEndTime() + " -c:v libx264 -an -strict -2 " + outputVideoFilePath;
            log.info("\ngenerate video file command 1 : {}\n", videoGenerationCommand1);
            Runtime rt = Runtime.getRuntime();
            try{
                Process proc3 = rt.exec(videoGenerationCommand1);
                StreamGobbler errorGobbler = new StreamGobbler(proc3.getErrorStream(), "ERROR");
                StreamGobbler outputGobbler = new StreamGobbler(proc3.getInputStream(), "OUTPUT");
                errorGobbler.start();
                outputGobbler.start();
                int exitVal = proc3.waitFor();
            } catch (IOException | InterruptedException e) {
                log.error("problems with generating audio files for {} error: {} ", transcript.getName(), e);
            }


            String videoGenerationCommand2 = ffmpegPath + "ffmpeg" + " -y -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
                    + " -to " + transcript.getEndTime() + " -c:v libx264 -an -strict -2 " + emotionOutputVideoFilePath;
            log.info("\ngenerate video file command 2 : {}\n", videoGenerationCommand2);
            try{
                Process proc4 = rt.exec(videoGenerationCommand2);
                StreamGobbler errorGobbler = new StreamGobbler(proc4.getErrorStream(), "ERROR");
                StreamGobbler outputGobbler = new StreamGobbler(proc4.getInputStream(), "OUTPUT");
                errorGobbler.start();
                outputGobbler.start();
                int exitVal = proc4.waitFor();
            } catch (IOException | InterruptedException e) {
                log.error("problems with generating audio files for {} error: {} ", transcript.getName(), e);
            }

            log.info("\nDone video segementation for {} -----------------------------------------------------------------\n", transcript.getName());
//            Runtime rt = Runtime.getRuntime();
//            Process p;
//            try{
//                p = rt.exec(audioGenerationCommand1);
//                p = rt.exec(audioGenerationCommand2);
//            } catch (IOException e) {
//                log.error("problems with generating audio files for {} error: {} ", transcript.getName(), e);
//            }

//

            // generate video files
//            String videoGenerationCommand1 = ffmpegPath + " -y -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
//                    + " -to " + transcript.getEndTime() + " -c:v libx264 -an -strict -2 " + outputVideoFilePath;
//            String videoGenerationCommand2 = ffmpegPath + " -y -i " + inputVideoFilePath + " -ss " + transcript.getStartTime()
//                    + " -to " + transcript.getEndTime() + " -c:v libx264 -an -strict -2 " + emotionOutputVideoFilePath;


//            try {
//                p = rt.exec(videoGenerationCommand1);
//                p = rt.exec(videoGenerationCommand2);
//            } catch (IOException e) {
//                log.error("problems with generating video files for {} error: {} ", transcript.getName(), e);
//            }


            log.info("Start individual transcript file generation for {} ----------------------------------------------", transcript.getName());
            XMLMapper.objToXML(emotionOutputTextFilePath, transcript);
            log.info("Done individual transcript file generation for {} -----------------------------------------------", transcript.getName());
        }


        log.info("Start full transcript file generation----------------------------------------------------------------");
        // generate text data files
        String outputTextFilePath = outputTextDirectoryPath.getPath() + "/" + transcripts.getId() + ".xml";
        try {
            XMLMapper.listToXML(outputTextFilePath, transcriptList);
        } catch (Exception e) {
            log.error("problems with generating full text file, error: {} ", e);
        }
        log.info("Done full transcript file generation-----------------------------------------------------------------");
        log.info("Done generation data process ------------------------------------------------------------------------");
        log.info("Start writing to summary CSV file -------------------------------------------------------------------");
        CSVUtil.writeToSummaryCSV(transcripts);
        log.info("Done writing to summary CSV file --------------------------------------------------------------------");
    }

    private Comparator<Transcript> comparator = new Comparator<Transcript>() {
        @Override
        public int compare(Transcript o1, Transcript o2) {
            return o1.getEndTime().compareTo(o2.getEndTime());
        }
    };

}
