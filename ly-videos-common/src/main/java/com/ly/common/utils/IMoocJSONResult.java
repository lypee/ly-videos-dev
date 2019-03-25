package com.ly.common.utils;

public class IMoocJSONResult {
    //响应的业务状态
    private Integer status ;

    //响应的消息
    private String msg ;

    //响应的数据
    private Object data ;

    //使用
    private String ok ;

    //构造函数
    public IMoocJSONResult(Integer status , String msg , Object data){
        this.status = status ;
        this.msg = msg ;
        this.data = data ;
    }

    //build
    public static IMoocJSONResult build(Integer status , String msg , Object data){
        return new IMoocJSONResult(status , msg , data ) ;
    }

    public static IMoocJSONResult ok(Object data) {
        return new IMoocJSONResult(data);
    }

    public static IMoocJSONResult ok() {
        return new IMoocJSONResult(null);
    }

    public static IMoocJSONResult errorMsg(String msg) {
        return new IMoocJSONResult(500, msg, null);
    }

    public static IMoocJSONResult errorMap(Object data) {
        return new IMoocJSONResult(501, "error", data);
    }

    public static IMoocJSONResult errorTokenMsg(String msg) {
        return new IMoocJSONResult(502, msg, null);
    }

    public static IMoocJSONResult errorException(String msg) {
        return new IMoocJSONResult(555, msg, null);
    }

    public IMoocJSONResult(){

    }

    public IMoocJSONResult(Object data){
        //200 return
        this.status = 200  ;
        this.msg = "OK" ;
        this.data = data ;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

}
