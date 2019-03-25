package com.miniapi.controller;

import com.ly.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis ;
    public static final String USER_REDIS_SESSION = "user-redis-session" ;
    // 文件保存的命名空间
    public static final String FILE_SPACE = "C:\\dev-reposity";
    //ffmpeg路径
    public static final String FFMPEG_EXE="C:\\ffmpeg\\bin\\ffmpeg.exe" ;
    //定义每页展示的视频数
    public static final Integer  PAGE_SIZE= 3;

//    public BasicController() {
//    }
}
