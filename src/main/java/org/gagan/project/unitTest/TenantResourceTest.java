package org.gagan.project.unitTest;


import org.gagan.project.models.Tenant;
import org.gagan.project.resources.DatabaseConnection;
import org.gagan.project.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.testng.Assert;
import org.testng.annotations.*;
import test.v6.A;

import javax.annotation.Priority;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.Statement;

public class TenantResourceTest {

    @Test(priority = 3)
    public void testGetAllTenants()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();
        Tenant[] tenants=response.readEntity(Tenant[].class);

        for(Tenant t:tenants)
        {
            System.out.println(t.getTenantID()+" "+t.getOrgName());
        }

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }
    @Test(priority = 2)
    public void testGetTenantWithID()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant").path(Long.toString(ResourceCreation.t.getTenantID()));

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();
        Tenant tenant=response.readEntity(Tenant.class);

        System.out.println(tenant.getTenantID()+" "+tenant.getOrgName());

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());




        webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant").path("1000000");
        builder=webTarget.request(MediaType.APPLICATION_JSON);
        response=builder.get();
        tenant=response.readEntity(Tenant.class);
        Assert.assertEquals(response.getStatus(),Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test(priority = 1)
    public void testAddTenant()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
//        Tenant t=new Tenant("ABCD3","abcd3","pass3");

        Response response=builder.post(Entity.entity(ResourceCreation.t,MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        System.out.println(response.getHeaders().toString());
        ResourceCreation.t.setTenantID(response.readEntity(Tenant.class).getTenantID());
        Assert.assertEquals(response.getStatus(),Response.Status.CREATED.getStatusCode());



        //add same tenant would return bad request
        response=builder.post(Entity.entity(ResourceCreation.t,MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        Assert.assertEquals(response.getStatus(),Response.Status.BAD_REQUEST.getStatusCode());

    }

}
