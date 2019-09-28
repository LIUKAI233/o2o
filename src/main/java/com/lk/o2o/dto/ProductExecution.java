package com.lk.o2o.dto;

import com.lk.o2o.entity.Product;
import com.lk.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {
    // 状态标识
    private int state;
    //状态描述
    private String stateInfo;
    //商品数量
    private int count;
    //操作的product(增删改的时候用到)
    private Product product;
    //product列表(查询商品列表的时候用到)
    private List<Product> productList;

    public ProductExecution(){

    }

    //商品操作失败使用的构造器
    public ProductExecution(ProductStateEnum productStateEnump){
        this.state = productStateEnump.getStatue();
        this.stateInfo = productStateEnump.getStateInfo();
    }
    //商品操作成功使用的构造器
    public ProductExecution(ProductStateEnum productStateEnump, Product product){
        this.state = productStateEnump.getStatue();
        this.stateInfo = productStateEnump.getStateInfo();
        this.product = product;
    }
    //商品操作成功使用的构造器
    public ProductExecution(ProductStateEnum productStateEnump, List<Product> productList){
        this.state = productStateEnump.getStatue();
        this.stateInfo = productStateEnump.getStateInfo();
        this.productList = productList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
