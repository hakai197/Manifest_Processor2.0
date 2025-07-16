package com.manifest_processor.dao;



import com.manifest_processor2.dao.JdbcOrderNumberDao;
import com.manifest_processor2.model.OrderNumber;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcOrderNumberDaoTest extends BaseDaoTest {

    private JdbcTemplate jdbcTemplate;

    private JdbcOrderNumberDao dao;


    @Test
    void getOrderById_returns_correct_order() {
        OrderNumber order = dao.getOrderById(1);
        assertNotNull(order);
        assertEquals("TESTORDER1", order.getOrderNumber());
        assertEquals(1, order.getCustomerId());
        assertEquals(1, order.getShipperId());
        assertEquals(1, order.getTrailerId());
        assertEquals("Door 1", order.getDoorNumber());
        assertEquals(5, order.getHandlingUnit());
        assertEquals(100, order.getWeight());
        assertEquals("Pending", order.getStatus());
    }

    @Test
    void getOrderById_with_invalid_id_returns_null() {
        OrderNumber order = dao.getOrderById(999);
        assertNull(order);
    }

    @Test
    void getOrderByNumber_returns_correct_order() {
        OrderNumber order = dao.getOrderByNumber("TESTORDER2");
        assertNotNull(order);
        assertEquals(2, order.getOrderId());
        assertEquals("TESTORDER2", order.getOrderNumber());
        assertEquals("Shipped", order.getStatus());
    }

    @Test
    void getOrderByNumber_with_invalid_number_returns_null() {
        OrderNumber order = dao.getOrderByNumber("INVALID");
        assertNull(order);
    }

    @Test
    void getAllOrders_returns_all_orders() {
        List<OrderNumber> orders = dao.getAllOrders();
        assertEquals(3, orders.size());
    }

    @Test
    void getOrdersByStatus_returns_correct_orders() {
        List<OrderNumber> pendingOrders = dao.getOrdersByStatus("Pending");
        assertEquals(2, pendingOrders.size());

        List<OrderNumber> shippedOrders = dao.getOrdersByStatus("Shipped");
        assertEquals(1, shippedOrders.size());
    }

    @Test
    void getOrdersByCustomerName_returns_correct_orders() {
        List<OrderNumber> orders = dao.getOrdersByCustomerName("Test Customer");
        assertEquals(3, orders.size());
    }

    @Test
    void getOrdersByTrailerNumber_returns_correct_orders() {
        List<OrderNumber> orders = dao.getOrdersByTrailerNumber("TEST123");
        assertEquals(3, orders.size());
    }

    @Test
    void getOrdersByShipperName_returns_correct_orders() {
        List<OrderNumber> orders = dao.getOrdersByShipperName("Test Shipper");
        assertEquals(3, orders.size());
    }

    @Test
    void createOrder_creates_and_returns_order() {
        OrderNumber newOrder = new OrderNumber();
        newOrder.setOrderNumber("NEWORDER");
        newOrder.setCustomerId(1);
        newOrder.setShipperId(1);
        newOrder.setTrailerId(1);
        newOrder.setDoorNumber("Door 4");
        newOrder.setHandlingUnit(20);
        newOrder.setWeight(400);
        newOrder.setStatus("Pending");

        OrderNumber createdOrder = dao.createOrder(newOrder);
        assertNotNull(createdOrder);
        assertTrue(createdOrder.getOrderId() > 0);
        assertEquals("NEWORDER", createdOrder.getOrderNumber());

        OrderNumber retrievedOrder = dao.getOrderById(createdOrder.getOrderId());
        assertNotNull(retrievedOrder);
        assertEquals(createdOrder.getOrderNumber(), retrievedOrder.getOrderNumber());
    }

    @Test
    void updateOrder_updates_order_successfully() {
        OrderNumber order = dao.getOrderById(1);
        order.setDoorNumber("Updated Door");
        order.setHandlingUnit(25);
        order.setWeight(500);
        order.setStatus("Shipped");

        boolean updated = dao.updateOrder(order);
        assertTrue(updated);

        OrderNumber updatedOrder = dao.getOrderById(1);
        assertEquals("Updated Door", updatedOrder.getDoorNumber());
        assertEquals(25, updatedOrder.getHandlingUnit());
        assertEquals(500, updatedOrder.getWeight());
        assertEquals("Shipped", updatedOrder.getStatus());
    }

    @Test
    void updateOrder_with_invalid_order_number_returns_false() {
        OrderNumber invalidOrder = new OrderNumber();
        invalidOrder.setOrderNumber("INVALID");

        boolean updated = dao.updateOrder(invalidOrder);
        assertFalse(updated);
    }

    @Test
    void deleteOrder_deletes_order_successfully() {
        boolean deleted = dao.deleteOrder("TESTORDER3");
        assertTrue(deleted);

        OrderNumber order = dao.getOrderByNumber("TESTORDER3");
        assertNull(order);
    }

    @Test
    void deleteOrder_with_invalid_order_number_returns_false() {
        boolean deleted = dao.deleteOrder("INVALID");
        assertFalse(deleted);
    }
}