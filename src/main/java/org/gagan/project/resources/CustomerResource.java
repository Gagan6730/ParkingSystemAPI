package org.gagan.project.resources;

import org.gagan.project.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.util.*;
import java.sql.Date;

@Path("/tenant/{tenID}/customer")
public class CustomerResource {
    private DatabaseConnection db=DatabaseConnection.getInstance();

    private ParkingSpotResource parkingSpotResource=new ParkingSpotResource();
    private PaymentResource paymentResource=new PaymentResource();

    private List<Customer> getCustomerByVehicleType(List<Customer> res,String vtype)
    {
        if(vtype.equals("null"))
        {
            return res;
        }
        else if(vtype.toLowerCase().equals("car"))
        {
            List<Customer> list=new ArrayList<>();
            for(Customer c:res)
            {
                if(c.getVehicleType()==VehicleType.CAR)
                {
                    list.add(c);
                }
            }
            return list;
        }
        else if(vtype.toLowerCase().equals("bike"))
        {
            List<Customer> list=new ArrayList<>();
            for(Customer c:res)
            {
                if(c.getVehicleType()==VehicleType.BIKE)
                {
                    list.add(c);
                }
            }
            return list;
        }
        return null;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers(@PathParam("tenID") long tenantId ,@DefaultValue("null") @QueryParam("parked") String parked,
                                    @DefaultValue("null") @QueryParam("vehicletype") String vtype)
    {
        List<Customer> res=new ArrayList<>();
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer where tenantid="+tenantId+";");
            while(rs.next())
            {
                long id=rs.getLong("id");
                long tenId=rs.getLong("tenantid");
                long parkingSpotId=rs.getLong("parkingspotid");
                Timestamp entryTime=rs.getTimestamp("entrytime");
                Timestamp exitTime=rs.getTimestamp("exittime");
                String vehicleNum=rs.getString("vehiclenum");
                boolean isParked=rs.getBoolean("isparked");
                VehicleType vehicleType=rs.getLong("vehicletype")==0?VehicleType.CAR : VehicleType.BIKE;



                System.out.println(id+" "+tenantId+" "+vehicleNum);
                Customer c=new Customer(id,vehicleNum,tenId,entryTime,exitTime,parkingSpotId,isParked,vehicleType);
                res.add(c);
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

        if (parked.equals("null")) {


            List<Customer> list=getCustomerByVehicleType(res,vtype);

            if(list==null)
            {
                return Response
                        .noContent()
                        .header("Error","Wrong Query parameter!")
                        .build();
            }

            Customer[] arr = new Customer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }

            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else if(parked.equals("false"))
        {
            List<Customer> notParked=new ArrayList<>();
            for(Customer c:res)
            {
                if(!c.isParked())
                {
                    notParked.add(c);
                }
            }

            List<Customer> list=getCustomerByVehicleType(notParked,vtype);
            if(list==null)
            {
                return Response
                        .noContent()
                        .header("Error","Wrong Query parameter!")
                        .build();
            }

            Customer[] arr = new Customer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }

            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else if(parked.equals("true")){
            List<Customer> park=new ArrayList<>();
            for(Customer c:res)
            {
                if(c.isParked())
                {
                    park.add(c);
                }
            }

            List<Customer> list=getCustomerByVehicleType(park,vtype);
            if(list==null)
            {
                return Response
                        .noContent()
                        .header("Error","Wrong Query parameter!")
                        .build();
            }


            Customer[] arr = new Customer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }

            return Response
                    .ok()
                    .entity(arr)
                    .build();
        }
        else
        {
            return Response
                    .noContent()
                    .header("Error","Wrong Query parameter!")
                    .build();
        }

    }

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerWithID(@PathParam("tenID") long tenantId, @PathParam("customerId") long customerId)
    {
        Customer c=null;
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer where tenantid="+tenantId+" and id="+customerId+";");
            while(rs.next())
            {
                long id=rs.getLong("id");
                long tenId=rs.getLong("tenantid");
                long parkingSpotId=rs.getLong("parkingspotid");
                Timestamp entryTime=rs.getTimestamp("entrytime");
                Timestamp exitTime=rs.getTimestamp("exittime");
                String vehicleNum=rs.getString("vehiclenum");
                boolean isParked=rs.getBoolean("isparked");
                VehicleType vehicleType=rs.getLong("vehicletype")==0?VehicleType.CAR : VehicleType.BIKE;



                System.out.println(id+" "+tenantId+" "+vehicleNum);
                c=new Customer(id,vehicleNum,tenId,entryTime,exitTime,parkingSpotId,isParked,vehicleType);
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


        if(c==null)
        {
            return Response
                    .noContent()
                    .header("Error","No customer with id="+customerId+" was found!")
                    .build();
        }
        return Response
                .ok()
                .entity(c)
                .build();

    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCustomer(@PathParam("tenID") long tenantId, Customer c, @Context UriInfo uriInfo)
    {
        try
        {
            Statement stmt=db.getC().createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from customer where tenantid="+tenantId+";");
            rs.next();
            long nextID=rs.getLong("count")+1;
            c.setId(nextID);
            c.setEntryTime(new Timestamp(System.currentTimeMillis()));
            c.setTenantID(tenantId);
            c.setParked(true);
            ParkingSpot ps=generateParkingSpot(c);
            if(ps==null)
            {
                return Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("Error", "No available parking spot was found!")
                        .build();
            }
            c.setParkingSpotID(ps.getId());




            String insert="insert into customer values("+c.getId()+","+c.getTenantID()+" , '"+c.getVehicleNum()+"' , '"+c.getEntryTime()+"' , "
                    +c.getExitTime()+" , "+c.getParkingSpotID()+" , "+c.isParked()+" , "+(c.getVehicleType()==VehicleType.CAR?0:1)+" );";
            System.out.println(insert);
            stmt.executeUpdate(insert);

            TicketResource.addTicket(tenantId,new Ticket(c.getId()));

            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Response
                    .status(400)
                    .header("Error",e.getMessage())
                    .entity(c)
                    .build();
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(c.getId())).build();
//        Tenant t=new Tenant(tenant.getTenantID(),tenant.getOrgName(),tenant.getUserName());
        return Response.
                created(uri)
                .entity(c)
                .build();

    }

    @PUT
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unparkCustomer(@PathParam("tenID") long tenantId, @PathParam("customerId") long customerId)
    {
        Customer customer=null;
        try
        {
            Statement stmt=db.getC().createStatement();
            customer=(Customer)getCustomerWithID(tenantId,customerId).getEntity();
            if(!customer.isParked())
            {
                return Response
                        .noContent()
                        .header("Error","Customer already unparked!")
                        .build();
            }


            Timestamp exitTime=new Timestamp(System.currentTimeMillis());
            customer.setExitTime(exitTime);
            customer.setParked(false);
            paymentResource.calculatePrice(tenantId,customer);
            parkingSpotResource.changeReservedStatus(tenantId,customer.getParkingSpotID(),false);


            String update="update customer set exittime='"+exitTime+"', isparked="+false+" where tenantid="+tenantId+" and id="+customerId+";";
            stmt.executeUpdate(update);
            System.out.println(update);
            stmt.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return Response
                .ok()
                .header("Customer id"+customerId,"Unparked")
                .entity(customer)
                .build();
    }

    private ParkingSpot generateParkingSpot(Customer c)
    {

        String type=c.getVehicleType()==VehicleType.CAR?"CAR":"BIKE";
        ParkingSpot[] parkingSpots= (ParkingSpot[]) parkingSpotResource.getAllParkingSpots(c.getTenantID(),type).getEntity();
        long level=-1,ps=-1;
        ParkingSpot parkingSpot=null;
        for(ParkingSpot p:parkingSpots)
        {
            if(!p.isReserved()) {
                if (p.getLevel() > level) {
                    level = p.getLevel();
                    parkingSpot = p;
                } else if (p.getLevel() == level) {
                    if (p.getId() > ps) {
                        ps = p.getId();
                        parkingSpot=p;
                    }
                }
            }
        }


        if(parkingSpot!=null) {


            boolean spotAvailabilityChange = parkingSpotResource.changeReservedStatus(c.getTenantID(), parkingSpot.getId(),true);
            return parkingSpot;
        }
        return null;

    }
}
