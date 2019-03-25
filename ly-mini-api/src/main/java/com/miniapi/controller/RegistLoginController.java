package com.miniapi.controller;

import com.ly.common.utils.IMoocJSONResult;
import com.ly.common.utils.MD5Utils;
import com.ly.pojo.Users;
import com.ly.pojo.vo.UsersVO;
import com.ly.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@Api(value="注册/登陆的接口" ,tags = {"注册/登陆的controller"})
public class RegistLoginController  extends BasicController{

    @Autowired
    private UserService userService;

    @PostMapping("regist")
    @ApiOperation(value = "注册的接口")
    public IMoocJSONResult regist(@RequestBody Users users) throws Exception {
//        System.out.println("UserName: "+users.getUsername() + " , UserPassword : " + users.getPassword() );
        //提取用户名和密码必须不为空
        if(StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
            return IMoocJSONResult.errorMsg("用户名或密码为空");
        }
        //判断用户是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(users.getUsername());
//        System.out.println("用户是否存在: "+usernameIsExist);
        if(!usernameIsExist){
            users.setNickname(users.getUsername());
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setFansCounts(0);
            users.setFollowCounts(0);
            users.setReceiveLikeCounts(0);
            users.setFaceImage(null);
            userService.saveUser(users);
        }else{
            return IMoocJSONResult.errorMsg("用户名已经存在 , 请换一个再试试 ! ") ;
        }
        //安全性问题 设置返回到前端的password为空
        /**
         * 配置redis后
         */
        users.setPassword("");

        return IMoocJSONResult.ok(users) ;
    }
    @ApiOperation(value="用户登录", notes="用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMoocJSONResult.ok("用户名或密码不能为空...");
        }
//        Thread.sleep(3000);

        // 2. 判断用户是否存在
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(user.getPassword()));

        // 3. 返回
       if(userResult != null){
           //for safe
           userResult.setPassword("");
           String uniqueToken = UUID.randomUUID().toString();
           //设置redis和过期时间
           redis.set(USER_REDIS_SESSION + ":"+ userResult.getId() , uniqueToken , 1000*60*30);
           //空对象
//           System.out.println("redis: "+redis.get(USER_REDIS_SESSION + ":"+ userResult.getId()));
           UsersVO usersVO = new UsersVO();
           BeanUtils.copyProperties(userResult,usersVO);
           usersVO.setUserToken(uniqueToken);
           return IMoocJSONResult.ok(usersVO) ;
       }else {
           return IMoocJSONResult.errorMsg("用户名或密码不正确");
       }
    }
    @ApiImplicitParam(name = "userId",value = "用户id", required = true , dataType = "String" , paramType = "query")
    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @PostMapping("/logout")
    public IMoocJSONResult logout(String userId) throws Exception
    {
        redis.del(USER_REDIS_SESSION+":"+userId);
        return IMoocJSONResult.ok();
    }
}


