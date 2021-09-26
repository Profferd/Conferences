package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.entity.Value;
import com.hrushko.service.ServiceException;
import com.hrushko.service.ThemeService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class ThemeServiceImpl extends ServiceImpl implements ThemeService {
    private static final Logger LOGGER = LogManager.getLogger(ThemeServiceImpl.class);

    public ThemeServiceImpl() throws ServiceException {
    }

    @Override
    public List<Value> findAll() throws ServiceException {
        List<Value> themes = null;
        try {
            themes = transaction.getThemeDao().read();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return themes;
    }

    @Override
    public Value findById(Integer id) throws ServiceException {
        Value value;
        if (id != null) {
            try {
                value = transaction.getThemeDao().read(id);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }
            return value;
        } else {
            LOGGER.log(Level.ERROR, "Argument ID is invalid");
            throw new ServiceException("Argument ID is invalid");
        }
    }

    @Override
    public Integer save(Value theme) throws ServiceException {
        if (theme != null) {
            Integer id = null;
            try {
                if (!isExist(theme)){
                    id = transaction.getThemeDao().create(theme);
                    transaction.commit();
                } else {
                    LOGGER.log(Level.ERROR, "such theme already exist");
                    throw new ServiceException("error.theme.exist");
                }
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
            }
            return id;
        } else {
            LOGGER.log(Level.ERROR, "Argument THEME is invalid");
            throw new ServiceException("Argument THEME is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null) {
            try {
                transaction.getThemeDao().delete(id);
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
            }
        } else {
            LOGGER.log(Level.ERROR, "Argument - ID is invalid");
            throw new ServiceException("Argument - ID is invalid");
        }
    }

    private boolean isExist(Value theme) throws ServiceException{
        return findAll().contains(theme);
    }
}
