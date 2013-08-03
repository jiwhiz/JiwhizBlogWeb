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
package com.jiwhiz.blog.web.post;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.web.dto.CommentPostDTO;

/**
 * RESTful Service for current logged in author managing blog comments.
 * <p>
 * API: '<b>rest/myPost/blogs/:blogId/comments/:action:commentId/:commentAction</b>'
 * </p>
 * <p>
 * <b>:action</b> can be
 * <ul>
 * <li>'list' - return blog post's comments.</li>
 * </ul>
 * </p>
 * <p>
 * <b>:commentAction</b> can be
 * <ul>
 * <li>'approve' - approve comment (PENDING -> APPROVED).</li>
 * <li>'disapprove' - disapprove comment (APPROVED -> PENDING).</li>
 * <li>'markSpam' - mark comment as spam (PENDING -> SPAM).</li>
 * <li>'unmarkSpam' - remove comment spam mark (SPAM -> PENDING).</li>
 * </ul>
 * </p>
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/rest/myPost/blogs")
public class MyBlogCommentRestService {
    private static final Logger logger = LoggerFactory.getLogger(MyBlogCommentRestService.class);

    private final UserAccountRepository userAccountRepository;
    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;

    @Inject
    public MyBlogCommentRestService(UserAccountRepository userAccountRepository, BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository) {
        this.userAccountRepository = userAccountRepository;
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
    }

    @RequestMapping(value = "/{blogId}/comments/list", method = RequestMethod.GET)
    @ResponseBody
    public List<CommentPostDTO> getAllCommentsOfBlog(@PathVariable("blogId") String blogId) {
        List<CommentPostDTO> result = new ArrayList<CommentPostDTO>();
        BlogPost blogPost = blogPostRepository.findByPostId(blogId);
        List<CommentPost> comments = commentPostRepository.findByBlogPostKey(blogPost.getKey(), new Sort(
                Direction.DESC, "createdTime"));
        for (CommentPost comment : comments) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            result.add(new CommentPostDTO(comment, user, null));
        }
        return result;
    }

    @RequestMapping(value = "/{blogId}/comments/{commentId}/approve", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO approveComment(@PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) {
        BlogPost blog = blogPostRepository.findByPostId(blogId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        checkCommentIsForBlogPost(comment, blog);
        UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
        comment.approve();
        comment = commentPostRepository.save(comment);
        return new CommentPostDTO(comment, user, null);
    }

    @RequestMapping(value = "/{blogId}/comments/{commentId}/disapprove", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO disapproveComment(@PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) {
        BlogPost blog = blogPostRepository.findByPostId(blogId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        checkCommentIsForBlogPost(comment, blog);
        UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
        comment.disapprove();
        comment = commentPostRepository.save(comment);
        return new CommentPostDTO(comment, user, null);
    }

    @RequestMapping(value = "/{blogId}/comments/{commentId}/markSpam", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO markSpamComment(@PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) {
        BlogPost blog = blogPostRepository.findByPostId(blogId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        checkCommentIsForBlogPost(comment, blog);
        UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
        comment.markSpam();
        comment = commentPostRepository.save(comment);
        return new CommentPostDTO(comment, user, null);
    }

    @RequestMapping(value = "/{blogId}/comments/{commentId}/unmarkSpam", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO unmarkSpamComment(@PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) {
        BlogPost blog = blogPostRepository.findByPostId(blogId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        checkCommentIsForBlogPost(comment, blog);
        UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
        comment.unmarkSpam();
        comment = commentPostRepository.save(comment);
        return new CommentPostDTO(comment, user, null);
    }

    private void checkCommentIsForBlogPost(CommentPost commentPost, BlogPost blogPost) {
        if (!commentPost.getBlogPostKey().equals(blogPost.getKey())) {
            logger.warn("Illegal access of comment '%s', not belong to blog '%s'.", commentPost.getPostId(),
                    blogPost.getPostId());
            throw new IllegalArgumentException("Comment is not for the blog.");
        }
    }

}
