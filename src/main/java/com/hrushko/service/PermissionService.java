package com.hrushko.service;

import com.hrushko.entity.Permission;

import java.util.List;

public interface PermissionService extends Service {
    List<Permission> readAll() throws ServiceException;
    Permission readById(Integer id) throws ServiceException;
    Integer save(Permission permission) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
