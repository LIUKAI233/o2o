package com.lk.o2o.exceptions;

public class ShopOperationException extends RuntimeException {
    private static final long serialVersionUID = 1233966719579988941L;

    public ShopOperationException (String msg){
        super(msg);
    }
}
