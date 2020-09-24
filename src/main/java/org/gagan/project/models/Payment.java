package org.gagan.project.models;

import java.io.*;
import java.util.*;

public class Payment {
    private long id;
    private long tenantId;
    private long customerId;
    private long amount;

    public Payment() {
    }

    public Payment(long id, long tenantId, long customerId, long amount) {
        this.id = id;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.amount = amount;
    }
    public Payment(long customerId, long amount) {
        this.customerId = customerId;
        this.amount = amount;
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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
