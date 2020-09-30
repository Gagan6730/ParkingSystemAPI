package org.gagan.project.unitTest;

import org.gagan.project.models.Customer;
import org.gagan.project.models.VehicleType;
import org.gagan.project.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Priority;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;


public class CustomerResourceTest {
    public List<Customer> list=new ArrayList<>();
    @BeforeClass
    public void createCustomers()
    {
        String[] arr={"DL9CAE1232","PB9KDE0001","DL9UOP1112","UK8ERE0192","KL9QWR6564","PB5PRE8888"};
        for(int i=0;i<6;i++)
        {
            if(i<3)
            {
                list.add(new Customer(arr[i],VehicleType.CAR));
            }
            else
            {
                list.add(new Customer(arr[i],VehicleType.BIKE));
            }
        }
    }

    @Test(priority = 8)
    public void testAddCustomer()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("customer");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        for(Customer c:list) {
            Response response = builder.post(Entity.entity(c, MediaType.APPLICATION_JSON));
//            Customer customer=response.readEntity(Customer.class);
            System.out.println(response.getHeaders().toString());
//            System.out.println(c.toString());
            Assert.assertEquals(response.getStatus(), Response.Status.CREATED.getStatusCode());
        }
    }

    @Test(priority = 9)
    public void testGetAllCustomers()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("customer");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);

        Response response=builder.get();

        Customer[] customers=response.readEntity(Customer[].class);
        System.out.println("testGetAllCustomers");
        for(Customer c:customers)
        {
            System.out.println(c.getId()+" "+c.getVehicleNum()+" "+c.getVehicleType());
        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }
    @Test(priority = 10)
    public void testGetAllCustomers_Car()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("customer")
                .queryParam("vehicletype","CAR");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);

        Response response=builder.get();

        Customer[] customers=response.readEntity(Customer[].class);
        System.out.println("testGetAllCustomers_Car");
        for(Customer c:customers)
        {
            Assert.assertEquals(c.getVehicleType()== VehicleType.CAR?0:1,0);
            System.out.println(c.getId()+" "+c.getVehicleType());
        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }

    @Test(priority = 11)
    public void testGetAllCustomers_Bike()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("customer")
                .queryParam("vehicletype","BIKE");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);

        Response response=builder.get();

        System.out.println("testGetAllCustomers_Bike");
        Customer[] customers=response.readEntity(Customer[].class);
        for(Customer c:customers)
        {
            Assert.assertEquals(c.getVehicleType()== VehicleType.CAR?0:1,1);
            System.out.println(c.getId()+" "+c.getVehicleType());
        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }






}
