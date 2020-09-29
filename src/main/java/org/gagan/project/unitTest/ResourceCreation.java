package org.gagan.project.unitTest;

import org.gagan.project.models.Tenant;
import org.gagan.project.resources.DatabaseConnection;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ResourceCreation {
    public static Tenant t=null;
    @BeforeTest
    public void createTenant()
    {
        t=new Tenant("ABCD3","abcd3","pass3");
        System.out.println("BeforeTest");
    }

    @AfterTest
    public void deleteAll()
    {
        DatabaseConnection db=DatabaseConnection.getInstance();
        try
        {
            Statement stmt=db.getC().createStatement();
            stmt.executeUpdate("delete from ticket where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from payment where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from customer where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from parkingspot where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from price where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from level where tenantid="+t.getTenantID()+";");
            stmt.executeUpdate("delete from tenant where id="+t.getTenantID()+";");
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("AfterTest");

    }
}
