package com.ly.common.utils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取视频Jpg
 */
public class FetchVideoCover {
    private String ffmpegEXE ;

    /**
     * 获取视频jpg 封面的类
     * @param videoInputPath
     * @param coverOutputPath 输出jpg
     * @throws Exception
     */
    public void getCover(String videoInputPath  , String coverOutputPath) throws Exception
    {
        List<String> command = new ArrayList<>() ;
        //拼接命令行

        command.add(ffmpegEXE) ;

        command.add("-ss") ;
        command.add("00:00:01") ;

        command.add("-y") ;
        command.add("-i") ;
        command.add(videoInputPath) ;

        command.add("-vframes") ;
        command.add("1") ;
        command.add(coverOutputPath) ;

        //java 命令行类
        ProcessBuilder processBuilder = new ProcessBuilder(command) ;
        Process process =processBuilder.start() ;

        //输入输出流
        //获取错误的输出流
        InputStream errorStream = process.getErrorStream() ;
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream) ;
        //缓存输入流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "" ;

        while( (line = bufferedReader.readLine()) != null ){}
        //关闭 java流
        if(bufferedReader != null) bufferedReader.close() ;
        if(inputStreamReader != null ) inputStreamReader.close();
        if(errorStream != null) errorStream.close();
    }

    public String getFfmpegEXE() {
        return ffmpegEXE;
    }

    public void setFfmpegEXE(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public FetchVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }
    public FetchVideoCover(){
        super() ;
    }

    public static void main(String [] args) {
        FetchVideoCover fetchVideoCover = new FetchVideoCover("E:\\ffmpeg\\bin\\ffmpeg.exe" );
       try{
           fetchVideoCover.getCover("E:\\ffmpeg\\bin\\video.mp4","E:\\ffmpeg\\bin\\newww.jpg") ;
       }catch (Exception e){
           e.printStackTrace();
       }

    }
}
