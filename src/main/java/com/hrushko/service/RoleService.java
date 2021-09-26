package com.hrushko.service;

import com.hrushko.entity.Value;

import java.util.List;

public interface RoleService extends Service {
    List<Value> findAll() throws ServiceException;
    Value findById(Integer id) throws ServiceException;
    Integer save(Value role) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
