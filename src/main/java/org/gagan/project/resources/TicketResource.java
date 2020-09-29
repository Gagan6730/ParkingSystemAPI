package org.gagan.project.resources;

import org.gagan.project.models.Customer;
import org.gagan.project.models.Ticket;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Path("/tenant/{tenID}/ticket")
public class TicketResource {

    private static DatabaseConnection db=DatabaseConnection.getInstance();
    private CustomerResource customerResource=new CustomerResource();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTickets(@PathParam("tenID") long tenantId )
    {
        List<Ticket> res=new ArrayList<>();
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from ticket where tenantid="+tenantId+";");

            while(rs.next())
            {
                long id=rs.getLong("id");
                long cid=rs.getLong("customerid");

                res.add(new Ticket(id,tenantId,cid));
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

        Ticket[] arr=new Ticket[res.size()];
        for(int i=0;i<res.size();i++)
        {
            arr[i]=res.get(i);
        }
        return Response
                .ok()
                .entity(arr)
                .build();
    }


    @GET
    @Path("/{ticketid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketWithID(@PathParam("tenID") long tenantId, @PathParam("ticketid") long id,
                                    @DefaultValue("false") @QueryParam("customer") boolean findCustomer)
    {
        Ticket ticket=null;
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from ticket where tenantid="+tenantId+" and id="+id+";");
            if(rs.next())
            {
                long customerid=rs.getLong("customerid");
                ticket=new Ticket(id,tenantId,customerid);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(ticket==null)
        {
            return Response
                    .noContent()
                    .header("Error","No ticket with tenantid="+tenantId+" and id="+id+" was found!")
                    .build();
        }

        if(findCustomer)
        {
            return customerResource.getCustomerWithID(tenantId,ticket.getCustomerid());
        }
        else {
            return Response
                    .ok()
                    .entity(ticket)
                    .build();
        }
    }

//    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response addTicket(@PathParam("tenID") long tenantId,Ticket ticket)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select count(*) from ticket where tenantid="+tenantId+";");
            rs.next();
            long nextID=rs.getLong("count")+1;

            stmt.executeUpdate("insert into ticket values("+nextID+" , "+tenantId+" , "+ticket.getCustomerid()+");");
            ticket.setTenantid(tenantId);
            ticket.setId(nextID);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }

        return Response
                .status(201)
                .entity(ticket)
                .build();
    }

}
