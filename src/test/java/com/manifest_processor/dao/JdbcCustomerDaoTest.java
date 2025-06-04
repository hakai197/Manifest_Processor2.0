package com.manifest_processor.dao;


import com.manifest_processor2.dao.JdbcCustomerDao;
import com.manifest_processor2.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcCustomerDaoTest extends BaseDaoTest {

    private JdbcCustomerDao sut;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcCustomerDao(dataSource);
    }


    @Test
    void getCustomers_returns_all_customers() {
        List<Customer> customers = sut.getCustomers();
        assertEquals(3, customers.size());
    }


    @Test
    void createCustomer_creates_and_returns_customer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("Test Customer");
        newCustomer.setAddress("123 Test St");
        newCustomer.setCity("Testville");
        newCustomer.setState("TS");
        newCustomer.setZipCode("12345");


        Customer createdCustomer = sut.createCustomer(newCustomer);
        assertNotNull(createdCustomer.getCustomerId());

        Customer retrievedCustomer = sut.getCustomerById(createdCustomer.getCustomerId());
        assertEquals("Test Customer", retrievedCustomer.getName());
    }

    @Test
    void updateCustomer_updates_customer() {
        Customer customer = sut.getCustomerById(1);
        customer.setName("Updated Name");

        Customer updatedCustomer = sut.updateCustomer(customer);
        assertEquals("Updated Name", updatedCustomer.getName());

        Customer retrievedCustomer = sut.getCustomerById(1);
        assertEquals("Updated Name", retrievedCustomer.getName());
    }

    @Test
    void deleteCustomerById_deletes_customer() {
        int rowsAffected = sut.deleteCustomerById(1);
        assertEquals(1, rowsAffected);

        Customer deletedCustomer = sut.getCustomerById(1);
        assertNull(deletedCustomer);
    }

    @Test
    void getByName_returns_correct_customer() {
        Customer customer = sut.getByName("Alec Holland");
        assertNotNull(customer);
        assertEquals(1, customer.getCustomerId());
    }


}
