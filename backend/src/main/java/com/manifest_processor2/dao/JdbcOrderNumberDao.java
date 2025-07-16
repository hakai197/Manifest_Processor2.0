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
            "o.shipper_id, o.trailer_id, o.door_id, o.handling_unit, o.weight, o.status FROM Order_Number o ";
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
            SqlRowSet results = jdbcTemplate.queryForRowSet(ORDER_SELECT + "ORDER BY o.order_id");
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
        String sql = ORDER_SELECT + "WHERE o.status = ? ORDER BY o.order_id";
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
        return List.of();
    }

    @Override
    public List<OrderNumber> getOrdersByTrailerNumber(String trailerNumber) {
        return List.of();
    }

    @Override
    public List<OrderNumber> getOrdersByShipperName(String shipperName) {
        return List.of();
    }

    @Override
    public List<OrderNumber> getOrdersByCustomerId(int customerId) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT + "WHERE o.customer_id = ? ORDER BY o.order_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by customer ID", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByTrailerId(int trailerId) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT + "WHERE o.trailer_id = ? ORDER BY o.order_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerId);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by trailer ID", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByShipperId(int shipperId) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT + "WHERE o.shipper_id = ? ORDER BY o.order_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, shipperId);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by shipper ID", e);
        }
        return orders;
    }

    @Override
    public List<OrderNumber> getOrdersByDoorId(int doorId) {
        List<OrderNumber> orders = new ArrayList<>();
        String sql = ORDER_SELECT + "WHERE o.door_id = ? ORDER BY o.order_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, doorId);
            while (results.next()) {
                orders.add(mapRowToOrderNumber(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing orders by door ID", e);
        }
        return orders;
    }

    @Override
    public OrderNumber createOrder(OrderNumber orderNumber) {
        String sql = "INSERT INTO Order_Number (order_number, customer_id, shipper_id, " +
                "trailer_id, door_id, handling_unit, weight, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING order_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    orderNumber.getOrderNumber(),
                    orderNumber.getCustomerId(),
                    orderNumber.getShipperId(),
                    orderNumber.getTrailerId(),
                    orderNumber.getDoorId(),
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
        String sql = "UPDATE Order_Number SET order_number = ?, customer_id = ?, " +
                "shipper_id = ?, trailer_id = ?, door_id = ?, " +
                "handling_unit = ?, weight = ?, status = ? " +
                "WHERE order_id = ?";

        try {
            int rowsUpdated = jdbcTemplate.update(sql,
                    orderNumber.getOrderNumber(),
                    orderNumber.getCustomerId(),
                    orderNumber.getShipperId(),
                    orderNumber.getTrailerId(),
                    orderNumber.getDoorId(),
                    orderNumber.getHandlingUnit(),
                    orderNumber.getWeight(),
                    orderNumber.getStatus(),
                    orderNumber.getOrderId());

            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new DaoException("Error updating order", e);
        }
    }

    @Override
    public boolean deleteOrder(String orderNumber) {
        return false;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM Order_Number WHERE order_id = ?";
        try {
            int rowsDeleted = jdbcTemplate.update(sql, orderId);
            return rowsDeleted == 1;
        } catch (Exception e) {
            throw new DaoException("Error deleting order by ID", e);
        }
    }

    @Override
    public boolean assignOrderToDoor(int orderId, int doorId) {
        String sql = "UPDATE Order_Number SET door_id = ?, status = 'Assigned' WHERE order_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql, doorId, orderId);
            return rowsUpdated == 1;
        } catch (Exception e) {
            throw new DaoException("Error assigning order to door", e);
        }
    }

    @Override
    public boolean unassignOrder(int orderId) {
        String sql = "UPDATE Order_Number SET door_id = NULL, status = 'Unassigned' WHERE order_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql, orderId);
            return rowsUpdated == 1;
        } catch (Exception e) {
            throw new DaoException("Error unassigning order", e);
        }
    }

    private OrderNumber mapRowToOrderNumber(SqlRowSet results) {
        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setOrderId(results.getInt("order_id"));
        orderNumber.setOrderNumber(results.getString("order_number"));
        orderNumber.setCustomerId(results.getInt("customer_id"));
        orderNumber.setShipperId(results.getInt("shipper_id"));
        orderNumber.setTrailerId(results.getInt("trailer_id"));
        orderNumber.setDoorId(results.getInt("door_id"));
        orderNumber.setHandlingUnit(results.getInt("handling_unit"));
        orderNumber.setWeight(results.getInt("weight"));
        orderNumber.setStatus(results.getString("status"));
        return orderNumber;
    }
}