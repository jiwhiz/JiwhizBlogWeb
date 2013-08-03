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
package com.jiwhiz.blog.web.account;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.CommentPostService;
import com.jiwhiz.blog.web.CommentNotificationSender;
import com.jiwhiz.blog.web.dto.CommentPostDTO;

/**
 * RESTful Service for current logged in user post comment to blog.
 * 
 * <p>
 * API: '<b>rest/myAccount/blogs/{blogId}/comment</b>'
 * </p>
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/rest/myAccount/blogs")
public class BlogCommentRestService {
    private static final Logger logger = LoggerFactory.getLogger(BlogCommentRestService.class);

    private final UserAccountRepository userAccountRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserAccountService userAccountService;
    private final CommentPostService commentPostService;
    private final CommentNotificationSender commentNotificationSender;

    @Inject
    public BlogCommentRestService(UserAccountRepository userAccountRepository, BlogPostRepository blogPostRepository,
            UserAccountService userAccountService, CommentPostService commentPostService,
            CommentNotificationSender commentNotificationSender) {
        this.userAccountRepository = userAccountRepository;
        this.blogPostRepository = blogPostRepository;
        this.userAccountService = userAccountService;
        this.commentPostService = commentPostService;
        this.commentNotificationSender = commentNotificationSender;
    }

    @RequestMapping(value = "/{blogId}/comment", method = RequestMethod.POST)
    @ResponseBody
    public CommentPostDTO postComment(@PathVariable("blogId") String blogId, @RequestBody CommentPostDTO CommentPostDTO) {
        logger.debug("==>RestAccountController.postComment() for blogId =" + blogId);
        UserAccount currentUser = userAccountService.getCurrentUser();
        BlogPost blogPost = blogPostRepository.findByPostId(blogId);
        if (blogPost == null || currentUser == null) {
            return null;
        }

        CommentPost comment = commentPostService.postComment(currentUser, blogPost, CommentPostDTO.getContent());

        if (this.commentNotificationSender != null) {
            UserAccount author = userAccountRepository.findOne(blogPost.getAuthorKey());
            this.commentNotificationSender.send(author, currentUser, comment, blogPost);
        }

        return new CommentPostDTO(comment, currentUser, null);
    }

}
