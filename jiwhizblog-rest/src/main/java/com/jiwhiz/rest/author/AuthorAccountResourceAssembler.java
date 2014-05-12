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
public class AuthorAccountResourceAssembler implements ResourceAssembler<UserAccount, AuthorAccountResource> {
    private final PagedResourcesAssembler<BlogPost> assembler;
    
    @Inject
    public AuthorAccountResourceAssembler(PagedResourcesAssembler<BlogPost> assembler) {
        this.assembler = assembler;
    }

    @Override
    public AuthorAccountResource toResource(UserAccount entity) {
        AuthorAccountResource resource = new AuthorAccountResource(entity);

        try {
            resource.add(linkTo(methodOn(AuthorAccountRestController.class).getCurrentAuthorAccount())
                    .withSelfRel());
            
            String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
            Link blogsLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS), AuthorAccountResource.LINK_NAME_BLOGS);
            resource.add(assembler.appendPaginationParameterTemplates(blogsLink));
            
            Link blogLink = new Link(
                    new UriTemplate(baseUri + ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG), AuthorAccountResource.LINK_NAME_BLOG);
            resource.add(blogLink);

        } catch (Exception ex) {
            //do nothing
        }
        
        return resource;

    }

}
