package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.dao.RegistrationDao;
import com.hrushko.entity.Event;
import com.hrushko.entity.Registration;
import com.hrushko.entity.User;
import com.hrushko.entity.Value;
import com.hrushko.service.*;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RegistrationServiceImpl extends ServiceImpl implements RegistrationService {
    private static final Logger LOGGER = LogManager.getLogger(RegistrationServiceImpl.class);

    public RegistrationServiceImpl() throws ServiceException {
    }

    @Override
    public List<User> findUsersOnEvent(Integer eventId) throws ServiceException {
        if (eventId >= 0) {
            List<Registration> registrations = null;
            List<User> users = new ArrayList<>();
            RegistrationDao registrationDao = transaction.getRegistrationDao();
            UserService userService = new UserServiceImpl();
            try {
                registrations = registrationDao.readUsersOnEvent(eventId);
                for (Registration registration : registrations){
                    User user = userService.findById(registration.getUserId());
                    users.add(user);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            }

            return users;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - EVENT_ID is inalid");
            throw new ServiceException("Parameter - EVENT_ID is invalid");
        }
    }

    @Override
    public List<Event> findUserEvents(Integer userId) throws ServiceException {
        if (userId >= 0) {
            List<Registration> registrations = null;
            List<Event> events = new ArrayList<>();
            RegistrationDao registrationDao = transaction.getRegistrationDao();
            EventService eventService = new EventServiceImpl();
            try {
                registrations = registrationDao.readUserEvent(userId);
                for (Registration registration : registrations){
                    Event event = eventService.findById(registration.getEventId());
                    events.add(event);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            }

            return events;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - USER_ID is inalid");
            throw new ServiceException("Parameter - USER_ID is invalid");
        }
    }

    @Override
    public Registration read(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            Registration registration = null;
            RegistrationDao dao = transaction.getRegistrationDao();
            try {
                registration = dao.read(id);
                setRole(registration);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }

            return registration;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - ID is inalid");
            throw new ServiceException("Parameter - ID is invalid");
        }

    }

    @Override
    public Integer save(Registration registration) throws ServiceException {
        Integer id;
        if (registration != null) {
            RegistrationDao dao = transaction.getRegistrationDao();
            try {
                if (registration.getId() != null) {
                    id = registration.getId();
                    dao.update(registration);
                } else {
                    id = dao.create(registration);
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
            LOGGER.log(Level.ERROR, "Parameter - REGISTRATION is invalid");
            throw new ServiceException("Parameter - REGISTRATION is invalid");
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        if (id != null && id > 0) {
            RegistrationDao dao = transaction.getRegistrationDao();
            try {
                dao.delete(id);
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
            LOGGER.log(Level.ERROR, "Parameter - ID is inalid");
            throw new ServiceException("Parameter - ID is invalid");
        }
    }

    @Override
    public Registration readByUserAndEvent(Integer eventId, Integer userId) throws ServiceException {
        if (eventId >= 0 && userId >= 0){
            Registration registration = null;
            RegistrationDao dao = transaction.getRegistrationDao();
            try {
                registration = dao.read(eventId, userId);
            } catch (DaoException e) {
                throw new ServiceException(e);
            }

            return registration;
        } else {
            LOGGER.log(Level.ERROR, "Parameter - userId or eventId is inalid");
            throw new ServiceException("Parameter - userId or eventId is invalid");
        }
    }

    private void setRole(Registration registration) throws ServiceException {
        RoleService service = new RoleServiceImpl();
        Value role = service.findById(registration.getRole().getId());
        registration.setRole(role);
    }
}
