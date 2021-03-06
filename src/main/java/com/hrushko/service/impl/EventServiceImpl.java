package com.hrushko.service.impl;

import com.hrushko.dao.AddressDao;
import com.hrushko.dao.DaoException;
import com.hrushko.dao.EventDao;
import com.hrushko.entity.Address;
import com.hrushko.entity.Event;
import com.hrushko.entity.Value;
import com.hrushko.service.EventService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.ThemeService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class EventServiceImpl extends ServiceImpl implements EventService {
    private static final Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    public EventServiceImpl() throws ServiceException {
    }

    @Override
    public List<Event> findAll() throws ServiceException {
        List<Event> events = null;
        EventDao dao = transaction.getEventDao();
        try {
            events = dao.read();
            for (Event event : events) {
                setTheme(event);
                setDate(event);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return events;
    }

    @Override
    public Event findById(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            Event event = null;
            EventDao dao = transaction.getEventDao();
            try {
                event = dao.read(id);
                if (event == null)
                    return event;
                setTheme(event);
                setDate(event);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }

            return event;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - ID is inalid");
            throw new ServiceException("Parameter - ID is invalid");
        }
    }

    @Override
    public Integer save(Event event) throws ServiceException {
        if (event != null) {
            Integer id;
            EventDao dao = transaction.getEventDao();
            AddressDao addressDao = transaction.getAddressDao();
            try {
                if (event.getId() != null) {
                    id = event.getId();
                    dao.update(event);
                    addressDao.update(event.getAddress());
                } else {
                    event.getAddress().setId(addressDao.create(event.getAddress()));
                    id = dao.create(event);
                }
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
                throw new ServiceException(e);
            }
            return id;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - EVENT is invalid");
            throw new ServiceException("Parameter - EVENT is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            EventDao dao = transaction.getEventDao();
            try {
                Event event = dao.read(id);
                AddressDao addressDao = transaction.getAddressDao();
                dao.delete(id);
                addressDao.delete(event.getAddress().getId());
                transaction.commit();
            } catch (DaoException e) {
                try {
                    transaction.rollback();
                } catch (DaoException e1) {
                    throw new ServiceException(e1);
                }
                throw new ServiceException(e);
            }
        } else {
            LOGGER.log(Level.ERROR, "Parameter ID is invalid");
            throw new ServiceException("Parameter ID is invalid");
        }
    }

    private void setTheme(Event event) throws ServiceException {
        ThemeService service = new ThemeServiceImpl();
        Value theme = service.findById(event.getTheme().getId());
        event.setTheme(theme);
    }

    private void setDate(Event event) throws DaoException {
        AddressDao addressDao = transaction.getAddressDao();
        Address address = addressDao.read(event.getAddress().getId());
        event.setAddress(address);
    }
}
