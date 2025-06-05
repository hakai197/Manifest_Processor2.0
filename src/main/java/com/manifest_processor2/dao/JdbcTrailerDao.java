package com.manifest_processor2.dao;

import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.Trailer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTrailerDao implements TrailerDao {
    private final String TRAILER_SELECT = "SELECT t.trailer_id, t.trailer_number, t.trailer_type, " +
            "t.shipper_id, t.status, t.door_id FROM trailer t ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTrailerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Trailer getTrailerById(int id) {
        Trailer trailer = null;
        String sql = TRAILER_SELECT + "WHERE t.trailer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                trailer = mapRowToTrailer(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing trailer data", e);
        }
        return trailer;
    }

    @Override
    public List<Trailer> getTrailers() {
        List<Trailer> trailers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(TRAILER_SELECT);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing trailer data", e);
        }
        return trailers;
    }

    @Override
    public List<Trailer> getTrailersByShipperId(int shipperId) {
        List<Trailer> trailers = new ArrayList<>();
        String sql = TRAILER_SELECT + "WHERE t.shipper_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, shipperId);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing trailers by shipper", e);
        }
        return trailers;
    }

    @Override
    public Trailer createTrailer(Trailer trailer) {
        String sql = "INSERT INTO trailer (trailer_number, trailer_type, shipper_id, status, door_id) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING trailer_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    trailer.getTrailerNumber(),
                    trailer.getTrailerType(),
                    trailer.getShipperId(),
                    trailer.getStatus() != null ? trailer.getStatus() : "Unassigned",
                    trailer.getDoorId());

            return getTrailerById(newId);
        } catch (Exception e) {
            throw new DaoException("Error creating trailer", e);
        }
    }

    @Override
    public Trailer updateTrailer(Trailer trailer) {
        String sql = "UPDATE trailer SET trailer_number = ?, trailer_type = ?, " +
                "shipper_id = ?, status = ?, door_id = ? WHERE trailer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    trailer.getTrailerNumber(),
                    trailer.getTrailerType(),
                    trailer.getShipperId(),
                    trailer.getStatus(),
                    trailer.getDoorId(),
                    trailer.getTrailerId());

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getTrailerById(trailer.getTrailerId());
        } catch (Exception e) {
            throw new DaoException("Error updating trailer", e);
        }
    }

    @Override
    public List<Trailer> getAvailableTrailers() {
        List<Trailer> trailers = new ArrayList<>();
        String sql = TRAILER_SELECT + "WHERE t.status = 'Unassigned'";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing available trailers", e);
        }
        return trailers;
    }

    @Override
    public void unassignTrailer(String trailerNumber) {
        String sql = "UPDATE trailer SET status = 'Unassigned', door_id = NULL WHERE trailer_number = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, trailerNumber);
            if (rowsAffected == 0) {
                throw new DaoException("Trailer " + trailerNumber + " not found or not updated");
            }
        } catch (Exception e) {
            throw new DaoException("Error unassigning trailer " + trailerNumber, e);
        }
    }

    @Override
    public void assignUnloaderToTrailer(String trailerNumber, int employeeId) {

    }

    @Override
    public void assignTrailerToDoor(String trailerNumber, int doorId) {
        String sql = "UPDATE trailer SET status = 'Assigned', door_id = ? WHERE trailer_number = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, doorId, trailerNumber);
            if (rowsAffected == 0) {
                throw new DaoException("Trailer " + trailerNumber + " not found or not updated");
            }
        } catch (Exception e) {
            throw new DaoException("Error assigning trailer " + trailerNumber + " to door", e);
        }
    }

    @Override
    public boolean deleteTrailer(int id) {
        String checkSql = "SELECT COUNT(*) FROM order_number WHERE trailer_id = ?";
        String deleteSql = "DELETE FROM trailer WHERE trailer_id = ?";

        try {
            int orderCount = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            if (orderCount > 0) {
                throw new DaoException("Cannot delete trailer with associated orders");
            }

            int rowsDeleted = jdbcTemplate.update(deleteSql, id);
            return rowsDeleted == 1;
        } catch (Exception e) {
            throw new DaoException("Error deleting trailer with ID: " + id, e);
        }
    }

    @Override
    public Trailer getTrailerByNumber(String trailerNumber) {
        String sql = TRAILER_SELECT + "WHERE t.trailer_number = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerNumber);
            if (results.next()) {
                return mapRowToTrailer(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing trailer by number", e);
        }
        return null;
    }

    @Override
    public List<Trailer> getTrailersByDoorId(int doorId) {
        List<Trailer> trailers = new ArrayList<>();
        String sql = TRAILER_SELECT + "WHERE t.door_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, doorId);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error accessing trailers by door", e);
        }
        return trailers;
    }

    private Trailer mapRowToTrailer(SqlRowSet rs) {
        Trailer trailer = new Trailer();
        trailer.setTrailerId(rs.getInt("trailer_id"));
        trailer.setTrailerNumber(rs.getString("trailer_number"));
        trailer.setTrailerType(rs.getString("trailer_type"));
        trailer.setShipperId(rs.getInt("shipper_id"));
        trailer.setStatus(rs.getString("status"));
        trailer.setDoorId(rs.getInt("door_id"));
        return trailer;
    }
}