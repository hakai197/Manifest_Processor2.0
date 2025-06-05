package com.manifest_processor2.dao;

import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.DockDoor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCDockDoorDao implements DockDoorDao {
    private final JdbcTemplate jdbcTemplate;

    public JDBCDockDoorDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public DockDoor getDockDoorById(int id) {
        DockDoor dockDoor = null;
        String sql = "SELECT door_id, door_number, door_location, status FROM Dock_Door WHERE door_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                dockDoor = mapRowToDockDoor(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing dock door data", e);
        }
        return dockDoor;
    }

    @Override
    public List<DockDoor> getDockDoors() {
        List<DockDoor> dockDoors = new ArrayList<>();
        String sql = "SELECT door_id, door_number, door_location, status FROM Dock_Door ORDER BY door_number";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                dockDoors.add(mapRowToDockDoor(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing all dock doors", e);
        }
        return dockDoors;
    }

    @Override
    public List<DockDoor> getDockDoorbyStatus(String status) {
        List<DockDoor> dockDoors = new ArrayList<>();
        String sql = "SELECT door_id, door_number, door_location, status FROM Dock_Door WHERE status = ? ORDER BY door_number";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status);
            while (results.next()) {
                dockDoors.add(mapRowToDockDoor(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing dock doors by status", e);
        }
        return dockDoors;
    }

    @Override
    public DockDoor updateDockDoor(DockDoor dockDoor) {
        String sql = "UPDATE Dock_Door SET door_number = ?, door_location = ?, status = ? WHERE door_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    dockDoor.getDoorNumber(),
                    dockDoor.getDoorLocation(),
                    dockDoor.getStatus(),
                    dockDoor.getDoorId());

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getDockDoorById(dockDoor.getDoorId());
        } catch (Exception e) {
            throw new DaoException("Error updating dock door", e);
        }
    }

    @Override
    public DockDoor createDockDoor(DockDoor dockDoor) {
        String sql = "INSERT INTO Dock_Door (door_number, door_location, status) VALUES (?, ?, ?) RETURNING door_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    dockDoor.getDoorNumber(),
                    dockDoor.getDoorLocation(),
                    dockDoor.getStatus());

            if (newId != null) {
                return getDockDoorById(newId);
            }
        } catch (Exception e) {
            throw new DaoException("Error creating new dock door", e);
        }
        return null;
    }

    private DockDoor mapRowToDockDoor(SqlRowSet rs) {
        return new DockDoor(
                rs.getInt("door_id"),
                rs.getString("door_number"),
                rs.getString("door_location"),
                rs.getString("status")
        );
    }
}
