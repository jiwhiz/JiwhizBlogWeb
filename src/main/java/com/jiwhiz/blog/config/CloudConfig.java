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
import com.jiwhiz.blog.web.CommentNotificationSenderImpl;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.ContactMessageSenderImpl;

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
        sender.setAdminName(environment.getProperty("alphamail.adminName"));
        sender.setAdminEmail(environment.getProperty("alphamail.adminEmail"));
        return sender;
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        CommentNotificationSenderImpl sender = new CommentNotificationSenderImpl(alphaMailService());
        sender.setProjectId(environment.getProperty("alphamail.project.comment.notification.id", int.class));
        sender.setAdminName(environment.getProperty("alphamail.adminName"));
        sender.setAdminEmail(environment.getProperty("alphamail.adminEmail"));
        return sender;
    }
}