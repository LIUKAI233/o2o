package com.lk.o2o.service;

import com.lk.o2o.dto.LocalAuthExecution;
import com.lk.o2o.entity.LocalAuth;

public interface LocalAuthService {

    /**
     * 通过账号密码，获取平台账号信息
     * @param username 用户名
     * @param password 密码
     * @return 平台账号信息
     */
    LocalAuth getLocalAuthByUsernameAndPassword(String username, String password);

    /**
     * 通过用户id，获取平台账号信息
     * @param userId 用户id
     * @return 平台账号信息
     */
    LocalAuth getLocalAuthByUserId(Long userId);

    /**
     * 注册信息
     * @param localAuth 添加信息
     * @return 处理结果
     */
    LocalAuthExecution addLocalAuth(LocalAuth localAuth);

    /**
     * 更改密码
     * @param userId 用户id
     * @param username 用户名
     * @param password 密码
     * @param newpassword 新密码
     * @return 处理结果
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newpassword);
}
