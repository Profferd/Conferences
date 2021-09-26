package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.dao.PermissionDao;
import com.hrushko.entity.Permission;
import com.hrushko.service.PermissionService;
import com.hrushko.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class PermissionServiceImpl extends ServiceImpl implements PermissionService {
    private static final Logger LOGGER = LogManager.getLogger(PermissionServiceImpl.class);

    public PermissionServiceImpl() throws ServiceException {
    }

    @Override
    public List<Permission> readAll() throws ServiceException {
        List<Permission> permissions;
        try {
            permissions = transaction.getPermissionDao().read();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return permissions;
    }

    @Override
    public Permission readById(Integer id) throws ServiceException {
        Permission permission;
        if (id != null) {
            try {
                permission = transaction.getPermissionDao().read(id);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }
            return permission;
        } else {
            LOGGER.log(Level.ERROR, "Argument ID is invalid");
            throw new ServiceException("Argument ID is invalid");
        }
    }

    @Override
    public Integer save(Permission permission) throws ServiceException {
        Integer id;
        if (permission != null) {
            PermissionDao dao = transaction.getPermissionDao();
            try {
                if (isExist(permission)) {
                    if (permission.getId() != null) {
                        id = permission.getId();
                        dao.update(permission);
                    } else {
                        id = dao.create(permission);
                    }
                    transaction.commit();
                } else {
                    LOGGER.log(Level.ERROR, "Such permission already exist");
                    throw new ServiceException("Such permission already exist");
                }
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException ex) {
                    throw new ServiceException(ex);
                }
                throw new ServiceException(e);
            }
            return id;
        } else {
            LOGGER.log(Level.ERROR, "Parameter PERMISSION is invalid");
            throw new ServiceException("Parameter PERMISSION is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null) {
            try {
                transaction.getPermissionDao().delete(id);
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

    private boolean isExist(Permission permission) throws ServiceException {
        return readAll().contains(permission);
    }
}
