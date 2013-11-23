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

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jiwhiz.blog.domain.account.UserAccount;
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
@EnableMongoAuditing(auditorAwareRef="authenticatedAuditor")
@EnableMongoRepositories(basePackages = "com.jiwhiz.blog.domain")
public class MongoConfig extends AbstractMongoConfiguration {
    @Inject
    private Environment environment;
    
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        Mongo mongo = new MongoClient(environment.getProperty("mongodb.host"));
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
    }

    @Override
    protected String getDatabaseName() {
       return environment.getProperty("mongodb.dbname");
    }
    
    @Override
    protected String getMappingBasePackage() {
        return "com.jiwhiz.blog.domain";
    }
    
    @Bean(name={"mappingContext"})
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        return super.mongoMappingContext();
    }

// TODO: wait for DATAMONGO-802
// https://jira.springsource.org/browse/DATAMONGO-802
//    @Bean
//    public MongoDbFactory mongoDbFactory() throws Exception {
//        CloudEnvironment cloudEnvironment = new CloudEnvironment();
//
//        if (cloudEnvironment.isCloudFoundry()) {
//            List<MongoServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(MongoServiceInfo.class);
//            MongoServiceCreator serviceCreator = new MongoServiceCreator();
//            return serviceCreator.createService(serviceInfo.get(0));
//        } else {
//            return super.mongoDbFactory();
//        }
//    }

    @Bean
    public AuditorAware<String> authenticatedAuditor() {
        return new AuditorAware<String>() {
            public String getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                  return null;
                }

                return ((UserAccount) authentication.getPrincipal()).getUserId();
            }
        };
    }
}
