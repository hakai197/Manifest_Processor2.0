package com.manifest_processor2.dao;

import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.Unloader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUnloaderDao implements UnloaderDao {
    // Updated to include new fields
    private static final String UNLOADER_SELECT = "SELECT u.employee_id, u.name, u.shift, u.employee_number, u.status, u.door_id FROM unloader u ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcUnloaderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Unloader getUnloaderById(int id) {
        Unloader unloader = null;
        String sql = UNLOADER_SELECT + "WHERE u.employee_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                unloader = mapRowToUnloader(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data", e);
        }
        return unloader;
    }

    @Override
    public Unloader createUnloader(Unloader unloader) {
        String sql = "INSERT INTO unloader (name, shift, employee_number, status) " +
                "VALUES (?, ?, ?, ?) RETURNING employee_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    unloader.getName(), unloader.getShift(),
                    unloader.getEmployeeNumber(), unloader.getStatus());
            if (newId != null) {
                return getUnloaderById(newId);
            }
        } catch (Exception e) {
            throw new DaoException("Error creating unloader", e);
        }
        return null;
    }

    @Override
    public Unloader updateUnloader(int id, Unloader unloader) {
        String sql = "UPDATE unloader SET name = ?, shift = ?, employee_number = ?, status = ?, door_id = ? " +
                "WHERE employee_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unloader.getName(),
                    unloader.getShift(), unloader.getEmployeeNumber(),
                    unloader.getStatus(), unloader.getDoorId(),
                    unloader.getEmployeeId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getUnloaderById(unloader.getEmployeeId());
        } catch (Exception e) {
            throw new DaoException("Error updating unloader", e);
        }
    }

    @Override
    public int deleteUnloaderById(int id) {
        String sql = "DELETE FROM unloader WHERE employee_id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new DaoException("Error deleting unloader", e);
        }
    }

    @Override
    public List<Unloader> getAllUnloaders() {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT + "ORDER BY u.name";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data", e);
        }
        return unloaders;
    }

    @Override
    public List<Unloader> getAvailableUnloaders() {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT + "WHERE u.status = 'Unassigned'";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data", e);
        }
        return unloaders;
    }

    @Override
    public List<Unloader> getUnloadersByStatus(String status) {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT + "WHERE u.status = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data by status", e);
        }
        return unloaders;
    }

    @Override
    public List<Unloader> getUnloadersByDoorId(int doorId) {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT + "WHERE u.door_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, doorId);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data by door ID", e);
        }
        return unloaders;
    }

    @Override
    public boolean assignUnloaderToDoor(int unloaderId, int doorId) {
        String sql = "UPDATE unloader SET status = 'Assigned', door_id = ? WHERE employee_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, doorId, unloaderId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new DaoException("Error assigning unloader to door", e);
        }
    }

    @Override
    public boolean unassignUnloader(int unloaderId) {
        String sql = "UPDATE unloader SET status = 'Unassigned', door_id = NULL WHERE employee_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unloaderId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new DaoException("Error unassigning unloader", e);
        }
    }

    @Override
    public Unloader getUnloaderByName(String name) {
        Unloader unloader = null;
        String sql = UNLOADER_SELECT + "WHERE LOWER(u.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            if (results.next()) {
                unloader = mapRowToUnloader(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing unloader data by name", e);
        }
        return unloader;
    }

    private Unloader mapRowToUnloader(SqlRowSet results) {
        Unloader unloader = new Unloader();
        unloader.setEmployeeId(results.getInt("employee_id"));
        unloader.setName(results.getString("name"));
        unloader.setShift(results.getString("shift"));
        unloader.setEmployeeNumber(results.getString("employee_number"));
        unloader.setStatus(results.getString("status"));
        unloader.setDoorId(results.getInt("door_id"));
        return unloader;
    }
}