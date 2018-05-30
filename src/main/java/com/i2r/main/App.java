package com.i2r.main;

import com.i2r.object.Transcript;
import com.i2r.utils.GenerateRelevantFile;
import com.i2r.utils.ReadTxtInput;
import com.i2r.utils.VideoToAudio;

import java.io.IOException;
import java.util.List;

public class App 
{
//    public static void main(String[] args) {
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
        //VideoToAudio.convertVideoToAudio("D:/ZhangChen/I2R-Data-Collection/source/2/2.mp4");
        if(args.length != 2) {
            throw new IllegalArgumentException("Please enter input file path and input name");
        }
        String inputPathName = args[0];
        String inputName = args[1];
        ReadTxtInput readTxtInput = new ReadTxtInput(inputPathName, inputName);
        List<Transcript> transcriptList = readTxtInput.read();
        GenerateRelevantFile generateRelevantFile = new GenerateRelevantFile(transcriptList);
        generateRelevantFile.fileGeneration();
    }

}
