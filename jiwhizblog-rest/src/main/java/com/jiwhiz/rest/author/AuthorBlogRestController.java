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
package com.jiwhiz.rest.author;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * @author Yuan Ji
 */
@Controller
public class AuthorBlogRestController extends AbstractAuthorRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorBlogRestController.class);
    
    private final AuthorBlogResourceAssembler authorBlogResourceAssembler;
    @Inject
    public AuthorBlogRestController(
            UserAccountService userAccountService, 
            BlogPostRepository blogPostRepository,
            AuthorBlogResourceAssembler authorBlogResourceAssembler) {
        super(userAccountService, blogPostRepository);
        this.authorBlogResourceAssembler = authorBlogResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_AUTHOR_BLOGS) 
    public HttpEntity<PagedResources<AuthorBlogResource>> getBlogPosts(
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0)Pageable pageable,
            PagedResourcesAssembler<BlogPost> assembler) {
        LOGGER.debug("==>AuthorBlogRestController.getBlogPosts()");
        UserAccount currentUser = getCurrentAuthenticatedAuthor();        
        Page<BlogPost> blogPosts = this.blogPostRepository.findByAuthorIdOrderByCreatedTimeDesc(currentUser.getUserId(), pageable);
        return new ResponseEntity<>(assembler.toResource(blogPosts, authorBlogResourceAssembler), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_AUTHOR_BLOGS_BLOG) 
    public HttpEntity<AuthorBlogResource> getBlogPostById(@PathVariable("blogId") String blogId) 
            throws ResourceNotFoundException {
        LOGGER.debug("==>AuthorBlogRestController.getBlogPostById()");
        
        BlogPost blogPost = getBlogByIdAndCheckAuthor(blogId);
        return new ResponseEntity<>(authorBlogResourceAssembler.toResource(blogPost), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = ApiUrls.URL_AUTHOR_BLOGS)
    public HttpEntity<Void> createBlogPost(@RequestBody BlogPostForm blogPostForm) throws ResourceNotFoundException {
        LOGGER.info("==>AuthorBlogRestController.createBlogPost()");
        
        UserAccount currentUser = getCurrentAuthenticatedAuthor();
        BlogPost blogPost = new BlogPost(currentUser.getUserId(), blogPostForm.getTitle(), 
                blogPostForm.getContent(), blogPostForm.getTagString());
        
        blogPost = blogPostRepository.save(blogPost);
        AuthorBlogResource resource = authorBlogResourceAssembler.toResource(blogPost);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(resource.getLink("self").getHref()));
                
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = ApiUrls.URL_AUTHOR_BLOGS_BLOG)
    public HttpEntity<Void> updateBlogPost(@PathVariable("blogId") String blogId, 
            @RequestBody BlogPostForm blogPostForm) throws ResourceNotFoundException {
        LOGGER.info("==>AuthorBlogRestController.updateBlogPost() for "+blogId);
        
        BlogPost blogPost = getBlogByIdAndCheckAuthor(blogId);
        blogPost.setContent(blogPostForm.getContent());
        blogPost.setTitle(blogPostForm.getTitle());
        blogPost.parseAndSetTags(blogPostForm.getTagString());
        blogPost = blogPostRepository.save(blogPost);
        
        AuthorBlogResource resource = authorBlogResourceAssembler.toResource(blogPost);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(resource.getLink("self").getHref()));
        
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(method = RequestMethod.PATCH, value = ApiUrls.URL_AUTHOR_BLOGS_BLOG)
    public HttpEntity<AuthorBlogResource> patchBlogPost(
            @PathVariable("blogId") String blogId, 
            @RequestBody Map<String, String> updateMap) throws ResourceNotFoundException {
        LOGGER.info("==>AuthorBlogRestController.patchBlogPost() for "+blogId);
        
        BlogPost blogPost = getBlogByIdAndCheckAuthor(blogId);
        String content = updateMap.get("content");
        if (content != null) {
            blogPost.setContent(content);
        }
        String title = updateMap.get("title");
        if (title != null) {
            blogPost.setTitle(title);
        }
        String tagString = updateMap.get("tagString");
        if (tagString != null) {
            blogPost.parseAndSetTags(tagString);
        }
        String published = updateMap.get("published");
        if (published != null) {
            blogPost.setPublished(published.equals("true"));
        }
        
        blogPost = blogPostRepository.save(blogPost);

        AuthorBlogResource resource = authorBlogResourceAssembler.toResource(blogPost);
        return new ResponseEntity<>(resource, HttpStatus.OK);

    }
}
