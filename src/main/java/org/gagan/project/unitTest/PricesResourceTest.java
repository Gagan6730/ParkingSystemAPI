package org.gagan.project.unitTest;

import org.gagan.project.models.Price;
import org.gagan.project.models.VehicleType;
import org.gagan.project.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

public class PricesResourceTest {
    Price bike,car;
    @BeforeClass
    public void createPrice()
    {
        bike=new Price(VehicleType.BIKE,20L,250L,6L);
        car=new Price(VehicleType.CAR,50L,600L,6L);
    }
    @Test
    public void testAddPrice()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("price");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        Response response = builder.post(Entity.entity(car, MediaType.APPLICATION_JSON));
        car.setTenantId(response.readEntity(Price.class).getTenantId());

        Assert.assertEquals(response.getStatus(),Response.Status.CREATED.getStatusCode());
        System.out.println("testAddPrice");


        response = builder.post(Entity.entity(bike, MediaType.APPLICATION_JSON));
        bike.setTenantId(response.readEntity(Price.class).getTenantId());

        Assert.assertEquals(response.getStatus(),Response.Status.CREATED.getStatusCode());

        System.out.println(car.getTenantId()+" "+car.getVehicleType()+" "+car.getHourlyRate()+" "+car.getMaxRate()+" "+car.getThreshold());
        System.out.println(bike.getTenantId()+" "+bike.getVehicleType()+" "+bike.getHourlyRate()+" "+bike.getMaxRate()+" "+bike.getThreshold());

    }
    @Test
    public void testUpdatePrice()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("price");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        car.setHourlyRate(60);
        car.setThreshold(5);

        Response response = builder.put(Entity.entity(car, MediaType.APPLICATION_JSON));
//        Price p=response.readEntity(Price.class);
//        car.setTenantId(p.getTenantId());

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
        System.out.println("testUpdatePrice");


        System.out.println(car.getTenantId()+" "+car.getVehicleType()+" "+car.getHourlyRate()+" "+car.getMaxRate()+" "+car.getThreshold());
    }


    @Test
    public void testGetPrice()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("price");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);


        Response response = builder.get();

        Price[] prices=response.readEntity(Price[].class);
        for(Price p:prices)
        {
            System.out.println(p.getTenantId()+" "+p.getVehicleType()+" "+p.getHourlyRate()+" "+p.getMaxRate()+" "+p.getThreshold());

        }
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }
}
