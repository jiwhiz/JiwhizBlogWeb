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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.account.impl.UserAccountServiceImpl;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.BlogPostService;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.post.CommentPostService;
import com.jiwhiz.blog.domain.post.SlidePostRepository;
import com.jiwhiz.blog.domain.post.SlidePostService;
import com.jiwhiz.blog.domain.post.impl.BlogPostServiceImpl;
import com.jiwhiz.blog.domain.post.impl.CommentPostServiceImpl;
import com.jiwhiz.blog.domain.post.impl.SlidePostServiceImpl;
import com.jiwhiz.blog.domain.system.CounterService;
import com.jiwhiz.blog.domain.system.CounterServiceImpl;

/**
 * Configuration for application services and related beans.
 * 
 * @author Yuan Ji
 * 
 */
@Configuration
class MainAppConfig {

    @Inject
    private UserAccountRepository accountRepository;
    @Inject
    private BlogPostRepository blogPostRepository;
    @Inject
    private CommentPostRepository commentPostRepository;
    @Inject
    private SlidePostRepository slidePostRepository;
    @Inject
    private MongoTemplate mongoTemplate;
    
    @Bean
    public UserAccountService userAccountService() {
        return new UserAccountServiceImpl(accountRepository, counterService(), new AuthenticationNameUserIdSource());
    }

    @Bean
    public BlogPostService blogPostService() {
        return new BlogPostServiceImpl(accountRepository, blogPostRepository, commentPostRepository,
                userAccountService(), counterService());
    }

    @Bean
    public CommentPostService commentPostService() {
        return new CommentPostServiceImpl(accountRepository, commentPostRepository, userAccountService(),
                counterService());
    }

    @Bean
    public SlidePostService slidePostService() {
        return new SlidePostServiceImpl(accountRepository, slidePostRepository, userAccountService(), counterService());
    }

    @Bean
    public CounterService counterService() {
        return new CounterServiceImpl(mongoTemplate);
    }

}
