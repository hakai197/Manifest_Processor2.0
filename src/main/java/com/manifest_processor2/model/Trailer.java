package com.manifest_processor2.model;

public class Trailer {
    private int trailerId;
    private String trailerNumber;
    private String trailerType;
    private int shipperId;
    private String doorNumber;
    private String status;



    public Trailer() {

    }

    public Integer getCurrentAssignment() {
        return currentAssignment;
    }

    public void setCurrentAssignment(Integer currentAssignment) {
        this.currentAssignment = currentAssignment;
    }

    private Integer currentAssignment;


    public Trailer(Integer currentAssignment) {
        this.currentAssignment = currentAssignment;
    }

    public Trailer(String status, Integer currentAssignment) {
        this.status = status;
        this.currentAssignment = currentAssignment;
    }

    public int getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDoorId(int doorId) {
    }

    public Object getDoorId() {
        return null;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "trailerId=" + trailerId +
                ", trailerNumber='" + trailerNumber + '\'' +
                ", trailerType='" + trailerType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}