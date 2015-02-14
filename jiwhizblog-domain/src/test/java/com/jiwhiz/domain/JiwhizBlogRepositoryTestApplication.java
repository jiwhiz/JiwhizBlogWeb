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
package com.jiwhiz.domain;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.Mongo;

import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;

@SpringBootApplication
@EnableMongoAuditing(auditorAwareRef="auditorAware")
public class JiwhizBlogRepositoryTestApplication {
    public static final String TEST_AUDITOR = "UnitTester";
    
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            public String getCurrentAuditor() {
                return TEST_AUDITOR;
            }
        };
    }
    
    @Bean(destroyMethod="close")
    public Mongo mongo() throws IOException {
        return new EmbeddedMongoBuilder()
                .version("2.4.5")
                .bindIp("127.0.0.1")
                .port(12345)
                .build();
    }
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(JiwhizBlogRepositoryTestApplication.class, args);
    }

}
