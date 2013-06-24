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

import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * Configuration for MongoDB.
 * 
 * @author Yuan Ji
 * 
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.jiwhiz.blog.domain")
public class MongoConfig {
    @Inject
    private Environment environment;
    
    public Mongo mongo() throws Exception {
        Mongo mongo = new MongoClient("localhost");
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        if (cloudEnvironment.isCloudFoundry()) {
            List<MongoServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(MongoServiceInfo.class);
            MongoServiceCreator serviceCreator = new MongoServiceCreator();
            return serviceCreator.createService(serviceInfo.get(0));
        } else {
            return new SimpleMongoDbFactory(mongo(), environment.getProperty("mongodb.dbname"));
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
