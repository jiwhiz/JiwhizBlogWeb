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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.AbstractPost;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.AbstractRestController;
import com.jiwhiz.blog.web.dto.BlogPostDTO;
import com.jiwhiz.blog.web.dto.CommentPostDTO;

/**
 * RESTful Service for current logged in user comment management.
 * 
 * <p>API: '<b>rest/myAccount/comments/:action:commentId</b>'</p>
 * <p><b>:action</b> can be
 * <ul>
 * <li>'list' - return current user's comments.</li>
 * </ul>
 * </p>
 * 
 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping("/rest/myAccount/comments")
public class MyCommentRestService extends AbstractRestController {
	private static final Logger logger = LoggerFactory.getLogger(MyCommentRestService.class);
	
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    @ResponseBody
    public List<CommentPostDTO> getAllCommentsOfCurrentUser() {
        UserAccount currentUser = userAccountService.getCurrentUser();
        List<CommentPost> comments = commentPostRepository.findByAuthorKey(currentUser.getKey(), new Sort(Direction.DESC, "createdTime"));
        List<CommentPostDTO> result = new ArrayList<CommentPostDTO>();
        for (CommentPost comment : comments) {
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            BlogPostDTO blogDto = new BlogPostDTO(blog, false);
            CommentPostDTO commentDto = new CommentPostDTO(comment);
            commentDto.setBlog(blogDto);
            result.add(commentDto);
        }
        return result;
    }
    
    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommentPostDTO getComment(@PathVariable("commentId") String commentId) {
    	CommentPost comment = commentPostRepository.findByPostId(commentId);
    	checkIsAuthorOfComment(comment);
    	BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
        return new CommentPostDTO(comment, null, blog);
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public CommentPostDTO updateComment(@PathVariable("commentId") String commentId, @RequestBody CommentPostDTO commentForm) {
        logger.debug("==>MyCommentController.updateComment(), comment id=" + commentId);
        CommentPost comment = commentPostRepository.findByPostId(commentId);
        checkIsAuthorOfComment(comment);
        comment = comment.update(commentForm.getContent());
        comment = commentPostRepository.save(comment);
        BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
        return new CommentPostDTO(comment, null, blog);
    }

    protected void checkIsAuthorOfComment(AbstractPost post) throws AccessDeniedException {
        if (post == null || !post.getAuthorKey().equals(userAccountService.getCurrentUser().getKey())){
            throw new AccessDeniedException("Cannot access the post, becasue current user is not the author of the post.");
        }
    }


}
