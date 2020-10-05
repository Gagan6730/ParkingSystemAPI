package org.project.parkingsystem.unitTest;

import org.project.parkingsystem.models.Payment;
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

public class PaymentResourceTest {

    Payment[] payments;
    long amount=0;
    @Test
    public void testGetAllPayments() {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("payment");

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        payments=response.readEntity(Payment[].class);

        for(Payment p:payments)
        {
            System.out.println(p.getTenantId()+" "+p.getCustomerId()+" "+p.getAmount());
            amount+=p.getAmount();
        }
        Assert.assertEquals(payments.length,1);

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }

    @Test
    public void testGetPaymentWithID() {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("payment")
                .path(Long.toString(payments[0].getId()));

        Invocation.Builder builder=webTarget.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        Response response=builder.get();

        Payment p=response.readEntity(Payment.class);

        System.out.println(p.getTenantId()+" "+p.getCustomerId()+" "+p.getAmount());

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }

    @Test
    public void testGetTotalAmountCollected()
    {
        Client client= ClientBuilder.newClient(new ClientConfig().register(TenantResource.class));
        WebTarget webTarget= client.target("http://localhost:8080/ParkingSystem/webapi").path("tenant")
                .path(Long.toString(ResourceCreation.t.getTenantID()))
                .path("payment")
                .path("report");

        Invocation.Builder builder=webTarget.request(MediaType.TEXT_PLAIN);
        Response response=builder.get();

        long sum=response.readEntity(Long.class);
        System.out.println(sum);
        Assert.assertEquals(sum,amount);

        Assert.assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

    }

}