package org.gagan.project.resources;

import org.gagan.project.models.ParkingSpot;
import org.gagan.project.models.VehicleType;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;
@Path("/tenant/{tenID}/parkingspot")
public class ParkingSpotResource
{
    private DatabaseConnection db=DatabaseConnection.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllParkingSpots(@PathParam("tenID") long tenantId, @DefaultValue("null") @QueryParam("vehicletype") String type)
    {
        List<ParkingSpot> res=new ArrayList<>();
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select * from parkingspot where tenantid="+tenantId+";");
            while(rs.next())
            {
                long id=rs.getLong("id");
                long tenId=rs.getLong("tenantid");
                boolean isreserved=rs.getBoolean("isreserved");
                VehicleType vehicleType =rs.getLong("parkingtype")==0? VehicleType.CAR: VehicleType.BIKE;
                long level=rs.getLong("level");

                ParkingSpot spot=new ParkingSpot(id,tenId,isreserved, vehicleType,level);
                res.add(spot);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }

        if(type.equals("null")) {
            ParkingSpot[] arr = new ParkingSpot[res.size()];
            for (int i = 0; i < res.size(); i++) {
                arr[i] = res.get(i);
            }

            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else {
            long vType=type.equals("CAR")?0:1;
            List<ParkingSpot> parkingSpots=new ArrayList<>();
            for(ParkingSpot p:res)
            {
                if((p.getVehicleType()==VehicleType.CAR && vType==0) || (p.getVehicleType()==VehicleType.BIKE && vType==1))
                {
                    parkingSpots.add(p);
                }
            }
            ParkingSpot[] arr=new ParkingSpot[parkingSpots.size()];
            for(int i=0;i<parkingSpots.size();i++)
            {
                arr[i]=parkingSpots.get(i);
            }

            return Response
                    .ok()
                    .entity(arr)
                    .build();

        }

    }

    @GET
    @Path("/{psId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParkingSpotWithID(@PathParam("tenID") long tenantId, @PathParam("psId") long psId)
    {
        ParkingSpot spot=null;
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from parkingspot where tenantid="+tenantId+" and id="+psId+";");

            if(rs.next())
            {
                long id=rs.getLong("id");
                long tenId=rs.getLong("tenantid");
                boolean isreserved=rs.getBoolean("isreserved");
                VehicleType vehicleType =rs.getLong("parkingtype")==0? VehicleType.CAR: VehicleType.BIKE;
                long level=rs.getLong("level");

                spot=new ParkingSpot(id,tenId,isreserved, vehicleType,level);
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
        if(spot==null)
        {
            return Response
                    .noContent()
                    .header("Error","No customer with tenantId="+tenantId+" and id="+psId+" was found!")
                    .build();
        }

        return Response
                .ok()
                .entity(spot)
                .build();


    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addParkingSpot(@PathParam("tenID") long tenantId,ParkingSpot ps)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from parkingspot where tenantid="+tenantId+";");
            rs.next();
            long nextId=rs.getLong("count")+1;
            ps.setId(nextId);
            ps.setReserved(false);
            ps.setTenantId(tenantId);

            String insert="insert into parkingspot values("+nextId+","+tenantId+" , "+ps.isReserved()+" , "+(ps.getVehicleType()== VehicleType.CAR?0:1)+" , "
                +ps.getLevel()+" );";
            System.out.println(insert);
            stmt.executeUpdate(insert);

            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(500)
                    .header("Error",e.getMessage())
                    .build();
        }

//        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(ps.getId())).build();

        return Response
                .status(201)
                .entity(ps)
                .build();

    }


    boolean changeReservedStatus(long tenantId, long parkingSpotId, boolean changeTo)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            stmt.executeUpdate("update parkingspot set isreserved="+(changeTo)+" where tenantid="+tenantId+" and id="+parkingSpotId+";");
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
