package com.manifest_processor2.model;

public class DockDoor {
    private final String doorNumber;
    private final String doorLocation;
    private String status;

    public DockDoor(int doorId, String doorNumber, String doorLocation, String status) {
        this.doorNumber = doorNumber;
        this.doorLocation = doorLocation;
        this.status= status;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public String getDoorLocation() {
        return doorLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoorId() {
        return 0;
    }
}
