package com.hrushko.dao;

import com.hrushko.dao.impl.TransactionImpl;

import java.sql.Connection;

public interface TransactionFactory {
    TransactionImpl getTransaction();
    Connection getConnection();
}
