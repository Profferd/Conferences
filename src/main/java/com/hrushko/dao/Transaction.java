package com.hrushko.dao;

public interface Transaction {
    UserDao getUserDao();
    EventDao getEventDao();
    UserInfoDao getUserInfoDao();
    RegistrationDao getRegistrationDao();
    AddressDao getAddressDao();
    PermissionDao getPermissionDao();
    ThemeDao getThemeDao();
    RoleDao getRoleDao();
    void commit() throws DaoException;
    void rollback() throws DaoException;
}
