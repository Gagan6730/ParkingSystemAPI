package org.project.parkingsystem.models;

import java.io.*;
import java.util.*;

public class Ticket {
    private long id;
    private long tenantid;
    private long customerid;

    public Ticket() {
    }

    public Ticket(long id, long tenantid, long customerid) {
        this.id = id;
        this.tenantid = tenantid;
        this.customerid = customerid;
    }
    public Ticket(long customerid) {
        this.customerid = customerid;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTenantid() {
        return tenantid;
    }

    public void setTenantid(long tenantid) {
        this.tenantid = tenantid;
    }

    public long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(long customerid) {
        this.customerid = customerid;
    }
}
