package com.vartanian.warmline.interceptors;

import com.vartanian.warmline.example.HelloWorld;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by super on 10/5/15.
 */
// This dynamic binding provider registers GZIPWriterInterceptor
// only for HelloWorldResource and methods that contain
// "VeryLongString" in their name. It will be executed during
// application initialization phase.
public class CompressionDynamicBinding implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (HelloWorld.class.equals(resourceInfo.getResourceClass()) && resourceInfo.getResourceMethod().getName().contains("postMyBean")) {
            context.register(GZIPWriterInterceptor.class);
        }
    }
}