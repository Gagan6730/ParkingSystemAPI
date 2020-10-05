package org.project.parkingsystem.models;

import org.glassfish.jersey.internal.inject.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.*;
import java.lang.annotation.*;
import java.util.*;

@XmlRootElement
public class Tenant {
    private long tenantID;
    private String orgName;
    private String userName;

    private String pass;

    public Tenant() {
    }

    public Tenant(long tenantID, String orgName, String userName, String pass) {
        this.tenantID = tenantID;
        this.orgName = orgName;
        this.userName = userName;
        this.pass = pass;
    }

    public Tenant(String orgName, String userName, String pass) {
        this.orgName = orgName;
        this.userName = userName;
        this.pass = pass;
    }

    public Tenant(long tenantID, String orgName, String userName) {
        this.tenantID = tenantID;
        this.orgName = orgName;
        this.userName = userName;
    }

    public long getTenantID() {
        return tenantID;
    }

    public void setTenantID(long tenantID) {
        this.tenantID = tenantID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
