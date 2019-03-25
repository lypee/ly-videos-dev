package com.ly.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {
    //ffmpeg 所在的文件路径
    private String ffmpegEXE ;

    public MergeVideoMp3(String ffmpegEXE){
        super();
        this.ffmpegEXE = ffmpegEXE ;
    }

    public void convert(String videoInputPath  , String mp3InputPath ,Double seconds ,
                        String videoOutputPath) throws Exception{
        //ffmpeg.exe -i video.mp4 -i 成全.mp3 -t 7 -y Ly.mp4
        List<String> command = new ArrayList<>() ;
        //添加命令行命令
        command.add(ffmpegEXE) ;
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i") ;
        command.add(mp3InputPath);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-y");
        command.add(videoOutputPath);

        //创建命令行命令对象
        ProcessBuilder processBuilder = new ProcessBuilder(command) ;
        Process process = processBuilder.start() ;

        //错误流
        InputStream errorStream = process.getErrorStream() ;
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream) ;

        BufferedReader br = new BufferedReader(inputStreamReader) ;

        String line = "" ;
        while( (line = br.readLine()) != null){

        }
        if(br != null )  br.close();
        if(inputStreamReader != null) inputStreamReader.close();
        if(errorStream != null) errorStream.close();

    }
    public static void main(String[] args) {
        MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3("E:\\ffmpeg\\bin\\ffmpeg.exe") ;
        try{
            mergeVideoMp3.convert("E:\\ffmpeg\\bin\\video.mp4","E:\\ffmpeg\\bin\\成全.mp3",8.0,"E:\\ffmpeg\\bin\\123y.mp4");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
