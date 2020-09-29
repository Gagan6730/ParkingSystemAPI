package org.gagan.project.models;

import java.io.*;
import java.util.*;

public class Level {
    private long id;
    private long tenantId;
    private long carSpots;
    private long bikeSpots;

    public Level() {

    }

    public Level(long id, long tenantId, long carSpots, long bikeSpots) {
        this.id = id;
        this.tenantId = tenantId;
        this.carSpots = carSpots;
        this.bikeSpots = bikeSpots;
    }

    public Level(long carSpots, long bikeSpots) {
        this.id = id;
        this.tenantId = tenantId;
        this.carSpots = carSpots;
        this.bikeSpots = bikeSpots;
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

    public long getCarSpots() {
        return carSpots;
    }

    public void setCarSpots(long carSpots) {
        this.carSpots = carSpots;
    }

    public long getBikeSpots() {
        return bikeSpots;
    }

    public void setBikeSpots(long bikeSpots) {
        this.bikeSpots = bikeSpots;
    }
}
