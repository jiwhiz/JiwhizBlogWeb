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
package com.jiwhiz.rest.site;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * REST API for public BlogPost resources.
 * 
 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping( value = ApiUrls.API_ROOT, produces = "application/hal+json" )
public class PublicBlogRestController {
    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final PublicBlogResourceAssembler publicBlogResourceAssembler;
    private final PublicCommentResourceAssembler publicCommentResourceAssembler;

    @Inject
    public PublicBlogRestController(
            BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository,
            PublicBlogResourceAssembler publicBlogResourceAssembler,
            PublicCommentResourceAssembler publicCommentResourceAssembler) {
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.publicBlogResourceAssembler = publicBlogResourceAssembler;
        this.publicCommentResourceAssembler = publicCommentResourceAssembler;
    }

    /**
     * Returns public blog posts with pagination.
     * 
     * @param pageable
     * @param assembler
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_BLOGS)
    public HttpEntity<PagedResources<PublicBlogResource>> getPublicBlogPosts(
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0)Pageable pageable,
            PagedResourcesAssembler<BlogPost> assembler) {
        
        Page<BlogPost> blogPosts = this.blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc(pageable);
        return new ResponseEntity<>(assembler.toResource(blogPosts, publicBlogResourceAssembler), HttpStatus.OK);
    }

    /**
     * Returns public blog by id.
     * 
     * @param id
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_BLOGS_BLOG)
    public HttpEntity<PublicBlogResource> getPublicBlogPostById(
    		@PathVariable("blogId") String id) throws ResourceNotFoundException {
        
        BlogPost blogPost = getPublishedBlogById(id);
        PublicBlogResource resource = publicBlogResourceAssembler.toResource(blogPost);
        
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    /**
     * Returns blog post approved comments with pagination.
     * @param id
     * @param pageable
     * @param assembler
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS)
    public HttpEntity<PagedResources<PublicCommentResource>> getBlogApprovedCommentPosts(
            @PathVariable("blogId") String id,
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<CommentPost> assembler) throws ResourceNotFoundException {
        getPublishedBlogById(id); //security check
        
        Page<CommentPost> commentPosts = 
                this.commentPostRepository.findByBlogPostIdAndStatusOrderByCreatedTimeAsc(id, CommentStatusType.APPROVED, pageable);
        return new ResponseEntity<>(assembler.toResource(commentPosts, publicCommentResourceAssembler), HttpStatus.OK);
    }

    /**
     * Returns blog post approved comment by blog id and comment id.
     * 
     * @param blogId
     * @param commentId
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT)
    public HttpEntity<Resource<CommentPost>> getBlogApprovedCommentPostById(
            @PathVariable("blogId") String blogId,
            @PathVariable("commentId") String commentId) throws ResourceNotFoundException {
        
        BlogPost blogPost = getPublishedBlogById(blogId);
        CommentPost commentPost = commentPostRepository.findOne(commentId);
        if (commentPost == null) {
            throw new ResourceNotFoundException("No such comment for id "+commentId);
        }

        if (!commentPost.getBlogPostId().equals(blogPost.getId())) {
            throw new ResourceNotFoundException(
                    String.format("Comment(%s) is not for Blog(%s).", commentId, blogPost.getId()));
        }

        if (commentPost.getStatus() != CommentStatusType.APPROVED) {
            throw new ResourceNotFoundException(
                    String.format("Comment(%s) is not approved.", commentId));
        }

        Resource<CommentPost> resource = publicCommentResourceAssembler.toResource(commentPost);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    private BlogPost getPublishedBlogById(String blogId) throws ResourceNotFoundException {
        BlogPost blogPost = this.blogPostRepository.findOne(blogId);
        if (blogPost == null || !blogPost.isPublished()) {
            throw new ResourceNotFoundException("No published blog post with the id: "+blogId);
        }
        return blogPost;
    }

}
