package org.gagan.project.models;

public class Price {
    private long tenantId;
    private VehicleType vehicleType;
    private long hourlyRate;
    private long maxRate;
    private long threshold;

    public Price(long tenantId, VehicleType vehicleType, long hourlyRate, long maxRate, long threshold) {
        this.tenantId = tenantId;
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
        this.maxRate = maxRate;
        this.threshold = threshold;
    }

    public Price() {
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public long getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(long hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public long getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(long maxRate) {
        this.maxRate = maxRate;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }
}
