package com.hrushko.service;

import com.hrushko.entity.Event;
import com.hrushko.entity.Permission;
import com.hrushko.entity.Registration;
import com.hrushko.entity.User;

import java.util.List;

public interface RegistrationService extends Service {
    List<User> findUsersOnEvent(Integer eventId) throws ServiceException;
    List<Event> findUserEvents(Integer eventId) throws ServiceException;
    Registration read(Integer id) throws ServiceException;
    Integer save(Registration registration) throws ServiceException;
    void delete(Integer id) throws ServiceException;
    Registration readByUserAndEvent(Integer eventId, Integer userId) throws ServiceException;
}
