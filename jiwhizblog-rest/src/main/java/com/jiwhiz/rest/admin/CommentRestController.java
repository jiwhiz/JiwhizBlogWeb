/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
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
package com.jiwhiz.rest.admin;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * @author Yuan Ji
 */
@Controller
public class CommentRestController extends AbstractAdminRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentRestController.class);

    private final CommentPostRepository commentPostRepository;
    private final CommentResourceAssembler commentResourceAssembler;

    @Inject
    public CommentRestController(
            UserAccountService userAccountService, 
            CommentPostRepository commentPostRepository,
            CommentResourceAssembler commentResourceAssembler) {
        super(userAccountService);
        this.commentPostRepository = commentPostRepository;
        this.commentResourceAssembler = commentResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_COMMENTS) 
    public HttpEntity<PagedResources<CommentResource>> getCommentPosts(
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, 
                             page = 0, sort="createdTime", direction=Direction.DESC) Pageable pageable,
                             PagedResourcesAssembler<CommentPost> assembler) {
        Page<CommentPost> commentPosts = this.commentPostRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(commentPosts, commentResourceAssembler), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_COMMENTS_COMMENT) 
    public HttpEntity<CommentResource> getCommentPostById(
            @PathVariable("commentId") String commentId) 
            throws ResourceNotFoundException {
        LOGGER.debug("==>AdminCommentRestController.getCommentPostById()");
        CommentPost commentPost = getCommentById(commentId);
        return new ResponseEntity<>(commentResourceAssembler.toResource(commentPost), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PATCH, value = ApiUrls.URL_ADMIN_COMMENTS_COMMENT)
    public HttpEntity<Void> updateCommentPost(
            @PathVariable("commentId") String commentId,
            @RequestBody Map<String, String> updateMap) throws ResourceNotFoundException {
        LOGGER.info("==>AdminCommentRestController.updateCommentPost() with " + updateMap);
        CommentPost commentPost = getCommentById(commentId);
        
        String content = updateMap.get("content");
        if (content != null) {
            commentPost.update(content);
        }
        String statusString = updateMap.get("status");
        if (statusString != null) {
            CommentStatusType status = CommentStatusType.valueOf(statusString);
            if (status != null) {
                commentPost.setStatus(status);
            } else {
                //TODO throw exception for invalid status
            }
        }
        commentPostRepository.save(commentPost);
        
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private CommentPost getCommentById(String commentId) throws ResourceNotFoundException {
        CommentPost commentPost = commentPostRepository.findOne(commentId);
        if (commentPost == null) {
            throw new ResourceNotFoundException("No such comment for id "+commentId);
        }
        return commentPost;
    }
}
