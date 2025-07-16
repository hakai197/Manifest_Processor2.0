package com.manifest_processor2.model;

public class OrderNumber {
    private int orderId;
    private String orderNumber;
    private int customerId;
    private int shipperId;
    private int trailerId;
    private String doorNumber;
    private int handlingUnit;
    private int weight;
    private String status;


    public OrderNumber() {}


    public OrderNumber(int orderId, String orderNumber, int customerId, int shipperId,
                       int trailerId, String doorNumber, int handlingUnit, int weight, String status) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.trailerId = trailerId;
        this.doorNumber = doorNumber;
        this.handlingUnit = handlingUnit;
        this.weight = weight;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public int getHandlingUnit() {
        return handlingUnit;
    }

    public void setHandlingUnit(int handlingUnit) {
        this.handlingUnit = handlingUnit;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderNumber{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerId=" + customerId +
                ", shipperId=" + shipperId +
                ", trailerId=" + trailerId +
                ", doorNumber='" + doorNumber + '\'' +
                ", handlingUnit=" + handlingUnit +
                ", weight=" + weight + '\''+
                ", Status="+ status +
                '}';
    }

    public void setDoorId(int doorId) {
    }

    public Object getDoorId() {
        return null;
    }
}