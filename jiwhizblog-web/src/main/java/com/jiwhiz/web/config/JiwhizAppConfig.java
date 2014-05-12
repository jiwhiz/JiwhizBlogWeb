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
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.account.impl.UserAccountServiceImpl;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.BlogPostService;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentPostService;
import com.jiwhiz.domain.post.impl.BlogPostServiceImpl;
import com.jiwhiz.domain.post.impl.CommentPostServiceImpl;
import com.jiwhiz.domain.system.CounterService;
import com.jiwhiz.domain.system.impl.CounterServiceImpl;

/**
 * @author Yuan Ji
 */
@Configuration
public class JiwhizAppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer () {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Inject
    private UserAccountRepository accountRepository;
    @Inject
    private BlogPostRepository blogPostRepository;
    @Inject
    private CommentPostRepository commentPostRepository;
    @Inject
    private MongoTemplate mongoTemplate;

    @Bean
    public UserAccountService userAccountService() {
        return new UserAccountServiceImpl(accountRepository, counterService(), new AuthenticationNameUserIdSource());
    }

    @Bean
    public BlogPostService blogPostService() {
        return new BlogPostServiceImpl(blogPostRepository);
    }

    @Bean
    public CommentPostService commentPostService() {
        return new CommentPostServiceImpl(commentPostRepository);
    }

    @Bean
    public CounterService counterService() {
        return new CounterServiceImpl(mongoTemplate);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            public String getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                  return null;
                }

                return authentication.getName();
            }
        };
    }

}
