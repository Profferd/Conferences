package com.hrushko.dao.impl;

import com.hrushko.connection.ConnectionPool;
import com.hrushko.connection.PoolException;
import com.hrushko.dao.DaoException;
import com.hrushko.dao.TransactionFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The type Transaction factory.
 */
public class TransactionFactoryImpl implements TransactionFactory {
    private static final Logger logger = LogManager.getLogger(TransactionFactoryImpl.class);
    private static TransactionFactoryImpl instance;
    private Connection connection;


    private TransactionFactoryImpl() throws DaoException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);

        } catch (SQLException | PoolException e) {
            logger.log(Level.ERROR, "Can't initialize transactions");
            throw new DaoException(e + "Can't initialize transactions");
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     * @throws DaoException the dao exception
     */
    public static TransactionFactoryImpl getInstance() throws DaoException {
        if (instance == null) {
            instance = new TransactionFactoryImpl();
        }
        return instance;
    }

    @Override
    public TransactionImpl getTransaction() {
        return new TransactionImpl(connection);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}

