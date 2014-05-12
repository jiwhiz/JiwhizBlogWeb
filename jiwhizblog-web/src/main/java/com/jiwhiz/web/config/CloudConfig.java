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

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.jiwhiz.mail.CommentNotificationSender;
import com.jiwhiz.mail.ContactMessageSender;
import com.jiwhiz.mail.SystemMessageSender;
import com.jiwhiz.mail.sendgrid.AbstractMailSender;
import com.jiwhiz.mail.sendgrid.CommentNotificationSenderImpl;
import com.jiwhiz.mail.sendgrid.ContactMessageSenderImpl;
import com.jiwhiz.mail.sendgrid.SystemMessageSenderImpl;

/**
 * @author Yuan Ji
 */
@Configuration
@Profile("cloud")
@PropertySource(value={"classpath:cloud.properties", "classpath:sendgrid.properties"})
public class CloudConfig {
    @Inject
    private Environment environment;

    @Bean
    public ContactMessageSender contactMessageSender() {
        ContactMessageSenderImpl sender = new ContactMessageSenderImpl();
        configSendGrid(sender);
        return sender;
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        CommentNotificationSenderImpl sender = new CommentNotificationSenderImpl();
        configSendGrid(sender);
        return sender;
    }
    
    @Bean
    public SystemMessageSender systemMessageSender() {
        SystemMessageSenderImpl sender = new SystemMessageSenderImpl();
        configSendGrid(sender);
        return sender;
    }

    private void configSendGrid(AbstractMailSender sender) {
        sender.setSendGridUsername(environment.getProperty("sendgrid.username"));
        sender.setSendGridPassword(environment.getProperty("sendgrid.password"));
        sender.setSystemName(environment.getProperty("application.systemName"));
        sender.setSystemEmail(environment.getProperty("application.systemEmail"));
        sender.setAdminName(environment.getProperty("application.adminName"));
        sender.setAdminEmail(environment.getProperty("application.adminEmail"));
        sender.setApplicationBaseUrl(environment.getProperty("application.baseUrl"));
    }
}
