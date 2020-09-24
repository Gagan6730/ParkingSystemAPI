package org.gagan.project.resources;

import org.gagan.project.models.Customer;
import org.gagan.project.models.Payment;
import org.gagan.project.models.Price;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Path("/tenant/{tenID}/payment")
public class PaymentResource {

    private DatabaseConnection db=DatabaseConnection.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPayments(@PathParam("tenID") long tenantId, @DefaultValue("-1") @QueryParam("customerid") long cid)
    {
        List<Payment> res=new ArrayList<>();
        System.out.println(cid);
        try
        {

            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from payment where tenantid="+tenantId+";");
            while(rs.next())
            {
               long id=rs.getLong("id");
               long tenantid=rs.getLong("tenantid");
               long customerid=rs.getLong("customerid");
               long amount=rs.getLong("amount");

               Payment p=new Payment(id,tenantid,customerid,amount);
               res.add(p);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }
        if(cid==-1) {
            Payment[] arr = new Payment[res.size()];
            for (int i = 0; i < res.size(); i++) {
                arr[i] = res.get(i);
            }
            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else
        {
            for(Payment p:res)
            {
                if(p.getCustomerId()==cid)
                {
                    return Response
                            .ok()
                            .entity(p)
                            .build();
                }
            }
            return Response
                    .noContent()
                    .header("Error","No payment with customer id="+cid+" is available!")
                    .build();
        }
    }

    @GET
    @Path("/{pid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentWithID(@PathParam("tenID") long tenantId, @PathParam("pid") long paymentid)
    {
        Payment payment=null;
        try
        {

            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from payment where tenantid="+tenantId+" and id="+paymentid+";");
            if(rs.next()) {
                long id = paymentid;
                long tenantid=tenantId;
                long customerid=rs.getLong("customerid");
                long amount=rs.getLong("amount");

                payment=new Payment(id,tenantId,customerid,amount);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }

        if(payment==null)
        {
            return Response
                    .noContent()
                    .header("Error","No payment with payment id="+paymentid+" is available!")
                    .build();
        }
        return Response
                .ok()
                .entity(payment)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPayment(@PathParam("tenID") long tenantId, Payment payment )
    {
//        uriInfo=this.
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select count(*) from payment where tenantid="+tenantId+";");
            rs.next();
            long id=rs.getLong("count")+1;

            String insert="insert into payment values("+id+" ,"+tenantId+" , "+payment.getCustomerId()+" , "
                    +payment.getAmount()+")";
            stmt.executeUpdate(insert);

            payment.setTenantId(tenantId);
            payment.setId(id);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }
//        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(payment.getId())).build();

        return Response
                .ok()
                .entity(payment)
                .build();
    }


    void calculatePrice(long tenantId,Customer customer)
    {
        PricesResource pricesResource=new PricesResource();
        Price price= (Price) pricesResource.getPrice(tenantId,customer.getVehicleType().toString()).getEntity();

        long total_hours=calculateNumberOfHours(customer.getEntryTime(),customer.getExitTime());

        long amount=0;
        long days=total_hours/24;
        long hours=total_hours%24;
        if(hours>price.getThreshold())
        {
            hours=0;
            days++;
        }


        amount=days*price.getMaxRate();
        amount+=hours*price.getHourlyRate();

        Payment payment=new Payment(customer.getId(),amount);
        addPayment(tenantId,payment);
        System.out.println(days+" "+hours+" "+amount);
//        return amount;
    }
    private long calculateNumberOfHours(Timestamp entry,Timestamp exit)
    {
        long end=exit.getTime();
        long start=entry.getTime();
        long diff = end - start;

        long diffDays = diff / (24 * 60 * 60 * 1000);
        long remain = diff % (24 * 60 * 60 * 1000);

        long diffHours = remain / (60 * 60 * 1000);
        remain = remain % (60 * 60 * 1000);

        long diffMinutes = remain / (60 * 1000);
        remain = remain % (60 * 1000);

        long diffSeconds = remain / (1000);

        return diffDays*24+((diffMinutes>=1 || diffSeconds>=1)?diffHours+1:diffHours);

//        System.out.println("days : " + diffDays);
//        System.out.println("hours : " + diffHours);
//        System.out.println("minutes : " + diffMinutes);
//        System.out.println("secs: " + diffSeconds);
    }
}
