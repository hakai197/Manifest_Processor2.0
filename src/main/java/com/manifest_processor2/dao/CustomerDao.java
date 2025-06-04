package com.manifest_processor2.dao;



import com.manifest_processor2.model.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerById(int id);
    List<Customer> getCustomers();
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    int deleteCustomerById(int id);
    Customer getByName(String name);


}
