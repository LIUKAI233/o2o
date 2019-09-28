package com.lk.o2o.exceptions;

public class LocalAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = -779800375370257449L;

    public LocalAuthOperationException(String msg){
        super(msg);
    }
}
