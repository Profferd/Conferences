package com.hrushko.dao;

import com.hrushko.entity.Permission;

import java.util.List;

public interface PermissionDao extends Dao<Permission> {
    List<Permission> read() throws DaoException;
}
