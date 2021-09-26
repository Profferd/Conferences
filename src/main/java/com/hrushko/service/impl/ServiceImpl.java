package com.hrushko.service.impl;

import com.hrushko.dao.DaoException;
import com.hrushko.dao.Transaction;
import com.hrushko.dao.impl.TransactionFactoryImpl;
import com.hrushko.service.Service;
import com.hrushko.service.ServiceException;

public class ServiceImpl implements Service {
    Transaction transaction;

    public ServiceImpl() throws ServiceException {
        try {
            this.transaction = TransactionFactoryImpl.getInstance().getTransaction();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
