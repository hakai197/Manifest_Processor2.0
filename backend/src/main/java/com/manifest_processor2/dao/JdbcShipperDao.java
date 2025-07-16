package com.manifest_processor2.dao;


import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.Shipper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcShipperDao implements ShipperDao {
    //Switch to mapper
    private static final String SHIPPER_SELECT = "SELECT s.shipper_id, s.name, s.address, s.city, s.state, s.zip_code FROM shipper s ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcShipperDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Shipper getShipperById(int id) {
        Shipper shipper = null;
        String sql = SHIPPER_SELECT + "WHERE s.shipper_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                shipper = mapRowToShipper(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing shipper data", e);
        }
        return shipper;
    }

    @Override
    public List<Shipper> getShippers() {
        List<Shipper> shippers = new ArrayList<>();
        String sql = SHIPPER_SELECT;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                shippers.add(mapRowToShipper(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing shipper data", e);
        }
        return shippers;
    }

    @Override
    public Shipper createShipper(Shipper shipper) {
        String sql = "INSERT INTO shipper (name, address, city, state, zip_code) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING shipper_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    shipper.getName(), shipper.getAddress(), shipper.getCity(),
                    shipper.getState(), shipper.getZipCode());
            if (newId != null) {
                return getShipperById(newId);
            }
        } catch (Exception e) {
            throw new DaoException("Error creating shipper", e);
        }
        return null;
    }

    @Override
    public Shipper updateShipper(Shipper shipper) {
        String sql = "UPDATE shipper SET name = ?, address = ?, city = ?, " +
                "state = ?, zip_code = ? WHERE shipper_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, shipper.getName(),
                    shipper.getAddress(), shipper.getCity(), shipper.getState(),
                    shipper.getZipCode(), shipper.getShipperId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getShipperById(shipper.getShipperId());
        } catch (Exception e) {
            throw new DaoException("Error updating shipper", e);
        }
    }

    @Override
    public int deleteShipperById(int id) {
        String nullifyShipperSql = "UPDATE trailer SET shipper_id = NULL WHERE shipper_id = ?";
        String deleteShipperSql = "DELETE FROM shipper WHERE shipper_id = ?";

        try {

            jdbcTemplate.update(nullifyShipperSql, id);


            return jdbcTemplate.update(deleteShipperSql, id);

        } catch (Exception e) {
            throw new DaoException("Error deleting shipper", e);
        }
    }

    @Override
    public Shipper getByName(String name) {
        Shipper shipper = null;
        String sql = SHIPPER_SELECT + "WHERE LOWER(s.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            if (results.next()) {
                shipper = mapRowToShipper(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing shipper data by name", e);
        }
        return shipper;
    }


    private Shipper mapRowToShipper(SqlRowSet results) {
        Shipper shipper = new Shipper();
        shipper.setShipperId(results.getInt("shipper_id"));
        shipper.setName(results.getString("name"));
        shipper.setAddress(results.getString("address"));
        shipper.setCity(results.getString("city"));
        shipper.setState(results.getString("state"));
        shipper.setZipCode(results.getString("zip_code"));
        return shipper;
    }
}
