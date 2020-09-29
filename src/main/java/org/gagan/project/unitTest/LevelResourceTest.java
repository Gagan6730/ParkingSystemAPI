package org.gagan.project.unitTest;

import org.gagan.project.models.Level;
import org.gagan.project.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.*;

public class LevelResourceTest {

    public static Level level=null;
    @BeforeClass
    public void createLevel()
    {
        level=new Level(10,10);
    }

    @Test(priority = 6)
    public void testGetAllLevels() {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("level");
        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        Level[] levels=response.readEntity(Level[].class);
        System.out.println("testGetAllLevels");
        for(Level l:levels)
        {
            System.out.println(l.getId()+" "+l.getCarSpots()+" "+l.getBikeSpots());
        }

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }

    @Test(priority = 7)
    public void testGetLevelWithId() {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("level")
                .path(Long.toString(level.getId()));
        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        Level l=response.readEntity(Level.class);
        System.out.println("testGetLevelWithId");
        System.out.println(l.getId()+" "+l.getCarSpots()+" "+l.getBikeSpots());


        Assert.assertEquals(l.getId(),level.getId());
        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }

    @Test(priority = 4)
    public void testAddLevel() {

        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("level");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        Response response=builder.post(Entity.entity(level,MediaType.APPLICATION_JSON));

        System.out.println("testAddLevel");
        Level l=response.readEntity(Level.class);
        level.setTenantId(l.getTenantId());
        level.setId(l.getId());

        Assert.assertEquals(response.getStatus(),Response.Status.CREATED.getStatusCode());
    }


    @Test(priority = 5)
    public void testAddParkingSpotsAtLevel()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("level")
                .path(Long.toString(level.getId()))
                .queryParam("car",10)
                .queryParam("bike",10);

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        System.out.println(webTarget.getUri().toString());
        Response response=builder.put(Entity.entity(level,MediaType.APPLICATION_JSON));
        if(response.getHeaders().containsKey("Error"))
        {
            System.out.println(response.getHeaders().get("Error"));
        }
        System.out.println(response.getHeaders().toString());
        System.out.println("testAddParkingSpotsAtLevel");

        Assert.assertEquals(response.getStatus(),204);
    }



}