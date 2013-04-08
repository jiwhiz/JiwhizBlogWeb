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

import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.CommentNotificationSender;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.site.ContactForm;

/**
 * Local Test configuration. Use mock email service.
 * 
 * @author Yuan Ji
 */
@Configuration
@Profile("local")
@PropertySource(value={"classpath:localConfig.properties"})
public class LocalConfig {
    @Bean
    public ContactMessageSender contactMessageSender() {
        return new ContactMessageSender(){
            public void send(ContactForm contact){
                System.out.println(String.format("Send email message to jiwhiz. From  %s: \" %s \""
                                , contact.getName(), contact.getMessage()));
            }
        };
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        return new CommentNotificationSender(){
            public void send(CommentPost comment){
                System.out.println(String.format("Send email message to author. %s posted a comment to your blog '%s': \" %s \""
                                , comment.getAuthorAccount().getDisplayName(), comment.getBlogPost().getTitle(), comment.getContent()));
            }
        };
    }

}