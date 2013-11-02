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
package com.jiwhiz.blog.domain.post.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.BlogPostService;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for BlogPostService.
 * 
 * @author Yuan Ji
 *
 */
public class BlogPostServiceImpl extends AbstractPostServiceImpl implements BlogPostService {
    final static Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);
    public static final String BLOG_POST_ID_PREFIX = "blog";
    
    private final BlogPostRepository blogPostRepository;

    @Inject
    public BlogPostServiceImpl(UserAccountRepository accountRepository, BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, UserAccountService userAdminService,
            CounterService counterService) {
        super(accountRepository, userAdminService, counterService);
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public BlogPost createPost(UserAccount author, String title, String content, String tagString) {
        String postId = BLOG_POST_ID_PREFIX + this.counterService.getNextBlogPostIdSequence();
        BlogPost post = new BlogPost(postId, author, title, content, tagString);
        return blogPostRepository.save(post);
    }
}
