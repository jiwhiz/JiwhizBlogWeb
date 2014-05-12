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
package com.jiwhiz.config;

import javax.inject.Inject;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * Configuration for MongoDB.
 * 
 * @author Yuan Ji
 */
@Configuration
@EnableMongoAuditing(auditorAwareRef="auditorAware")
@EnableMongoRepositories(basePackages = "com.jiwhiz.domain")
public class MongoConfig extends AbstractMongoConfiguration {
    @Inject
    private Environment environment;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        Mongo mongo = new MongoClient(environment.getProperty("mongoDb.host"));
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
    }

    @Override
    protected String getDatabaseName() {
       return environment.getProperty("mongoDb.name");
    }
    
    @Override
    protected String getMappingBasePackage() {
        return "com.corpavinc.fbng.domain";
    }
    
    @Bean(name={"mappingContext"})
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        return super.mongoMappingContext();
    }

    @Override
    public MongoDbFactory mongoDbFactory() throws Exception {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        if (cloudEnvironment.isCloudFoundry()) {
            MongoServiceInfo serviceInfo = cloudEnvironment.getServiceInfo(environment.getProperty("mongoDb.serviceName"), MongoServiceInfo.class);
            MongoServiceCreator serviceCreator = new MongoServiceCreator();
            return serviceCreator.createService(serviceInfo);
        } else {
            return super.mongoDbFactory();
        }
    }

}
