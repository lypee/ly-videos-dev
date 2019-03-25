package com.ly.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

public class MD5Utils {

    public static void main(String[] args){
        try{
            String md5 = getMD5Str("imooc");
            System.out.println(md5);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getMD5Str(String strValue) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newStr ;
    }
}
