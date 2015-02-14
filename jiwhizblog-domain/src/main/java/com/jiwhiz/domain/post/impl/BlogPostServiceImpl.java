/* 
 * Copyright 2013-2015 JIWHIZ Consulting Inc.
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
package com.jiwhiz.domain.post.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.BlogPostService;

/**
 * Implementation for BlogPostService.
 * 
 * @author Yuan Ji
 * 
 */
@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Inject
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public BlogPost createPost(UserAccount author, String title, String content, String tagString) {
        BlogPost post = new BlogPost(author.getId(), title, content, tagString);
        return blogPostRepository.save(post);
    }
}
