package com.lk.o2o.dao;

import com.lk.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {

    /**
     * 根据对应的用户名和密码，查询用户信息
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    LocalAuth queryLocalByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户id查询用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    LocalAuth queryLocalByUserId(@Param("userId") Long userId);

    /**
     * 添加平台账号
     * @param localAuth 用户信息
     * @return 处理结果
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 更改用户信息
     * @param userId 用户id
     * @param username 用户名
     * @param password 密码
     * @param newpassword 新密码
     * @param lastEditTime 修改时间
     * @return 处理结果
     */
    int updataLocalAuth(@Param("userId") Long userId, @Param("username") String username, @Param("password") String password,
                        @Param("newpassword") String newpassword, @Param("lastEditTime") Date lastEditTime);
}
