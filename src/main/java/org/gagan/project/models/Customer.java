package org.gagan.project.models;

import org.gagan.project.resources.CustomerResource;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;


public class Customer {
    private long id;
    private String vehicleNum;
    private long tenantID;
    private Timestamp entryTime;
    private Timestamp exitTime;
    private long parkingSpotID;
    private boolean isParked;
    private VehicleType vehicleType;

    public Customer()
    {

    }

    public Customer(long id, String vehicleNum, long tenantID, Timestamp entryTime, Timestamp exitTime, long parkingSpotID, boolean isParked, VehicleType vehicleType) {
        this.id = id;
        this.vehicleNum = vehicleNum;
        this.tenantID = tenantID;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parkingSpotID = parkingSpotID;
        this.isParked = isParked;
        this.vehicleType = vehicleType;
    }



    public Customer(long id, String vehicleNum, long tenantID, long parkingSpotID, boolean isParked) {
        this.id = id;
        this.vehicleNum = vehicleNum;
        this.tenantID = tenantID;
        this.entryTime = new Timestamp(System.currentTimeMillis());
        this.entryTime=null;
        this.parkingSpotID = parkingSpotID;
        this.isParked=isParked;
    }

    public Customer(long id, String vehicleNum, long parkingSpotID, boolean isParked) {
        this.id = id;
        this.vehicleNum = vehicleNum;
        this.entryTime = new Timestamp(System.currentTimeMillis());
        this.entryTime=null;
        this.parkingSpotID = parkingSpotID;
        this.isParked=isParked;
    }
    public Customer(String vehicleNum,VehicleType vehicleType) {
        this.vehicleNum = vehicleNum;
        this.entryTime = new Timestamp(System.currentTimeMillis());
        this.entryTime=null;
        this.isParked=true;
        this.vehicleType=vehicleType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public long getTenantID() {
        return tenantID;
    }

    public void setTenantID(long tenantID) {
        this.tenantID = tenantID;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }

    public long getParkingSpotID() {
        return parkingSpotID;
    }

    public void setParkingSpotID(long parkingSpotID) {
        this.parkingSpotID = parkingSpotID;
    }

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
