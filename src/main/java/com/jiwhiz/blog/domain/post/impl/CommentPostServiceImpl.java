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
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.post.CommentPostService;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for CommentPostService.
 * 
 * @author Yuan Ji
 * 
 */
public class CommentPostServiceImpl extends AbstractPostServiceImpl implements CommentPostService {
    final static Logger logger = LoggerFactory.getLogger(CommentPostServiceImpl.class);
    public static final String COMMENT_POST_ID_PREFIX = "comment";

    private final CommentPostRepository commentPostRepository;

    @Inject
    public CommentPostServiceImpl(UserAccountRepository accountRepository, 
            CommentPostRepository commentPostRepository, UserAccountService userAccountService,
            CounterService counterService) {
        super(accountRepository, userAccountService, counterService);
        this.commentPostRepository = commentPostRepository;
    }
    
    @Override
    public CommentPost postComment(UserAccount user, BlogPost blogPost, String content) {
        String postId = COMMENT_POST_ID_PREFIX + this.counterService.getNextCommentPostIdSequence();
        CommentPost comment = new CommentPost(postId, user, blogPost, content);
        logger.debug(String.format("Add comment to blog (blogId='%s'), by %s (userId='%s'.",
                blogPost.getPostId(), user.getDisplayName(), user.getUserId()));

        if (user.isTrustedAccount()) {
            comment.approve();
        } 

        return commentPostRepository.save(comment);
    }

}
