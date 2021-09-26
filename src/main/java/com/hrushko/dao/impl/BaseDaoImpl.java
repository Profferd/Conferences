package com.hrushko.dao.impl;

import java.sql.Connection;

abstract class BaseDaoImpl {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
