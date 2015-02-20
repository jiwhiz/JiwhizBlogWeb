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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.jiwhiz.mail.EmailMessage;
import com.jiwhiz.mail.EmailService;

/**
 * @author Yuan Ji
 */
@Configuration
@Profile("local")
public class LocalConfig {
    @Bean
    public EmailService emailService() {
        return new EmailService() {
            @Override
            public void sendEmail(EmailMessage message) {
                System.out.println(String.format("Local test: Send email to %s. From  %s: \" %s \"", 
                		message.getToEmail(), message.getFromEmail(), message.getMessage()));
            }
            
        };
    }
}
