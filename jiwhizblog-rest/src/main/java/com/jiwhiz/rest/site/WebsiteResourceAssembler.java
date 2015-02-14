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

import javax.inject.Inject;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Component
public class WebsiteResourceAssembler {
    private final UserAccountService userAccountService;
    private final PagedResourcesAssembler<BlogPost> assembler;
    
    @Inject
    public WebsiteResourceAssembler(UserAccountService userAccountService, PagedResourcesAssembler<BlogPost> assembler) {
        this.userAccountService = userAccountService;
        this.assembler = assembler;
        
    }
    
    public WebsiteResource toResource() {
        UserAccount currentUser = this.userAccountService.getCurrentUser();
        WebsiteResource resource = new WebsiteResource();
        resource.setAuthenticated(currentUser != null);
        
        try{
            resource.add(linkTo(methodOn(WebsiteRestController.class).getPublicWebsiteResource())
                    .withSelfRel());
            
            String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
            
            Link blogsLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS), WebsiteResource.LINK_NAME_BLOGS);
            resource.add(assembler.appendPaginationParameterTemplates(blogsLink));
            
            Link blogLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG), WebsiteResource.LINK_NAME_BLOG);
            resource.add(blogLink);
            
            resource.add(linkTo(methodOn(WebsiteRestController.class).getCurrentUserAccount())
                    .withRel(WebsiteResource.LINK_NAME_CURRENT_USER));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getLatestBlogPost())
                    .withRel(WebsiteResource.LINK_NAME_LATEST_BLOG));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getRecentPublicBlogPosts())
                    .withRel(WebsiteResource.LINK_NAME_RECENT_BLOGS));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getRecentPublicCommentPosts())
                    .withRel(WebsiteResource.LINK_NAME_RECENT_COMMENTS));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getTagCloud())
                    .withRel(WebsiteResource.LINK_NAME_TAG_CLOUD));

            Link profileLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_SITE_PROFILES_USER), WebsiteResource.LINK_NAME_PROFILE);
            resource.add(profileLink);

            Link postCommentLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_USER_BLOGS_BLOG_COMMENTS), WebsiteResource.LINK_NAME_POST_COMMENT);
            resource.add(postCommentLink);
        
        } catch (ResourceNotFoundException ex) {
        }

        return resource;
    }
}
