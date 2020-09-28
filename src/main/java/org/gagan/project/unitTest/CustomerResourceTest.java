package org.gagan.project.unitTest;

import org.gagan.project.models.Customer;
import org.gagan.project.models.VehicleType;
import org.gagan.project.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

public class CustomerResourceTest {
    @Test
    public void getAllCustomers_Car()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant/"+2+"/customer").queryParam("vehicletype","CAR");
//        webTarget.queryParam("vehicletype","CAR");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);

        Response response=builder.get();

        Customer[] customers=response.readEntity(Customer[].class);
        for(Customer c:customers)
        {
            Assert.assertEquals(c.getVehicleType()== VehicleType.CAR?0:1,0);
            System.out.println(c.getId()+" "+c.getVehicleType());
        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }

    @Test
    public void getAllCustomers_Bike()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant/"+2+"/customer").queryParam("vehicletype","BIKE");
//        webTarget.queryParam("vehicletype","CAR");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);

        Response response=builder.get();

        Customer[] customers=response.readEntity(Customer[].class);
        for(Customer c:customers)
        {
            Assert.assertEquals(c.getVehicleType()== VehicleType.CAR?0:1,1);
            System.out.println(c.getId()+" "+c.getVehicleType());
        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }

    @Test
    public void addCustomer()
    {
        
    }


}
