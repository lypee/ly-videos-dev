package com.miniapi.controller;


import com.ly.common.utils.IMoocJSONResult;
import com.ly.service.BgmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "背景音乐接口",tags = {"背景音乐接口的Controller"})
@RestController
@RequestMapping("/bgm")
public class BgmController {
    @Autowired
    private BgmService bgmService ;

    @ApiOperation(value="获取背景音乐列表",notes="获取背景音乐列表的接口")
    @PostMapping("/list")
    public IMoocJSONResult list(){
        return IMoocJSONResult.ok(bgmService.queryBgmList()) ;
    }
}
