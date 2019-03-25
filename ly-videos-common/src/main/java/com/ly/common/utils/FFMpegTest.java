package com.ly.common.utils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class FFMpegTest {
    private String ffmpegEXE ;
    public FFMpegTest(String ffmpegEXE){
        super() ;
        this.ffmpegEXE = ffmpegEXE ;
    }
    public void convertor(String videoInputPath  , String videoOutputPath) throws  Exception{
        List<String> command = new ArrayList<>() ;
        command.add(ffmpegEXE) ;
        command.add("-i") ;
        command.add(videoInputPath) ;
        command.add("-y") ;
        command.add(videoOutputPath);

        for(String c:command){
            System.out.print(c+ " ") ;
        }
        //新建java执行命令行的类的对象
        ProcessBuilder builder = new ProcessBuilder(command) ;
        Process process = builder.start() ;

        InputStream errorStream = process.getErrorStream() ;
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream) ;
        //缓存类
        BufferedReader br = new BufferedReader(inputStreamReader) ;

        String line = "" ;
        while((line = br.readLine()) != null){

        }
        if(br != null){
            br.close();
        }
        if(inputStreamReader != null){
            errorStream.close();
        }

    }

    public static void main(String[] args){
//        FFMpegTest ffmpeg = new FFMpegTest("E:\\ffmpeg\\bin\\ffmpeg.exe");
//        try{
//            ffmpeg.convertor("C:\\Users\\lypee\\Desktop\\video.mp4","C:\\Users\\lypee\\Desktop\\video2.mp4");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        String s = "12345" ;
        System.out.println( (-1) < s.length());
    }

}
