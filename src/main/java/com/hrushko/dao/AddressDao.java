package com.hrushko.dao;

import com.hrushko.entity.Address;

import java.util.List;

public interface AddressDao extends Dao<Address> {
    List<Address> read() throws DaoException;
}
