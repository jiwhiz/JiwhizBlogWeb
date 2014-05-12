/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jiwhiz.web.config;

import javax.servlet.Filter;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import com.jiwhiz.config.MongoConfig;

/**
 * @author Yuan Ji
 */
public class JiwhizApplicationInitializer extends AbstractDispatcherServletInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        //set active profile for cloud foundry
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        if (cloudEnvironment.isCloudFoundry()) {
            rootAppContext.getEnvironment().setActiveProfiles("cloud");
        }
        rootAppContext.register( JiwhizAppConfig.class, MongoConfig.class, SecurityConfig.class, SocialConfig.class,
                CloudConfig.class, LocalConfig.class);
        return rootAppContext;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext servletAppContext =
                new AnnotationConfigWebApplicationContext();
        servletAppContext.register( WebConfig.class );
        return servletAppContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{
                new DelegatingFilterProxy("springSecurityFilterChain"), 
                new MultipartFilter()
                };
    }

    @Override
    protected String getServletName() {
        return "JiwhizWeb";
    }
}
