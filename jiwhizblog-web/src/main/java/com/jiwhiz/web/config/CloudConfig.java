/* 
 * Copyright 2013-2015 JIWHIZ Consulting Inc.
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

import javax.inject.Inject;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;

import com.jiwhiz.mail.EmailService;
import com.jiwhiz.mail.sendgrid.EmailServiceImpl;

/**
 * @author Yuan Ji
 */
@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
    @Inject
    private Environment environment;

    @Bean
    public EmailService emailService() {
        String sendgridServiceName = environment.getProperty("sendgrid.serviceName");
        String credentialPath = "vcap.services." + sendgridServiceName + ".credentials.";
        return new EmailServiceImpl(environment.getProperty(credentialPath + "username"), 
                                    environment.getProperty(credentialPath + "password"));
    }
    
    @Bean
    public MongoDbFactory mongoDbFactory() {
        return connectionFactory().mongoDbFactory(environment.getProperty("mongoDb.serviceName"));
    }
}
