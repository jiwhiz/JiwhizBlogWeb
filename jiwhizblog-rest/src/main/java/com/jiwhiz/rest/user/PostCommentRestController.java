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
package com.jiwhiz.rest.user;

import javax.inject.Inject;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.MessageSender;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Controller
public class PostCommentRestController extends AbstractUserRestController {
    private final UserAccountRepository userAccountRepository;
    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final MessageSender messageSender;

    @Inject
    public PostCommentRestController(
            UserAccountService userAccountService, 
            UserAccountRepository userAccountRepository, 
            BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, 
            MessageSender messageSender) {
        super(userAccountService);
        this.userAccountRepository = userAccountRepository;
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.messageSender = messageSender;
    }

    @RequestMapping(method = RequestMethod.POST, value = ApiUrls.URL_USER_BLOGS_BLOG_COMMENTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<CommentPost>> postComment(@PathVariable("blogId") String blogId,
            @RequestBody CommentForm newComment) throws ResourceNotFoundException {
        UserAccount currentUser = getCurrentAuthenticatedUser();
        BlogPost blogPost = this.blogPostRepository.findOne(blogId);
        if (blogPost == null || !blogPost.isPublished()) {
            throw new ResourceNotFoundException("No published blog post with the id: " + blogId);
        }
        CommentPost comment = new CommentPost(currentUser.getUserId(), blogPost.getId(), newComment.getContent());
        if (currentUser.isTrustedAccount()) {
            comment.setStatus(CommentStatusType.APPROVED);
        }
        comment = commentPostRepository.save(comment);
        
        //send email to author if someone else posted a comment to blog.
        if (this.messageSender != null && !blogPost.getAuthorId().equals(currentUser.getUserId())) {
            UserAccount author = userAccountRepository.findByUserId(blogPost.getAuthorId());
            this.messageSender.notifyAuthorForNewComment(author, currentUser, comment, blogPost);
        }

        Resource<CommentPost> resource = new Resource<CommentPost>(comment);
        return new ResponseEntity<Resource<CommentPost>>(resource, HttpStatus.CREATED);
    }

}
