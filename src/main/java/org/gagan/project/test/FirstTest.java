//package org.gagan.project.test;
//
//import org.gagan.project.resources.TenantResource;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.test.JerseyTest;
//import org.glassfish.jersey.test.TestProperties;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.Response;
//import java.io.*;
//import java.util.*;
//
//public class FirstTest extends JerseyTest {
//
//    @Override
//    protected Application configure() {
//        enable(TestProperties.LOG_TRAFFIC);
//        enable(TestProperties.DUMP_ENTITY);
//        return new ResourceConfig(TenantResource.class);
//    }
//    @Test
//    public void test()
//    {
//        Response response=target("/tenant/1").request()
//                .get();
//        Assert.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
//    }
//}
