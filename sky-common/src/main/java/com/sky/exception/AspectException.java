package com.sky.exception;

/*
* 自定义切面异常
* */
public class AspectException extends RuntimeException{

    public AspectException() {
    }

    public AspectException(String msg) {
        super(msg);
    }
}
