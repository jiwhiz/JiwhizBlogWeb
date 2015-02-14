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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.mail.ContactForm;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.MessageSender;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * @author Yuan Ji
 */
@Controller
@RequestMapping( value = ApiUrls.API_ROOT, produces = "application/hal+json" )
public class WebsiteRestController {
    public static final int MOST_RECENT_NUMBER = 4;
    
    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;
    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final MessageSender messageSender;
    private final WebsiteResourceAssembler websiteResourceAssembler;
    private final PublicBlogResourceAssembler publicBlogResourceAssembler;
    private final PublicCommentResourceAssembler publicCommentResourceAssembler;
    private final UserProfileResourceAssembler userProfileResourceAssembler;

    @Inject
    public WebsiteRestController(
            UserAccountService userAccountService,
            UserAccountRepository userAccountRepository, 
            BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, 
            MessageSender messageSender,
            WebsiteResourceAssembler websiteResourceAssembler,
            PublicBlogResourceAssembler publicBlogResourceAssembler,
            PublicCommentResourceAssembler publicCommentResourceAssembler,
            UserProfileResourceAssembler userProfileResourceAssembler) {
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.messageSender = messageSender;
        this.websiteResourceAssembler = websiteResourceAssembler;
        this.publicBlogResourceAssembler = publicBlogResourceAssembler;
        this.publicCommentResourceAssembler = publicCommentResourceAssembler;
        this.userProfileResourceAssembler = userProfileResourceAssembler;
    }

    /**
     * Get public website resource root, with links to other resources
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE)
    public HttpEntity<WebsiteResource> getPublicWebsiteResource() {
        WebsiteResource resource = websiteResourceAssembler.toResource();        
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Get current user info. If not authenticated, return 401.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_CURRENT_USER)
    public HttpEntity<Resource<UserAccount>> getCurrentUserAccount() {
        UserAccount currentUser = this.userAccountService.getCurrentUser();
        Resource<UserAccount> resource = (currentUser == null)? null : new Resource<UserAccount>(currentUser);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Returns signed up user public profile info.
     * 
     * @param userId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_PROFILES_USER)
    public HttpEntity<UserProfileResource> getUserProfile(@PathVariable("userId") String userId) {
        UserAccount userAccount = this.userAccountRepository.findByUserId(userId);
        UserProfileResource resource = userProfileResourceAssembler.toResource(userAccount);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_PROFILES_USER_COMMENTS)
    public HttpEntity<PagedResources<PublicCommentResource>> getUserApprovedCommentPosts(
            @PathVariable("userId") String userId,
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<CommentPost> assembler) throws ResourceNotFoundException {
        Page<CommentPost> commentPosts = this.commentPostRepository.findByAuthorIdAndStatusOrderByCreatedTimeDesc(
                userId, CommentStatusType.APPROVED, pageable);
        return new ResponseEntity<>(assembler.toResource(commentPosts, publicCommentResourceAssembler), HttpStatus.OK);
    }

    
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_LATEST_BLOG)
    public HttpEntity<PublicBlogResource> getLatestBlogPost() throws ResourceNotFoundException {
        PageRequest request = new PageRequest(0, 1);
        Page<BlogPost> blogPosts = this.blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc(request);
        if (blogPosts.getContent().size() != 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        BlogPost blogPost = blogPosts.getContent().get(0);
        PublicBlogResource resource = publicBlogResourceAssembler.toResource(blogPost);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_RECENT_BLOGS)
    public HttpEntity<Resources<PublicBlogResource>> getRecentPublicBlogPosts() {
        PageRequest request = new PageRequest(0, MOST_RECENT_NUMBER);
        Collection<PublicBlogResource> blogPostResourceCollection = new ArrayList<PublicBlogResource>();
        Page<BlogPost> blogPosts = this.blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc(request);
        for (BlogPost blogPost : blogPosts) {
            PublicBlogResource resource = publicBlogResourceAssembler.toResource(blogPost);
            blogPostResourceCollection.add(resource);
        }
        
        Resources<PublicBlogResource> resources = new Resources<>(blogPostResourceCollection);
        resources.add(linkTo(methodOn(WebsiteRestController.class).getRecentPublicBlogPosts())
                .withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_RECENT_COMMENTS)
    public HttpEntity<Resources<Resource<CommentPost>>> getRecentPublicCommentPosts() {
        PageRequest request = new PageRequest(0, MOST_RECENT_NUMBER);
        Collection<Resource<CommentPost>> resourceCollection = new ArrayList<Resource<CommentPost>>();
        Page<CommentPost> commentPosts = this.commentPostRepository.findByStatusOrderByCreatedTimeDesc(CommentStatusType.APPROVED, request);
        for (CommentPost comment : commentPosts) {
            Resource<CommentPost> resource = publicCommentResourceAssembler.toResource(comment);
            resourceCollection.add(resource);
        }
        
        Resources<Resource<CommentPost>> resources = new Resources<Resource<CommentPost>>(resourceCollection);
        resources.add(linkTo(methodOn(WebsiteRestController.class).getRecentPublicCommentPosts())
                .withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_SITE_TAG_CLOUDS)
    public HttpEntity<TagCloud[]> getTagCloud() {
        Map<String, Integer> tagMap = new HashMap<String, Integer>();
        List<BlogPost> blogList = blogPostRepository.findAllPublishedPostsIncludeTags();
        for (BlogPost blog : blogList) {
            for (String tag : blog.getTags()) {
                tag = tag.trim();
                if (tagMap.containsKey(tag)){
                    tagMap.put(tag,  tagMap.get(tag) + 1);
                } else {
                    tagMap.put(tag,  1);
                }
            }
        }
        
        TagCloud[] tagClouds = new TagCloud[tagMap.size()];
        int index = 0;
        for (String key : tagMap.keySet()) {
            tagClouds[index++] = new TagCloud(key, tagMap.get(key));
        }
        
        Arrays.sort(tagClouds, TagCloud.TagCloudCountComparator);
        
        //get at most first 30 tags
        final int MAX_NUM = 30;
        int returnTagNumber = (tagMap.size() > MAX_NUM) ? MAX_NUM : tagMap.size();
        TagCloud[] returnTagClouds = new TagCloud[returnTagNumber];
        for (int i=0; i<returnTagNumber; i++) {
            returnTagClouds[i] = tagClouds[i];
        }
        Arrays.sort(returnTagClouds, TagCloud.TagCloudNameComparator);
        
        return new ResponseEntity<TagCloud[]>(returnTagClouds, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = ApiUrls.URL_SITE_CONTACT)
    @ResponseBody
    public String submitContactMessage(@RequestBody ContactForm contactForm) {
        messageSender.notifyAdminForContactMessage(contactForm);
        return "";
    }

}
