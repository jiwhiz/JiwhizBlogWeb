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

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

/**
 * Configuration for MongoDB.
 * 
 * @author Yuan Ji
 * 
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.jiwhiz.blog.domain")
public class MongoConfig extends AbstractMongoConfiguration {
    @Inject
    private Environment environment;
    
    @Override
    public String getDatabaseName() {
        return environment.getProperty("mongodb.dbname");
    }
    
    @Override
    protected UserCredentials getUserCredentials() {
        return new UserCredentials(environment.getProperty("mongodb.username"), environment.getProperty("mongodb.password"));
    }

    @Override
    public String getMappingBasePackage() {
        return "com.jiwhiz.blog.domain";
    }

    @Override
    public Mongo mongo() throws Exception {
        Mongo mongo = new Mongo("localhost");
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
    }
}
