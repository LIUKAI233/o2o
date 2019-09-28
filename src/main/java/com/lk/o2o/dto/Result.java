package com.lk.o2o.dto;

public class Result<T> {
    private boolean success; //是否成功的标志
    private T data;//成功返回的数据
    private String errMsg;//错误信息
    private int errorCode;

    public Result(){

    }

    /*这是成功时使用的构造方法*/
    public Result(boolean success,T data){
        this.success = success;
        this.data = data;
    }

    /*这是失败时使用的构造方法*/
    public Result(boolean success,String errMsg,int errorCode){
        this.success = success;
        this.errMsg = errMsg;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
