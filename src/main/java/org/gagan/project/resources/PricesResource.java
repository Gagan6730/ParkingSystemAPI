package org.gagan.project.resources;

import org.gagan.project.models.Price;
import org.gagan.project.models.VehicleType;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
@Path("/tenant/{tenID}/price")
public class PricesResource {
    private DatabaseConnection db=DatabaseConnection.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrice(@PathParam("tenID") long tenantId, @DefaultValue("null") @QueryParam("vehicletype") String type)
    {
        List<Price> res=new ArrayList<>();

        try{
            Statement stmt=db.getC().createStatement();
            String query="select * from price where tenantid="+tenantId+";";
            ResultSet rs=stmt.executeQuery(query);

           while(rs.next()) {
               long tenid = rs.getLong("tenantid");
               VehicleType vehicleType = rs.getLong("vehicletype") == 0 ? VehicleType.CAR : VehicleType.BIKE;
               long hourlyRate = rs.getLong("hourlyrate");
               long maxrate = rs.getLong("maxrate");
               long threshold = rs.getLong("threshold");

               Price price = new Price(tenid, vehicleType, hourlyRate, maxrate, threshold);
               res.add(price);
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

        if(type.equals("null")) {
            Price[] arr = new Price[res.size()];
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
            long vType=type.equals("CAR")?0:1;

            for(Price p:res)
            {
                if((p.getVehicleType()==VehicleType.CAR && vType==0) || (p.getVehicleType()==VehicleType.BIKE && vType==1))
                {
                    return Response
                            .ok()
                            .entity(p)
                            .build();
                }
            }
            return Response
                    .noContent()
                    .header("Error","Price for tenantID="+tenantId+" with vehicle type as "+type+" was not found!")
                    .build();
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrice(@PathParam("tenID") long tenantId,Price price)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            long type=price.getVehicleType()==VehicleType.CAR?0:1;
            String update="update price set hourlyrate="+price.getHourlyRate()+" , maxrate="+price.getMaxRate()
                    +" , threshold="+price.getThreshold()+" where tenantid="+tenantId+"and vehicletype="+type+";";

            price.setTenantId(tenantId);

            stmt.executeUpdate(update);



        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }

        return Response
                .ok()
                .header("Updated","Price for tenantID="+tenantId+" was updated!")
                .entity(price)
                .build();
    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrice(@PathParam("tenID") long tenantId, Price price, @Context UriInfo uriInfo)
    {
        try
        {
            Statement stmt=db.getC().createStatement();

            price.setTenantId(tenantId);
            String addPrice="insert into price values("+tenantId+" , "+(price.getVehicleType()==VehicleType.CAR?0:1)+" , "
                    + price.getHourlyRate()+" , "+price.getMaxRate()+" , "+price.getThreshold()+");";
            stmt.executeUpdate(addPrice);
            stmt.close();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(400)
                    .header("Error",e.getMessage())
                    .entity(price)
                    .build();
        }

        return Response
                .status(201)
                .entity(price)
                .build();

    }



}
