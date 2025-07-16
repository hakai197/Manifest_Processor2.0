package com.manifest_processor2.controller;

import com.manifest_processor2.dao.CustomerDao;
import com.manifest_processor2.model.Customer;
import com.manifest_processor2.model.DaoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path= "/api/customers")
public class CustomerController {
    private final CustomerDao customerDao;

    public CustomerController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        try {
            return customerDao.getCustomers();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find customers: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable int id) {
        try {
            Customer customer = customerDao.getCustomerById(id);
            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }
            return customer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving customer: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/name/{name}")
    public Customer getCustomerByName(@PathVariable String name) {
        try {
            Customer customer = customerDao.getByName(name);
            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with name: " + name);
            }
            return customer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving customer: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody Customer customer) {
        if (customer.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        try {
            return customerDao.createCustomer(customer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating customer: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        try {
            customer.setCustomerId(id);
            Customer updatedCustomer = customerDao.updateCustomer(customer);
            if (updatedCustomer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }
            return updatedCustomer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating customer: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable int id) {
        try {
            int rowsAffected = customerDao.deleteCustomerById(id);
            if (rowsAffected == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting customer: " + e.getMessage());
        }
    }
}