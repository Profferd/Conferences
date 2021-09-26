package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.dao.UserInfoDao;
import com.hrushko.entity.User;
import com.hrushko.entity.UserInfo;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserInfoService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class UserInfoServiceImpl extends ServiceImpl implements UserInfoService {
    private static final Logger logger = LogManager.getLogger(UserInfoServiceImpl.class);

    /**
     * Instantiates a new User info service.
     *
     * @throws ServiceException the service exception
     */
    public UserInfoServiceImpl() throws ServiceException {
    }

    @Override
    public UserInfo findByUser(User user) throws ServiceException {
        if (user != null) {
            UserInfo info = null;
            UserInfoDao dao = transaction.getUserInfoDao();
            try {
                info = dao.read(user);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }
            return info;
        } else {
            logger.log(Level.ERROR, "Parameter - USER is invalid");
            throw new ServiceException("Parameter - USER is invalid");
        }
    }

    @Override
    public Integer save(UserInfo userInfo) throws ServiceException {
        if (userInfo != null) {
            Integer id;
            UserInfoDao dao = transaction.getUserInfoDao();
            try {
                if (userInfo.getId() != null) {
                    id = userInfo.getId();
                    dao.update(userInfo);
                } else {
                    id = dao.create(userInfo);
                }
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
                throw new ServiceException(e);
            }
            return id;
        } else {
            logger.log(Level.ERROR, "Parameter - USER_INFO is invalid");
            throw new ServiceException("Parameter - USER_INFO is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            UserInfoDao dao = transaction.getUserInfoDao();
            try {
                dao.delete(id);
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
                throw new ServiceException(e);
            }
        } else {
            logger.log(Level.ERROR, "Parameter - ID is inalid");
            throw new ServiceException("Parameter - ID is invalid");
        }
    }
}
