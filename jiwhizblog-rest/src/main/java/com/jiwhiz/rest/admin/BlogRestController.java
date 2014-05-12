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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * @author Yuan Ji
 */
@Controller
public class BlogRestController extends AbstractAdminRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogRestController.class);

    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final BlogResourceAssembler blogResourceAssembler;
    private final CommentResourceAssembler commentResourceAssembler;

    @Inject
    public BlogRestController(
            UserAccountService userAccountService, 
            BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository,
            BlogResourceAssembler blogResourceAssembler,
            CommentResourceAssembler commentResourceAssembler) {
        super(userAccountService);
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.blogResourceAssembler = blogResourceAssembler;
        this.commentResourceAssembler = commentResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_BLOGS) 
    public HttpEntity<PagedResources<BlogResource>> getBlogPosts(
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<BlogPost> assembler) {
        Page<BlogPost> blogPosts = this.blogPostRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(blogPosts, blogResourceAssembler), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_BLOGS_BLOG) 
    public HttpEntity<BlogResource> getBlogPostById(
            @PathVariable("blogId") String blogId) 
            throws ResourceNotFoundException {
        LOGGER.debug("==>AdminBlogRestController.getCommentPostById()");
        BlogPost blogPost = getBlogById(blogId);
        return new ResponseEntity<>(blogResourceAssembler.toResource(blogPost), HttpStatus.OK);
    }

    private BlogPost getBlogById(String blogId) throws ResourceNotFoundException {
        BlogPost blogPost = blogPostRepository.findOne(blogId);
        if (blogPost == null) {
            throw new ResourceNotFoundException("No such blog for id "+blogId);
        }
        return blogPost;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_BLOGS_BLOG_COMMENTS) 
    public HttpEntity<PagedResources<CommentResource>> getCommentPostsByBlogPostId(
            @PathVariable("blogId") String blogId,
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<CommentPost> assembler) 
            throws ResourceNotFoundException {
        LOGGER.debug("==>AuthorBlogCommentRestController.getCommentPostsByBlogPostId()");
        Page<CommentPost> commentPosts = commentPostRepository.findByBlogPostIdOrderByCreatedTimeDesc(blogId, pageable);
        return new ResponseEntity<>(assembler.toResource(commentPosts, commentResourceAssembler), HttpStatus.OK);
    }

}
