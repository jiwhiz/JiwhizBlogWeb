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
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.jiwhiz.blog.domain.account.AccountService;
import com.jiwhiz.blog.domain.account.AccountServiceImpl;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.account.UserAdminServiceImpl;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.BlogPostService;
import com.jiwhiz.blog.domain.post.BlogPostServiceImpl;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.post.CommentPostService;
import com.jiwhiz.blog.domain.post.CommentPostServiceImpl;
import com.jiwhiz.blog.domain.post.SlidePostRepository;
import com.jiwhiz.blog.domain.post.SlidePostService;
import com.jiwhiz.blog.domain.post.SlidePostServiceImpl;
import com.jiwhiz.blog.domain.system.AccessRecordRepository;
import com.jiwhiz.blog.domain.system.AdminService;
import com.jiwhiz.blog.domain.system.AdminServiceImpl;
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
    private Environment environment;

    //Repository beans injected from MongoConfig
    @Inject
    private UserAccountRepository accountRepository;
    @Inject
    private BlogPostRepository blogPostRepository;
    @Inject
    private CommentPostRepository commentPostRepository;
    @Inject
    private SlidePostRepository slidePostRepository;
    @Inject
    private AccessRecordRepository accessRecordRepository;
    @Inject
    private UserSocialConnectionRepository userSocialConnectionRepository;

    //Application Service beans
    @Bean
    public AccountService accountService(MongoTemplate mongoTemplate, UserAccountRepository accountRepository,
                    UserSocialConnectionRepository userSocialConnectionRepository) {
        AccountServiceImpl service = new AccountServiceImpl(accountRepository, userSocialConnectionRepository,
                userAdminService(mongoTemplate));
        return service;
    }

    @Bean
    public BlogPostService blogPostService(MongoTemplate mongoTemplate) {
        BlogPostServiceImpl service = new BlogPostServiceImpl(accountRepository, blogPostRepository, 
                commentPostRepository, userAdminService(mongoTemplate), counterService(mongoTemplate));
        return service;
    }

    @Bean
    public CommentPostService commentPostService(MongoTemplate mongoTemplate) {
        CommentPostServiceImpl service = new CommentPostServiceImpl(accountRepository, blogPostRepository, 
                commentPostRepository, userAdminService(mongoTemplate), counterService(mongoTemplate));
        return service;
    }
    
    @Bean
    public SlidePostService slidePostService(MongoTemplate mongoTemplate) {
        SlidePostServiceImpl service = new SlidePostServiceImpl(accountRepository, slidePostRepository, 
                userAdminService(mongoTemplate), counterService(mongoTemplate));
        return service;
    }

    @Bean
    public AdminService adminService() {
        AdminServiceImpl service = new AdminServiceImpl(accessRecordRepository);
        return service;
    }

    @Bean
    public UserAdminService userAdminService(MongoTemplate mongoTemplate) {
        return new UserAdminServiceImpl(accountRepository, counterService(mongoTemplate));
    }

    @Bean
    public CounterService counterService(MongoTemplate mongoTemplate) {
        return new CounterServiceImpl(mongoTemplate);
    }

}
