package com.hrushko.dao;

import com.hrushko.entity.Entity;
import com.hrushko.service.ServiceException;

public interface Dao<Type extends Entity> {
    Integer create(Type entity) throws DaoException;
    Type read(Integer id) throws DaoException;
    void update(Type Entity) throws DaoException;
    void delete(Integer id) throws DaoException, ServiceException;
}
