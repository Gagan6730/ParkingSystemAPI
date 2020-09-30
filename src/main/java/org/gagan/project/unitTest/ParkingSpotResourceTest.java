package org.gagan.project.unitTest;

import org.gagan.project.models.ParkingSpot;
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

public class ParkingSpotResourceTest {
    @Test
    public void getAllParkingSpots()
    {
//        "/tenant/{tenID}/parkingspot"
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("/tenant/2/parkingspot");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        ParkingSpot[] parkingSpots=response.readEntity(ParkingSpot[].class);

        for(ParkingSpot ps:parkingSpots)
        {
            System.out.println(ps.getId()+" "+ps.getLevel()+" "+ps.getVehicleType());
        }

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }

    @Test
    public void getAllCarParkingSpots()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("/tenant/2/parkingspot")
                .queryParam("vehicletype","CAR");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        ParkingSpot[] parkingSpots=response.readEntity(ParkingSpot[].class);

        for(ParkingSpot ps:parkingSpots)
        {
            Assert.assertEquals(ps.getVehicleType(), VehicleType.CAR);
            System.out.println(ps.getId()+" "+ps.getLevel()+" "+ps.getVehicleType());
        }

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());



        //wrong query param
        webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("/tenant/2/parkingspot")
                .queryParam("vehicletype","yoyo");
        builder=webTarget.request(MediaType.APPLICATION_JSON);
        response=builder.get();
        Assert.assertEquals(response.getStatus(),Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    public void getAllBikeParkingSpots()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("/tenant/2/parkingspot")
                .queryParam("vehicletype","BIKE");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        ParkingSpot[] parkingSpots=response.readEntity(ParkingSpot[].class);

        for(ParkingSpot ps:parkingSpots)
        {
            Assert.assertEquals(ps.getVehicleType(), VehicleType.BIKE);
            System.out.println(ps.getId()+" "+ps.getLevel()+" "+ps.getVehicleType());
        }

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());



        //wrong query param
        webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("/tenant/2/parkingspot")
                .queryParam("vehicletype","yoyo");
        builder=webTarget.request(MediaType.APPLICATION_JSON);
        response=builder.get();
        Assert.assertEquals(response.getStatus(),Response.Status.NO_CONTENT.getStatusCode());

    }
}
