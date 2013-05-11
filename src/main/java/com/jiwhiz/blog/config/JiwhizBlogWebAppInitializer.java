/* 
 * Copyright 2013 JIWHIZ Consulting Inc.
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
package com.jiwhiz.blog.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Code-based alternative to web.xml for use within Servlet 3.0+ environments. See
 * {@link WebApplicationInitializer} Javadoc for complete details.
 * 
 * @author Yuan Ji
 *
 */
public class JiwhizBlogWebAppInitializer implements WebApplicationInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.scan("com.jiwhiz.blog.config");
        root.getEnvironment().setDefaultProfiles("local");

        // Manages the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(root));

        // Enables support for DELETE and PUT request methods with web browser clients
        servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class)
            .addMappingForUrlPatterns(null, false, "/*");

        // Secures the application
        servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
            .addMappingForUrlPatterns(null, false, "/*");

        // Handles requests into the application
        ServletRegistration.Dynamic appServlet =
                servletContext.addServlet("JiwhizBlogWeb", new DispatcherServlet(new GenericWebApplicationContext()));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
	}

}
