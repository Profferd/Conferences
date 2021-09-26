package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.dao.UserDao;
import com.hrushko.entity.Permission;
import com.hrushko.entity.User;
import com.hrushko.service.PermissionService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class UserServiceImpl extends ServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl() throws ServiceException {
    }

    @Override
    public List<User> findAll() throws ServiceException {
        List<User> users = null;
        UserDao dao = transaction.getUserDao();
        try {
            users = dao.read();
            readPermission(users);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
        return users;
    }

    @Override
    public User findById(Integer id) throws ServiceException {
        if(id != null && id > 0) {
            User user = null;
            UserDao dao = transaction.getUserDao();
            try {
                user = dao.read(id);
                if (user != null) {
                    readPermission(user);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            }
            return user;
        } else {
            LOGGER.log(Level.ERROR, "Parameter ID is invalid");
            throw new ServiceException("Parameter ID is invalid");
        }
    }

    @Override
    public boolean isExist(String login) throws ServiceException {
        Integer id;
        if (login != null) {
            UserDao dao = transaction.getUserDao();
            try {
                id = dao.find(login);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }
            return id != null;
        } else {
            LOGGER.log(Level.ERROR, "Parameter login is invalid");
            throw new ServiceException("Parameter login is invalid");
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) throws ServiceException {
        if (login != null && !login.equals("")) {
            if (password != null && !password.equals("")) {
                User user = null;
                UserDao dao = transaction.getUserDao();
                try {
                    user = dao.read(login, DigestUtils.md5Hex(password));
                    if (user != null) {
                        readPermission(user);
                    } else {
                        throw new ServiceException("user.find.error");
                    }
                } catch (DaoException e) {
                    throw new ServiceException(e);
                }
                return user;
            } else {
                LOGGER.log(Level.ERROR, "Parameter password is invalid");
                throw new ServiceException("Parameter password is invalid");
            }
        } else {
            LOGGER.log(Level.ERROR, "Parameter login is invalid");
            throw new ServiceException("Parameter login is invalid");
        }
    }

    @Override
    public Integer save(User user) throws ServiceException {
        Integer id;
        if (user != null) {
            UserDao dao = transaction.getUserDao();
            try {
                if (user.getId() != null) {
                    id = user.getId();
                    if (user.getPassword() == null) {
                        user.setPassword(dao.read(user.getId()).getPassword());
                    }
                    dao.update(user);
                } else {
                    id = dao.create(user);
                }
                transaction.commit();
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
            LOGGER.log(Level.ERROR, "Parameter user is invalid");
            throw new ServiceException("Parameter user is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            UserDao dao = transaction.getUserDao();
            try {
                dao.delete(id);
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException ex) {
                    throw new ServiceException(ex);
                }
                throw new ServiceException(e);
            }
        } else {
            LOGGER.log(Level.ERROR, "Parameter ID is invalid");
            throw new ServiceException("Parameter ID is invalid");
        }
    }

    private void readPermission(List<User> users) throws ServiceException {
        for (User user:
             users) {
            readPermission(user);
        }
    }

    private void readPermission(User user) throws ServiceException {
        PermissionService service = new PermissionServiceImpl();
        Permission permission = service.readById(user.getPermission().getId());
        user.setPermission(permission);
    }
}
