package com.zhouwei.mzbannerview.http;

/**
 *
 * 网络请求结果 基类
 * Created by zhouwei on 16/11/10.
 */

public class BaseResponse<T> {
    public int status;
    public String message;

    public T data;

    public boolean isSuccess(){
        return status == 200;
    }
}
