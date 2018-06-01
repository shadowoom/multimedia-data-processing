package com.i2r.data;

import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
import com.i2r.utils.GenerateRelevantFile;
import com.i2r.utils.ReadTxtInput;
import com.i2r.utils.VideoToAudio;

import java.io.IOException;
import java.util.List;

public class PrepareData
{
//    public static void data(String[] args) {
//        //Create video chunk
//        int startSecs = 10;
//        String startTime = convertSecsToTimeString(startSecs);
//        int endSecs = 20;
//        String endTime = convertSecsToTimeString(endSecs);
//
//        //Call ffmpeg to create this chunk of the video using a ffmpeg wrapper
//        String currentWorkingPath = System.getProperty("user.dir");
//        String inputFilePath = currentWorkingPath + "/input.mp4";
//        String outputFilePath = currentWorkingPath + "/output.mp4";
//        String ffmpegPath = currentWorkingPath.substring(0, currentWorkingPath.lastIndexOf("/") + 1)
//                + "ffmpeg/bin/ffmpeg";
//        String command = ffmpegPath + " -i "+ inputFilePath +" -ss " + startTime
//                + " -t " + endTime + " -c copy " + outputFilePath;
//        Runtime rt = Runtime.getRuntime();
//        try {
//            Process pr = rt.exec(command);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static String convertSecsToTimeString(int timeSeconds) {
//        //Convert number of seconds into hours:mins:seconds string
//        int hours = timeSeconds / 3600;
//        int mins = (timeSeconds % 3600) / 60;
//        int secs = timeSeconds % 60;
//        String timeString = String.format("%02d:%02d:%02d", hours, mins, secs);
//        return timeString;
//    }



    public static void main(String[] args) throws IOException{

        // convert video to audio
        if(args.length == 1) {
            if(args[0].contains("mp4")) {
                String inputPathName = args[0];
                VideoToAudio.convertVideoToAudio(inputPathName);
            }
            else {
                String inputPathName = args[0];
                ReadTxtInput readTxtInput = new ReadTxtInput(inputPathName);
                Transcripts transcripts = readTxtInput.read();
                GenerateRelevantFile generateRelevantFile = new GenerateRelevantFile(transcripts);
                generateRelevantFile.fileGeneration();
            }
        }
        else {
            throw new IllegalArgumentException("Please enter input file path and input name");
        }
    }

}
