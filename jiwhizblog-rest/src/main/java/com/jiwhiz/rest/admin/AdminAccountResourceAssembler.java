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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
@Component
public class AdminAccountResourceAssembler implements ResourceAssembler<UserAccount, AdminAccountResource> {
    private final PagedResourcesAssembler<BlogPost> assembler;
    
    @Inject
    public AdminAccountResourceAssembler(PagedResourcesAssembler<BlogPost> assembler) {
        this.assembler = assembler;
    }

    @Override
    public AdminAccountResource toResource(UserAccount userAccount) {
        AdminAccountResource resource = new AdminAccountResource(userAccount);

        try {
            resource.add(linkTo(methodOn(AdminAccountRestController.class).getAdminAccount()).withSelfRel());
            
            String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
            
            Link usersLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_USERS), AdminAccountResource.LINK_NAME_USERS);
            resource.add(assembler.appendPaginationParameterTemplates(usersLink));
            
            Link blogsLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_BLOGS), AdminAccountResource.LINK_NAME_BLOGS);
            resource.add(assembler.appendPaginationParameterTemplates(blogsLink));
            
            Link commentsLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_COMMENTS), AdminAccountResource.LINK_NAME_COMMENTS);
            resource.add(assembler.appendPaginationParameterTemplates(commentsLink));

        } catch (Exception ex) {
            //do nothing
        }
        
        return resource;

    }

}
