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
package com.jiwhiz.blog.web.admin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.AbstractRestController;
import com.jiwhiz.blog.web.dto.BlogPostDTO;
import com.jiwhiz.blog.web.dto.CommentPostDTO;
import com.jiwhiz.blog.web.dto.UserAccountDTO;

/**
 * RESTful Service for admin user managing comments.
 * 
 * <p>API: '<b>rest/admin/comments/:action:commentId/:commentAction</b>'</p>
 * <p><b>:action</b> can be
 * <ul>
 * <li>'list' - return current user's comments.</li>
 * </ul>
 * </p>
 * <p>:commentAction can be
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
@RequestMapping("/rest/admin/comments")
public class ManageCommentRestService extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ManageCommentRestService.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<CommentPostDTO> listComments() {
        List<CommentPostDTO> result = new ArrayList<CommentPostDTO>();
        List<CommentPost> comments = commentPostRepository.findAll(new Sort(Direction.DESC, "createdTime"));
        for (CommentPost comment : comments) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            UserAccountDTO userDto = new UserAccountDTO(user, true);
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            BlogPostDTO blogDto = new BlogPostDTO(blog, false);
            CommentPostDTO commentDto = new CommentPostDTO(comment);
            commentDto.setUser(userDto);
            commentDto.setBlog(blogDto);
            result.add(commentDto);
        }
        return result;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommentPostDTO getComment(@PathVariable("commentId") String commentId) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO updateComment(@PathVariable("commentId") String commentId,
            @RequestBody CommentPostDTO commentForm) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            comment.update(commentForm.getContent());
            comment = commentPostRepository.save(comment);
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}/approve", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO approveComment(@PathVariable("commentId") String commentId) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            comment.approve();
            comment = commentPostRepository.save(comment);
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}/disapprove", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO unpublishComment(@PathVariable("commentId") String commentId) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            comment.disapprove();
            comment = commentPostRepository.save(comment);
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}/markSpam", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO markSpamComment(@PathVariable("commentId") String commentId) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            comment.markSpam();
            comment = commentPostRepository.save(comment);
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}/unmarkSpam", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO unmarkSpamComment(@PathVariable("commentId") String commentId) {
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        if (comment != null) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            comment.unmarkSpam();
            comment = commentPostRepository.save(comment);
            return new CommentPostDTO(comment, user, blog);
        } else {
            return null; // throw exception??
        }
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("commentId") String commentId) {
        logger.debug("User delete comment " + commentId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        commentPostRepository.delete(comment);
    }
}
