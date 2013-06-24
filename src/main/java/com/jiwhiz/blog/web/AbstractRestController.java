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
package com.jiwhiz.blog.web;

import javax.inject.Inject;

import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.BlogPostService;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.post.CommentPostService;
import com.jiwhiz.blog.domain.post.SlidePostRepository;
import com.jiwhiz.blog.domain.post.SlidePostService;
import com.jiwhiz.blog.domain.system.CounterService;

public class AbstractRestController {
    @Inject
    protected UserAccountRepository userAccountRepository;
    
    @Inject
    protected UserSocialConnectionRepository userSocialConnectionRepository;
    
    @Inject
    protected UserAccountService userAccountService;
    
    @Inject
    protected BlogPostRepository blogPostRepository;
    
    @Inject
    protected BlogPostService blogPostService;
    
    @Inject
    protected CommentPostRepository commentPostRepository;
    
    @Inject
    protected CommentPostService commentPostService;
    
    @Inject
    protected SlidePostRepository slidePostRepository;
    
    @Inject
    protected SlidePostService slidePostService;
    
    @Inject
    protected CounterService counterService;

}
