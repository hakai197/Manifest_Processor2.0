package com.manifest_processor2.dao;

import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.OrderNumber;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcOrderNumberDao implements OrderNumberDao {
    private static final String ORDER_SELECT = "SELECT o.order_id, o.order_number, o.customer_id, " +
            "o.shipper_id, o.trailer_id, o.door_number, o.handling_unit, o.weight, o.status FROM Order_Number o ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderNumberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public OrderNumber getOrderById(int orderId) {
        OrderNumber order = null;
        String sql = ORDER_SELECT + "WHERE o.order_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, orderId);
            if (results.next()) {
                order = mapRowToOrderNumber(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing order data", e);
        }
        return order;
    }

    @Override
    public OrderNumber getOrderByNumber(String orderNumber) {
        OrderNumber order = null;
        String sql = ORDER_SELECT + "WHERE o.order_number = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, orderNumber);
            if (results.next()) {
                order = mapRowToOrderNumber(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing order data by order number", e);
        }
        return order;
    }

    @Override
    public List<OrderNumber> getAllOrders() {
        List<OrderNumber> orders = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(ORDER_SELECT);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing all orders", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByStatus(String status) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT + "WHERE o.status = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by status", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByCustomerName(String customerName) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "WHERE LOWER(c.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerName);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by customer name", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByTrailerNumber(String trailerNumber) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT +
                "JOIN trailer t ON o.trailer_id = t.trailer_id " +
                "WHERE t.trailer_number = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerNumber);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by trailer number", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByShipperName(String shipperName) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT +
                "JOIN shipper s ON o.shipper_id = s.shipper_id " +
                "WHERE LOWER(s.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, shipperName);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by shipper name", e);
        }
        return orders;
    }

    @Override
    public OrderNumber createOrder(OrderNumber orderNumber) {
        String sql = "INSERT INTO Order_Number (order_number, customer_id, shipper_id, " +
                "trailer_id, door_number, handling_unit, weight, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING order_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    orderNumber.getOrderNumber(),
                    orderNumber.getCustomerId(),
                    orderNumber.getShipperId(),
                    orderNumber.getTrailerId(),
                    orderNumber.getDoorNumber(),
                    orderNumber.getHandlingUnit(),
                    orderNumber.getWeight(),
                    orderNumber.getStatus()
            );
            return getOrderById(newId);
        } catch (Exception e) {
            throw new DaoException("Error creating order", e);
        }
    }

    @Override
    public boolean updateOrder(OrderNumber orderNumber) {
        String sql = "UPDATE Order_Number SET customer_id = ?, " +
                "shipper_id = ?, trailer_id = ?, door_number = ?, " +
                "handling_unit = ?, weight = ?, status = ? " +
                "WHERE order_number = ?";

        try {
            int rowsUpdated = jdbcTemplate.update(sql,
                    orderNumber.getCustomerId(),
                    orderNumber.getShipperId(),
                    orderNumber.getTrailerId(),
                    orderNumber.getDoorNumber(),
                    orderNumber.getHandlingUnit(),
                    orderNumber.getWeight(),
                    orderNumber.getStatus(),
                    orderNumber.getOrderNumber());

            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new DaoException("Error updating order by number", e);
        }
    }

    @Override
    public boolean deleteOrder(String orderNumber) {
        String sql = "DELETE FROM Order_Number WHERE order_number = ?";
        try {
            int rowsDeleted = jdbcTemplate.update(sql, orderNumber);
            return rowsDeleted == 1;
        } catch (Exception e) {
            throw new DaoException("Error deleting order by order number", e);
        }
    }

    private OrderNumber mapRowToOrderNumber(SqlRowSet results) {
        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setOrderId(results.getInt("order_id"));
        orderNumber.setOrderNumber(results.getString("order_number"));
        orderNumber.setCustomerId(results.getInt("customer_id"));
        orderNumber.setShipperId(results.getInt("shipper_id"));
        orderNumber.setTrailerId(results.getInt("trailer_id"));
        orderNumber.setDoorNumber(results.getString("door_number"));
        orderNumber.setHandlingUnit(results.getInt("handling_unit"));
        orderNumber.setWeight(results.getInt("weight"));
        orderNumber.setStatus(results.getString("status"));
        return orderNumber;
    }
}