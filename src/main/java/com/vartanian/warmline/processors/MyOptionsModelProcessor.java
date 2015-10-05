package com.vartanian.warmline.processors;

import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.ModelProcessor;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceModel;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by super on 10/5/15.
 */
@Provider
public class MyOptionsModelProcessor implements ModelProcessor {

    @Override
    public ResourceModel processResourceModel(ResourceModel resourceModel, Configuration configuration) {
        // we get the resource model and we want to enhance each resource by OPTIONS method
        ResourceModel.Builder newResourceModelBuilder = new ResourceModel.Builder(false);
        for (final Resource resource : resourceModel.getResources()) {
            // for each resource in the resource model we create a new builder which is based on the old resource
            final Resource.Builder resourceBuilder = Resource.builder(resource);

            // we add a new OPTIONS method to each resource
            // note that we should check whether the method does not yet exist to avoid failures
            resourceBuilder.addMethod("OPTIONS")
                    .handledBy(new Inflector<ContainerRequestContext, String>() {
                        @Override
                        public String apply(ContainerRequestContext containerRequestContext) {
                            return "On this path the resource with " + resource.getResourceMethods().size()
                                    + " methods is deployed.";
                        }
                    }).produces(MediaType.TEXT_PLAIN)
                    .extended(true); // extended means: not part of core RESTful API

            // we add to the model new resource which is a combination of the old resource enhanced
            // by the OPTIONS method
            newResourceModelBuilder.addResource(resourceBuilder.build());
        }

        final ResourceModel newResourceModel = newResourceModelBuilder.build();
        // and we return new model
        return newResourceModel;
    };

    @Override
    public ResourceModel processSubResource(ResourceModel subResourceModel, Configuration configuration) {
        // we just return the original subResourceModel which means we do not want to do any modification
        return subResourceModel;
    }
}