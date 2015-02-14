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
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Component
public class UserProfileResourceAssembler {
    private final PagedResourcesAssembler<CommentPost> assembler;
    
    @Inject
    public UserProfileResourceAssembler(
            PagedResourcesAssembler<CommentPost> assembler) {
        this.assembler = assembler;
    }

    public UserProfileResource toResource(UserAccount userAccount) {
        UserProfileResource resource = new UserProfileResource(userAccount);
        try {
            resource.add(linkTo(methodOn(WebsiteRestController.class).getUserProfile(userAccount.getUserId()))
                    .withSelfRel());
            
            Link commentsLink = linkTo(methodOn(WebsiteRestController.class)
                    .getUserApprovedCommentPosts(userAccount.getUserId(), null, null))
                    .withRel(UserProfileResource.LINK_NAME_COMMENTS);
            resource.add(assembler.appendPaginationParameterTemplates(commentsLink));
        } catch (ResourceNotFoundException ex) {
            //do nothing
        }

        return resource;
    }
}
