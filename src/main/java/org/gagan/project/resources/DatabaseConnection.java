package org.gagan.project.resources;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

public class DatabaseConnection {
    private Connection c = null;


    private static DatabaseConnection instance=null;

    private DatabaseConnection() {
        try {
//            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/ParkingSystem",
                            "postgres", "Gagan@6730");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static DatabaseConnection getInstance()
    {
        if(instance==null)
        {
            instance=new DatabaseConnection();
        }
        return instance;
    }

    public Connection getC() {
        return c;
    }
}
