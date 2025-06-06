package com.manifest_processor2.controller;


import com.manifest_processor2.dao.OrderNumberDao;
import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.OrderNumber;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderNumberController {

    private final OrderNumberDao orderNumberDao;

    public OrderNumberController(OrderNumberDao orderNumberDao) {
        this.orderNumberDao = orderNumberDao;
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<OrderNumber> getAllOrders() {
        try {
            return orderNumberDao.getAllOrders();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/number/{orderNumber}")
    public OrderNumber getOrderByNumber(@PathVariable String orderNumber) {
        try {
            OrderNumber order = orderNumberDao.getOrderByNumber(orderNumber);
            if (order == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }
            return order;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving order");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{trailerNumber}/orders")
    public List<OrderNumber> getOrdersByTrailerNumber(@PathVariable String trailerNumber) {
        try {
            List<OrderNumber> orders = orderNumberDao.getOrdersByTrailerNumber(trailerNumber);
            if (orders.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No orders found for trailer");
            }
            return orders;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/customers/{customerName}")
    public List<OrderNumber>getOrdersByCustomerName(@PathVariable String customerName) {
        try {
            List<OrderNumber> orders = orderNumberDao.getOrdersByCustomerName(customerName);
            if (orders.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No orders found for customer");
            }
            return orders;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/shippers/{shipperName}")
    public List<OrderNumber> getOrdersByShipperName(@PathVariable String shipperName) {
        try {
            List<OrderNumber> orders = orderNumberDao.getOrdersByShipperName(shipperName);
            if (orders.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No orders found for shipper");
            }
            return orders;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/status/{status}")
    public List<OrderNumber>getOrdersbyStatus(@PathVariable String status){
        try{
            List<OrderNumber>orders = orderNumberDao.getOrdersByStatus(status);
            if (orders.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to locate orders.");
            }
            return orders;
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderNumber createOrder(@RequestBody OrderNumber orderNumber) {
        try {
            return orderNumberDao.createOrder(orderNumber);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating order");
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/orderNumber/{orderNumber}")
    public OrderNumber updateOrderByNumber(
            @PathVariable String orderNumber,
            @RequestBody OrderNumber updatedOrder) {
        try {
            updatedOrder.setOrderNumber(orderNumber);

            boolean updated = orderNumberDao.updateOrder(updatedOrder);

            if (!updated) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }

            OrderNumber result = orderNumberDao.getOrderByNumber(orderNumber);
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found after update");
            }
            return result;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating order");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/orderNumbers/{orderNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderNumber) {
        try {
            boolean deleted = orderNumberDao.deleteOrder(orderNumber);
            if (!deleted) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting order");
        }
    }
}
