package org.project.parkingsystem.unitTest;

import org.project.parkingsystem.models.Ticket;
import org.project.parkingsystem.resources.TenantResource;
import org.glassfish.jersey.client.ClientConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TicketResourceTest {
    Ticket[] tickets;
    @Test
    public void testGetAllTickets()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("ticket");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        tickets=response.readEntity(Ticket[].class);

        for(Ticket t:tickets)
        {
            System.out.println(t.getId()+" "+t.getTenantid()+" "+t.getCustomerid());
        }
        Assert.assertEquals(tickets.length,6);

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }


    @Test
    public void testGetTicketWithID()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("ticket")
                .path(Long.toString(tickets[0].getId()));

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        Ticket t=response.readEntity(Ticket.class);

        System.out.println(t.getId()+" "+t.getTenantid()+" "+t.getCustomerid());

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }
}
