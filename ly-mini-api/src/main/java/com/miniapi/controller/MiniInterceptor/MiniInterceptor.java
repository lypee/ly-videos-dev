package com.miniapi.controller.MiniInterceptor;

import com.ly.common.utils.IMoocJSONResult;
import com.ly.common.utils.JsonUtils;
import com.ly.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
public class MiniInterceptor  implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis ;

    public static final String USER_REDIS_SESSION="user-redis-session" ;


    /**
     * 在Controller之前 拦截请求
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader("headerUserId");
        String userToken = httpServletRequest.getHeader("headerUserToken");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
                System.out.println("请登录3...");
                returnErrorResponse( httpServletResponse, new IMoocJSONResult().errorTokenMsg("请登录1..."));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    System.out.println("账号被挤出...");
                    returnErrorResponse( httpServletResponse, new IMoocJSONResult().errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        } else {
//            returnErrorResponse( httpServletResponse, new IMoocJSONResult().errorTokenMsg("请登录2..."));
            return true;
        }
        return true ;
    }
    public void returnErrorResponse(HttpServletResponse httpServletResponse , IMoocJSONResult iMoocJSONResult) throws  IOException ,UnsupportedEncodingException{
        OutputStream outputStream = null ;
//        System.out.println(iMoocJSONResult.getData()+ " , " + iMoocJSONResult.getMsg());
        try{
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/json");
            outputStream = httpServletResponse.getOutputStream() ;
            outputStream.write(JsonUtils.objectToJson(iMoocJSONResult).getBytes("utf-8"));
            outputStream.flush();
        }finally {
            if(outputStream != null){
                outputStream.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        System.out.println("PostHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        System.out.println("afterCompletion");
    }

}
