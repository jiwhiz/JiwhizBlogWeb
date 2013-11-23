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
package com.jiwhiz.blog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * @author Yuan Ji
 *
 */
@Configuration
@EnableMongoRepositories(basePackages="com.jiwhiz.blog.domain")
@EnableMongoAuditing(auditorAwareRef="testAuditor")
public class TestConfig extends AbstractMongoConfiguration {
    public static final String TEST_AUDITOR = "UnitTester";
    
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        Mongo mongo = new MongoClient("localhost");
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
    }

    @Override
    protected String getDatabaseName() {
       return "test";
    }
    
    @Override
    protected String getMappingBasePackage() {
        return "com.jiwhiz.blog.domain";
    }
    
    @Bean(name={"mappingContext"})
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        return super.mongoMappingContext();
    }
    
    @Bean
    public AuditorAware<String> testAuditor() {
        return new AuditorAware<String>() {
            public String getCurrentAuditor() {
                return TEST_AUDITOR;
            }
        };
    }
}
