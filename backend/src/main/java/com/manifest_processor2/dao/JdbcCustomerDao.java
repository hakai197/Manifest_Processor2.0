package com.manifest_processor2.dao;

import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCustomerDao implements CustomerDao {
    private static final String CUSTOMER_SELECT = "SELECT c.customer_id, c.name, c.address, " +
            "c.city, c.state, c.zip_code FROM customer c ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcCustomerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        String sql = CUSTOMER_SELECT + "WHERE c.customer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                customer = mapRowToCustomer(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing customer data", e);
        }
        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(CUSTOMER_SELECT);
            while (results.next()) {
                customers.add(mapRowToCustomer(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing customer data", e);
        }
        return customers;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, address, city, state, zip_code) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING customer_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    customer.getName(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getZipCode());
            return getCustomerById(newId);
        } catch (Exception e) {
            throw new DaoException("Error creating customer", e);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) throws DaoException {
        String sql = "UPDATE customer SET name = ?, address = ?, " +
                "city = ?, state = ?, zip_code = ? " +
                "WHERE customer_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql,
                    customer.getName(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getZipCode(),
                    customer.getCustomerId());

            if (rowsUpdated == 0) {
                return null;
            }
            return getCustomerById(customer.getCustomerId());
        } catch (Exception e) {
            throw new DaoException("Failed to update customer: " + e.getMessage());
        }
    }

    @Override
    public int deleteCustomerById(int id) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new DaoException("Error deleting customer", e);
        }
    }

    @Override
    public Customer getByName(String name) {
        Customer customer = null;
        String sql = CUSTOMER_SELECT + "WHERE LOWER(c.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            if (results.next()) {
                customer = mapRowToCustomer(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing customer data by name", e);
        }
        return customer;
    }



    private Customer mapRowToCustomer(SqlRowSet results) {
        Customer customer = new Customer();
        customer.setCustomerId(results.getInt("customer_id"));
        customer.setName(results.getString("name"));
        customer.setAddress(results.getString("address"));
        customer.setCity(results.getString("city"));
        customer.setState(results.getString("state"));
        customer.setZipCode(results.getString("zip_code"));
        return customer;
    }
}