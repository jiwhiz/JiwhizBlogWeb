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
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.DefaultAlphaMailService;
import com.jiwhiz.blog.web.CommentNotificationSender;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.SystemMessageSender;
import com.jiwhiz.blog.web.mail.AbstractMailSender;
import com.jiwhiz.blog.web.mail.CommentNotificationSenderImpl;
import com.jiwhiz.blog.web.mail.ContactMessageSenderImpl;
import com.jiwhiz.blog.web.mail.SystemMessageSenderImpl;

/**
 * Cloud Deployment configuration. Use real email service.
 * 
 * @author Yuan Ji
 */
@Configuration
@Profile("cloud")
@PropertySource(value={"classpath:cloudConfig.properties", "classpath:alphamail.properties"})
public class CloudConfig {
    @Inject
    private Environment environment;

    @Bean
    public AlphaMailService alphaMailService() {
        DefaultAlphaMailService mailService = new DefaultAlphaMailService();
        mailService.setServiceUrl(environment.getProperty("alphamail.url"));
        mailService.setApiToken(environment.getProperty("alphamail.token"));
        return mailService;
    }

    @Bean
    public ContactMessageSender contactMessageSender() {
        ContactMessageSenderImpl sender = new ContactMessageSenderImpl(alphaMailService());
        sender.setProjectId(environment.getProperty("alphamail.project.contactMessage.id", int.class));
        configEmailSender(sender);
        return sender;
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        CommentNotificationSenderImpl sender = new CommentNotificationSenderImpl(alphaMailService());
        sender.setProjectId(environment.getProperty("alphamail.project.comment.notification.id", int.class));
        configEmailSender(sender);
        return sender;
    }
    
    @Bean
    public SystemMessageSender systemMessageSender() {
        SystemMessageSenderImpl sender = new SystemMessageSenderImpl(alphaMailService());
        sender.setProjectId(environment.getProperty("alphamail.project.system.message.id", int.class));
        configEmailSender(sender);
        return sender;
    }

    private void configEmailSender(AbstractMailSender sender) {
        sender.setSystemName(environment.getProperty("application.systemName"));
        sender.setSystemEmail(environment.getProperty("application.systemEmail"));
        sender.setAdminName(environment.getProperty("application.adminName"));
        sender.setAdminEmail(environment.getProperty("application.adminEmail"));
        sender.setApplicationBaseUrl(environment.getProperty("application.baseUrl"));
    }
}