package com.hrushko.dao.impl;

import com.hrushko.dao.AddressDao;
import com.hrushko.dao.DaoException;
import com.hrushko.entity.Address;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressDaoImpl extends BaseDaoImpl implements AddressDao {

    private static final String READ_ALL = "SELECT `id`, `country`, `city`, `street`, `building` FROM `address` ORDER BY `country`, `city`, `street`, `building`";
    private static final String CREATE = "INSERT INTO `address` (`country`, `city`, `street`, `building`) VALUE (?, ?, ?, ?)";
    private static final String READ = "SELECT `country`, `city`, `street`, `building` FROM `address` WHERE `id` = ?";
    private static final String UPDATE = "UPDATE `address` SET `country` = ?, `city` = ?, `street` = ?, `building` = ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `address` WHERE `id` = ?";
    private static final Logger LOGGER = LogManager.getLogger(AddressDaoImpl.class);

    @Override
    public List<Address> read() throws DaoException {
        ResultSet resultSet = null;
        try(PreparedStatement statement = connection.prepareStatement(READ_ALL)) {
            resultSet = statement.executeQuery();
            List<Address> addresses = new ArrayList<>();
            Address address = null;
            while(resultSet.next()) {
                address = new Address(resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("street"),
                        resultSet.getString("building"));
                address.setId(resultSet.getInt("id"));
                addresses.add(address);
            }
            return addresses;
        } catch(SQLException e) {
            LOGGER.log(Level.ERROR, "Can't readById all users");
            throw new DaoException(e + "Can't readById all users");
        } finally {
            try {
                if (resultSet == null) {
                    throw new DaoException();
                }
                resultSet.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public Integer create(Address entity) throws DaoException {
        ResultSet resultSet = null;
        try(PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getCountry());
            statement.setString(2, entity.getCity());
            statement.setString(3, entity.getStreet());
            statement.setString(4, entity.getBuilding());
            statement.executeUpdate();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't create new address");
            throw new DaoException(e + "Can't creat new address");
        } finally {
            try {
                if (resultSet == null) {
                    throw new DaoException();
                }
                resultSet.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public Address read(Integer id) throws DaoException {
        ResultSet resultSet = null;
        try(PreparedStatement statement = connection.prepareStatement(READ)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Address address = null;
            if(resultSet.next()) {
                address = new Address(resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("street"),
                        resultSet.getString("building"));
                address.setId(id);
            }
            return address;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't readById address by id" + id);
            throw new DaoException("Can't readById address by id = " + id);
        } finally {
            try {
            if (resultSet == null) {
                throw new DaoException();
            }
                resultSet.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public void update(Address entity) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, entity.getCountry());
            preparedStatement.setString(2, entity.getCity());
            preparedStatement.setString(3, entity.getStreet());
            preparedStatement.setString(4, entity.getBuilding());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            LOGGER.log(Level.ERROR, "Can't update user");
            throw new DaoException(e + "Can't update user");
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            LOGGER.log(Level.ERROR, "Can't delete address with id = " + id);
            throw new DaoException("Can't delete address with id = " + id);
        }
    }
}
