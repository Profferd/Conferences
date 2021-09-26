package com.hrushko.dao;

import com.hrushko.entity.Event;
import com.hrushko.entity.Value;

import java.util.Date;
import java.util.List;

public interface EventDao extends Dao<Event> {
    List<Event> read() throws DaoException;
    List<Event> readByName(String search) throws DaoException;
    List<Event> readByDate(Date search) throws DaoException;
    List<Event> readByTheme(Value search) throws DaoException;
}
