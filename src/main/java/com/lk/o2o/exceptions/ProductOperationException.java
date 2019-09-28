package com.lk.o2o.exceptions;

public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = 6772397063348153370L;

    public ProductOperationException(String msg){
        super(msg);
    }
}
