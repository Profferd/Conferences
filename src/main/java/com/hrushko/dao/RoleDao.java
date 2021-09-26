package com.hrushko.dao;

import com.hrushko.entity.Value;

import java.util.List;

public interface RoleDao extends Dao<Value> {
    List<Value> read() throws DaoException;
}
