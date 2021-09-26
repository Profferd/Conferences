package com.hrushko.service;

import com.hrushko.entity.Event;

import java.util.List;

public interface EventService extends Service {
    List<Event> findAll() throws ServiceException;
    Event findById(Integer id) throws ServiceException;
    Integer save(Event event) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
