package com.hrushko.service;

import com.hrushko.entity.User;
import com.hrushko.entity.UserInfo;

import java.util.List;

public interface UserInfoService extends Service {
    UserInfo findByUser(User user) throws ServiceException;
    Integer save(UserInfo userInfo) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
