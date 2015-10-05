package com.vartanian.warmline.example;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vartanian.warmline.annotations.Compress;
import com.vartanian.warmline.beans.MyBean;
import com.vartanian.warmline.exeptions.CustomNotFoundException;
import com.vartanian.warmline.filters.AuthorizationRequestFilter;
import com.vartanian.warmline.filters.PoweredByResponseFilter;
import com.vartanian.warmline.interceptors.CompressionDynamicBinding;
import com.vartanian.warmline.interceptors.GZIPReaderInterceptor;
import com.vartanian.warmline.interceptors.GZIPWriterInterceptor;
import com.vartanian.warmline.processors.MyOptionsModelProcessor;
import com.vartanian.warmline.validators.ValidationConfigurationContextResolver;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.glassfish.jersey.server.JSONP;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.validator.constraints.Email;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by super on 10/1/15.
 */
// The Java class will be hosted at the URI path "/test"
@Path("/test")
public class HelloWorld {

    @Context
    private MessageBodyWorkers workers;
    @Context
    UriInfo uriInfo;
    @InjectLinks({@InjectLink(resource=HelloWorld.class, rel = "self")})
    List<Link> links;

    public static void main(String[] args) throws Exception {

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
//        ResourceConfig config = new ResourceConfig(MainConfig.class, HelloWorld.class);
        ResourceConfig config = new ResourceConfig();
        config.packages("com.vartanian.warmline");
        config.register(JacksonJsonProvider.class);
        config.register(PoweredByResponseFilter.class);
        config.register(LoggingFilter.class);
        config.register(AuthorizationRequestFilter.class);
        config.register(GZIPWriterInterceptor.class);
        config.register(GZIPReaderInterceptor.class);
        config.register(CompressionDynamicBinding.class);
        config.register(MyOptionsModelProcessor.class);
        config.register(ValidationConfigurationContextResolver.class);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        try {
//            httpServer.start();

            System.out.println("Press any key to stop the server...");
            System.in.read();
        } finally {
            System.out.println("Stopping transport...");
            // stop the transport
            httpServer.shutdownNow();

            System.out.println("Stopped transport...");
        }

    }

    // The Java method will process HTTP GET requests
    @GET
    @Produces("text/plain")
    public String getClichedMessage() {

        // Return some cliched textual content
        return "Привет Мир!!!";
    }

    @GET
    @Path("/images/{image}")
    @Produces("image/*")
    public Response getImage(@PathParam("image") String image) {

        File f = new File("/home/super/Завантаження/imgo.jpeg");

        if (!f.exists()) {
            throw new CustomNotFoundException("Item, " + image + ", is not found");
        }

        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }

    @GET
    @Produces("application/xml")
    @Compress
    public String getMyBean() {

        final MyBean myBean = new MyBean("Hello World!", 42);

        // buffer into which myBean will be serialized
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // get most appropriate MBW
        final MessageBodyWriter<MyBean> messageBodyWriter =
                workers.getMessageBodyWriter(MyBean.class, MyBean.class,
                        new Annotation[]{}, MediaType.APPLICATION_XML_TYPE);

        try {
            // use the MBW to serialize myBean into baos
            messageBodyWriter.writeTo(myBean,
                    MyBean.class, MyBean.class, new Annotation[] {},
                    MediaType.APPLICATION_XML_TYPE, new MultivaluedHashMap<String, Object>(),
                    baos);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error while serializing MyBean.", e);
        }

        final String stringXmlOutput = baos.toString();
        // stringXmlOutput now contains XML representation:
        // "<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        // <myBean><anyString>Hello World!</anyString>
        // <anyNumber>42</anyNumber></myBean>"

        return stringXmlOutput;
    }

    @POST
    @Consumes("application/xml")
    public String postMyBean(MyBean myBean) {
        return myBean.anyString;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MyBean postMyBean() {
        return new MyBean("Hello World!", 41);
    }

    @GET
    @JSONP(callback = "eval", queryParam = "jsonpCallback")
    @Produces({"application/javascript"})
    public MyBean getSimpleJSONP() {
        return new MyBean("jsonp", 7);
    }

    @POST
    @Path("StreamSource")
    public StreamSource getStreamSource(StreamSource streamSource) {
        return streamSource;
    }

    @POST
    @Path("SAXSource")
    public SAXSource getSAXSource(SAXSource saxSource) {
        return saxSource;
    }

    @POST
    @Path("DOMSource")
    public DOMSource getDOMSource(DOMSource domSource) {
        return domSource;
    }

    @GET
    @Consumes("text/plain")
    public String getData(@Context SecurityContext sc) throws URISyntaxException {
        return "Test";
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getServerSentEvents() throws IOException {
        final EventOutput eventOutput = new EventOutput();
        eventOutput.write(new OutboundEvent.Builder().data(String.class, "Это займет какое-то время").build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        // ... code that waits 1 second
                        Thread.sleep(1000);
                        final OutboundEvent.Builder eventBuilder
                                = new OutboundEvent.Builder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class,
                                "Hello world " + i + "!");
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                            "Error when writing the event.", e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(
                            "Error when writing the event.", e);
                } finally {
                    try {
                        eventOutput.close();
                    } catch (IOException ioClose) {
                        throw new RuntimeException(
                                "Error when closing the event output.", ioClose);
                    }
                }
            }
        }).start();
        return eventOutput;
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public void registerUser(@NotNull @FormParam("lastName") String lastName,
            @Email @FormParam("email") String email) {



    }
}
