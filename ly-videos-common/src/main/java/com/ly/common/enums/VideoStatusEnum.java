package com.ly.common.enums;

public enum VideoStatusEnum {
    SUCCESS(1) ,
    FORBID(2) ;
    public final int value ;
    VideoStatusEnum(int value){
        this.value = value ;
    }
    public int getValue(){
        return value ;
    }
}
