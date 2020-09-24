package org.gagan.project.resources;

import org.gagan.project.models.Customer;
import org.gagan.project.models.Tenant;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Path("tenant")
public class TenantResource {

    private DatabaseConnection db=DatabaseConnection.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTenants()
    {
        List<Tenant> res=new ArrayList<>();
        try {
            Statement stmt = db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select * from tenant;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String orgName = rs.getString("orgname");
                String userName = rs.getString("username");
                String pass = rs.getString("pass");
                System.out.println(userName+" "+pass);
                Tenant tenant = new Tenant(id, orgName,userName);

                res.add(tenant);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(400)
                    .header("Error",e.getMessage())
                    .build();
        }

//        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(tenant.getTenantID())).build();
        Tenant[] arr=new Tenant[res.size()];
        for(int i=0;i<res.size();i++)
        {
            arr[i]=res.get(i);
        }
        return Response.ok()
                .entity(arr)
                .build();
    }

//    @Path("/{tenID}/customer")
//    public CustomerResource getCustomerResource()
//    {
//        return new CustomerResource();
//    }
//
//    @Path("/{tenID}/parkingspot")
//    public ParkingSpotResource getParkingSpotResource()
//    {
//        return new ParkingSpotResource();
//    }

    @GET
    @Path("/{tenID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTenantWithID(@PathParam("tenID") int tenID)
    {
//        ArrayList<Tenant> res=new ArrayList<>();
        Tenant tenant=null;
        try {
            Statement stmt = db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select * from tenant where id="+tenID+";");
            while (rs.next()) {
                int id = rs.getInt("id");
                String orgName = rs.getString("orgname");
                String userName = rs.getString("username");
                String pass = rs.getString("pass");
                System.out.println(userName+" "+pass);
                tenant = new Tenant(id, orgName,userName);


            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .noContent()
                    .header("Error",e.getMessage())
                    .build();
        }

//        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(tenant.getTenantID())).build();
        if(tenant==null)
        {
            return Response
                    .noContent()
                    .header("Error","No tenant with id="+tenID+" was found!")
                    .build();
        }

        return Response.ok()
                .entity(tenant)
                .build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTenant(Tenant tenant, @Context UriInfo uriInfo)
    {
        try {
            Statement stmt = db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from tenant;");
            rs.next();
            long newID=rs.getLong("count")+1;
            tenant.setTenantID(newID);

            String insertTenant="insert into tenant values("+tenant.getTenantID()+",'"+tenant.getOrgName()+"' , '"+tenant.getUserName()+"' , '"+tenant.getPass()+"');";
            System.out.println(newID+" "+rs.getRow()+" "+insertTenant);
            stmt.executeUpdate(insertTenant);

            rs.close();
            stmt.close();

        } catch (SQLException e) {

//            e.printStackTrace();
            System.out.println(e.getMessage());
            Tenant t=new Tenant(tenant.getTenantID(),tenant.getOrgName(),tenant.getUserName());
            return Response
                    .status(400)
                    .header("Error",e.getMessage())
                    .entity(t)
                    .build();
        }
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(tenant.getTenantID())).build();
        Tenant t=new Tenant(tenant.getTenantID(),tenant.getOrgName(),tenant.getUserName());
        return Response.
                created(uri)
                .entity(t)
                .build();
    }

}
