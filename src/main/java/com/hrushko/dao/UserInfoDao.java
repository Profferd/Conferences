package com.hrushko.dao;

import com.hrushko.entity.User;
import com.hrushko.entity.UserInfo;

public interface UserInfoDao extends Dao<UserInfo> {
    UserInfo read(User uSer) throws DaoException;
}
