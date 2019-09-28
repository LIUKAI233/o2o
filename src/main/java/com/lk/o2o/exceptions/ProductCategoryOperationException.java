package com.lk.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = -2575371659432387521L;

    public ProductCategoryOperationException(String msg){
        super(msg);
    }
}
