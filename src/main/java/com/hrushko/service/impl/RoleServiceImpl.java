package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.entity.Value;
import com.hrushko.service.RoleService;
import com.hrushko.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class RoleServiceImpl extends ServiceImpl implements RoleService {
    private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

    public RoleServiceImpl() throws ServiceException {
    }

    @Override
    public List<Value> findAll() throws ServiceException {
        List<Value> roles;
        try {
            roles = transaction.getRoleDao().read();
        } catch (DaoException e) {
            throw new ServiceException();
        }
        return roles;
    }

    @Override
    public Value findById(Integer id) throws ServiceException {
        Value value;
        if(id != null) {
            try {
                value = transaction.getRoleDao().read(id);
            } catch (DaoException e) {
                throw new ServiceException();
            }
            return value;
        } else {
            LOGGER.log(Level.ERROR, "Argument ID is invalid");
            throw new ServiceException("Argument ID is invalid");
        }
    }

    @Override
    public Integer save(Value role) throws ServiceException {
        if(role != null) {
            Integer id = null;
            try {
                if(isExist(role)) {
                    id = transaction.getRoleDao().create(role);
                    transaction.commit();
                } else {
                    LOGGER.log(Level.ERROR, "Such role already exist");
                    throw new ServiceException("Such role already exist");
                }
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException ex) {
                    throw new ServiceException(ex);
                }
            }
            return id;
        } else {
            LOGGER.log(Level.ERROR, "Argument role is invalid");
            throw new ServiceException("Argument role is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if(id != null) {
            try {
                transaction.getRoleDao().delete(id);
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException ex) {
                    throw new ServiceException(ex);
                }
            }
        } else {
            LOGGER.log(Level.ERROR, "Argument ID is invalid");
            throw new ServiceException("Argument ID is invalid");
        }
    }

    private boolean isExist(Value role) throws ServiceException {
        return findAll().contains(role);
    }
}
