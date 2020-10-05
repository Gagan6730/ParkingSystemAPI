package org.project.parkingsystem.unitTest;


import org.project.parkingsystem.models.Tenant;
import org.project.parkingsystem.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TenantResourceTest {

    @Test
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
    @Test
    public void testGetTenantWithID()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant").path(Long.toString(ResourceCreation.t.getTenantID()));

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();
        Tenant tenant=response.readEntity(Tenant.class);

        System.out.println(tenant.getOrgName());

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());




        webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant").path("1000000");
        builder=webTarget.request(MediaType.APPLICATION_JSON);
        response=builder.get();
        tenant=response.readEntity(Tenant.class);
        Assert.assertEquals(response.getStatus(),Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testAddTenant()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
//        Tenant t=new Tenant("ABCD3","abcd3","pass3");

        Response response=builder.post(Entity.entity(ResourceCreation.t,MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        System.out.println(response.getHeaders().toString());
        Tenant tenant=response.readEntity(Tenant.class);
        ResourceCreation.t.setTenantID(tenant.getTenantID());
        Assert.assertEquals(response.getStatus(),Response.Status.CREATED.getStatusCode());



        //add same tenant would return bad request
        response=builder.post(Entity.entity(ResourceCreation.t,MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        Assert.assertEquals(response.getStatus(),Response.Status.BAD_REQUEST.getStatusCode());

    }

}
