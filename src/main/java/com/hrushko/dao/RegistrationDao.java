package com.hrushko.dao;

import com.hrushko.entity.Registration;

import java.util.List;

public interface RegistrationDao extends Dao<Registration> {
    List<Registration> readUsersOnEvent(Integer eventId) throws DaoException;
    List<Registration> readUserEvent(Integer userId) throws DaoException;
    Registration read(Integer eventId, Integer userId) throws DaoException;
}
