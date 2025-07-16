package com.manifest_processor2.controller;


import com.manifest_processor2.dao.TrailerDao;
import com.manifest_processor2.exception.DaoException;
import com.manifest_processor2.model.Trailer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/trailers")
public class TrailerController {
    private final TrailerDao trailerDao;

    public TrailerController(TrailerDao trailerDao) {
        this.trailerDao = trailerDao;
    }

    @GetMapping
    public List<Trailer> getAllTrailers() {
        try {
            return trailerDao.getTrailers();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find Trailers: " + e.getMessage());
        }
    }

    @GetMapping("/{trailerNumber}")
    public Trailer getTrailerByNumber(@PathVariable String trailerNumber) {
        try {
            Trailer trailer = trailerDao.getTrailerByNumber(trailerNumber);
            if (trailer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trailer with number " + trailerNumber + " not found");
            }
            return trailer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving trailer: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Trailer> createTrailer(@Valid @RequestBody Trailer trailer) {
        Trailer createdTrailer = trailerDao.createTrailer(trailer);
        try {
            return new ResponseEntity<>(createdTrailer, HttpStatus.CREATED);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating Trailer: "+ e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{trailerNumber}")
    public Trailer updateTrailer(@PathVariable String trailerNumber, @Valid@RequestBody Trailer trailerUpdates) {
        try {

            Trailer existingTrailer = trailerDao.getTrailerByNumber(trailerNumber);
            if (existingTrailer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trailer not found");
            }


            if (trailerUpdates.getTrailerNumber() != null &&
                    !trailerUpdates.getTrailerNumber().equals(trailerNumber)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Trailer number in path and body don't match");
            }


            trailerUpdates.setTrailerId(existingTrailer.getTrailerId());


            Trailer updatedTrailer = trailerDao.updateTrailer(trailerUpdates);
            return updatedTrailer;

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating trailer: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{trailerNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrailer(@PathVariable String trailerNumber) {
        try {

            Trailer trailer = trailerDao.getTrailerByNumber(trailerNumber);
            if (trailer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trailer not found");
            }


            boolean deleted = trailerDao.deleteTrailer(trailer.getTrailerId());
            if (!deleted) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete trailer");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting trailer: " + e.getMessage());
        }
    }
}