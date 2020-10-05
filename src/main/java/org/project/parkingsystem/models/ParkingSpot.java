package org.project.parkingsystem.models;

public class ParkingSpot {
    private long id;
    private long tenantId;
    private boolean isReserved;
    private VehicleType vehicleType;
    private long level;

    public ParkingSpot(long id, long tenantId, boolean isReserved, VehicleType vehicleType, long level) {
        this.id = id;
        this.tenantId = tenantId;
        this.isReserved = isReserved;
        this.vehicleType = vehicleType;
        this.level = level;
    }

    public ParkingSpot(VehicleType vehicleType, long level) {

        this.vehicleType = vehicleType;
        this.level = level;
    }
    public ParkingSpot() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }
}
