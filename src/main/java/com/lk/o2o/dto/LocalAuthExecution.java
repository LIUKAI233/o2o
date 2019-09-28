package com.lk.o2o.dto;

import com.lk.o2o.entity.LocalAuth;
import com.lk.o2o.enums.LocalAuthStateEnum;

import java.util.List;

public class LocalAuthExecution {
    private int state;
    private String stateInfo;
    private int count;
    private LocalAuth localAuth;
    private List<LocalAuth> localAuthList;

    public LocalAuthExecution(){

    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum){
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, LocalAuth localAuth){
        this.state = state;
        this.stateInfo = stateInfo;
        this.localAuth = localAuth;
    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, List<LocalAuth> localAuthList){
        this.state = state;
        this.stateInfo = stateInfo;
        this.localAuthList = localAuthList;
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

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> localAuthList) {
        this.localAuthList = localAuthList;
    }
}
