package org.gagan.project.resources;


import org.gagan.project.models.Level;
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
@Path("/tenant/{tenID}/level")
public class LevelResource {
    private DatabaseConnection db=DatabaseConnection.getInstance();
    private ParkingSpotResource parkingSpotResource=new ParkingSpotResource();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLevelDetails(@PathParam("tenID") int tenantId , @DefaultValue("-1") @QueryParam("level") long levId)
    {
        List<Level> res=new ArrayList<>();
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select * from level where tenantid="+tenantId+";");

            while(rs.next())
            {
                long id=rs.getLong("id");
                long tenantid=rs.getLong("tenantid");
                long carspots=rs.getLong("carspots");
                long bikespots=rs.getLong("bikespots");

                Level level=new Level(id,tenantId,carspots,bikespots);
                res.add(level);
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

        if(levId==-1)
        {
            Level[] arr=new Level[res.size()];
            for(int i=0;i<res.size();i++)
            {
                arr[i]=res.get(i);
            }
            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else
        {
            for(Level l:res)
            {
                if(l.getId()==levId)
                {
                    return Response
                            .ok()
                            .entity(l)
                            .build();
                }
            }
            return Response
                    .noContent()
                    .header("Error","Level with tenantID="+tenantId+" and levelID="+levId+" was not found!")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLevel(@PathParam("tenID") long tenantId, Level level, @Context UriInfo uriInfo)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs=stmt.executeQuery("select count(*) from level where tenantid="+tenantId+";");
            rs.next();
            long nextid=rs.getLong("count")+1;

            level.setId(nextid);
            level.setTenantId(tenantId);

            String insert="insert into level values("+nextid+" , "+tenantId+" , "+level.getCarSpots()+" , "+level.getBikeSpots()+");";
            stmt.executeUpdate(insert);




            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("Error",e.getMessage())
                    .build();
        }

        for(int i=0;i<level.getCarSpots();i++)
        {
            ParkingSpot ps=new ParkingSpot(VehicleType.CAR,level.getId());
            parkingSpotResource.addParkingSpot(tenantId,ps);
        }

        for(int i=0;i<level.getBikeSpots();i++)
        {
            ParkingSpot ps=new ParkingSpot(VehicleType.BIKE,level.getId());
            parkingSpotResource.addParkingSpot(tenantId,ps);
        }
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(level.getId())).build();

        return Response
                .created(uri)
                .entity(level)
                .build();
    }
}
