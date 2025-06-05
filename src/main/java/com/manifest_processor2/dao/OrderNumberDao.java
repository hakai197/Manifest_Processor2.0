package com.manifest_processor2.dao;



import com.manifest_processor2.model.OrderNumber;

import java.util.List;

public interface OrderNumberDao {
    OrderNumber getOrderById(int orderId);
    OrderNumber getOrderByNumber(String orderNumber);
    List<OrderNumber> getAllOrders();
    List<OrderNumber> getOrdersByStatus(String status);
    List<OrderNumber> getOrdersByCustomerName(String customerName);
    List<OrderNumber> getOrdersByTrailerNumber(String trailerNumber);
    List<OrderNumber> getOrdersByShipperName(String shipperName);

    List<OrderNumber> getOrdersByCustomerId(int customerId);

    List<OrderNumber> getOrdersByTrailerId(int trailerId);

    List<OrderNumber> getOrdersByShipperId(int shipperId);

    List<OrderNumber> getOrdersByDoorId(int doorId);

    OrderNumber createOrder(OrderNumber orderNumber);
    boolean updateOrder(OrderNumber orderNumber);
    boolean deleteOrder(String orderNumber);

    boolean deleteOrder(int orderId);

    boolean assignOrderToDoor(int orderId, int doorId);

    boolean unassignOrder(int orderId);
}