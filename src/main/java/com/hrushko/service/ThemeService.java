package com.hrushko.service;

import com.hrushko.entity.Value;

import java.util.List;

public interface ThemeService extends Service {
    List<Value> findAll() throws ServiceException;
    Value findById(Integer id) throws ServiceException;
    Integer save(Value theme) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
