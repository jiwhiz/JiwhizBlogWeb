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
package com.jiwhiz.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.mail.CommentNotificationSender;
import com.jiwhiz.mail.ContactMessageSender;
import com.jiwhiz.mail.SystemMessageSender;

/**
 * @author Yuan Ji
 */
@Configuration
public class TestRestServiceConfig {    
    @Bean
    public UserAccountRepository userAccountRepositoryMock() {
        return Mockito.mock(UserAccountRepository.class);
    }

    @Bean
    public UserSocialConnectionRepository userSocialConnectionRepositoryMock() {
        return Mockito.mock(UserSocialConnectionRepository.class);
    }
    
    @Bean
    public CommentPostRepository commentPostRepositoryMock() {
        return Mockito.mock(CommentPostRepository.class);
    }

    @Bean
    public BlogPostRepository blogPostRepositoryMock() {
        return Mockito.mock(BlogPostRepository.class);
    }

    @Bean
    public UserAccountService userAccountServiceMock() {
        return Mockito.mock(UserAccountService.class);
    }
    
    @Bean
    public ContactMessageSender contactMessageSender() {
        return Mockito.mock(ContactMessageSender.class);
    }

    @Bean
    public SystemMessageSender systemMessageSender() {
        return Mockito.mock(SystemMessageSender.class);
    }

    @Bean
    public CommentNotificationSender commentNotificationSender() {
        return Mockito.mock(CommentNotificationSender.class);
    }

}
