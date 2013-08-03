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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.CommentNotificationSender;
import com.jiwhiz.blog.web.ContactForm;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.SystemMessageSender;

/**
 * Local Test configuration. Use mock email service.
 * 
 * @author Yuan Ji
 */
@Configuration
@Profile("local")
@PropertySource(value = { "classpath:localConfig.properties" })
public class LocalConfig {
    @Bean
    public ContactMessageSender contactMessageSender() {
        return new ContactMessageSender() {
            @Override
            public void send(ContactForm contact) {
                System.out.println(String.format("Send email message to jiwhiz. From  %s: \" %s \"", contact.getName(),
                        contact.getMessage()));
            }
        };
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        return new CommentNotificationSender() {
            @Override
            public void send(UserAccount receivingUser, UserAccount commentUser, CommentPost comment, BlogPost blog) {
                System.out.println(String.format(
                        "Send email message to '%s': %s posted a comment to blog '%s': \" %s \"",
                        receivingUser.getDisplayName(), commentUser.getDisplayName(), blog.getTitle(),
                        comment.getContent()));
            }
        };
    }

    @Bean
    public SystemMessageSender systemMessageSender() {
        return new SystemMessageSender() {
            @Override
            public void sendNewUserRegistered(UserAccount user) {
                System.out.println(String.format(
                        "Send email message to admin: a new user '%s' was registered.", user.getDisplayName()));
            }

            @Override
            public void sendNewPostPublished(UserAccount author, BlogPost blog) {
                System.out.println(String.format("'%s' published a new blog. See %s", 
                        author.getDisplayName(), blog.getFullPublishedPath()));                
            }

        };
    }

}