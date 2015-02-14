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
package com.jiwhiz.rest.admin;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.rest.site.WebsiteRestController;

/**
 * @author Yuan Ji
 */
@Component
public class UserResourceAssembler implements ResourceAssembler<UserAccount, UserResource> {

    @Override
    public UserResource toResource(UserAccount userAccount) {
        UserResource resource = new UserResource(userAccount);

        try {
            resource.add(linkTo(methodOn(UserRestController.class).getUserAccountByUserId(userAccount.getUserId()))
                    .withSelfRel());
            resource.add(linkTo(methodOn(WebsiteRestController.class).getUserProfile(userAccount.getUserId()))
                    .withRel(UserResource.LINK_NAME_PROFILE));
            resource.add(linkTo(methodOn(UserRestController.class).getUserSocialConnections(userAccount.getUserId()))
                    .withRel(UserResource.LINK_NAME_SOCIAL_CONNECTIONS));
        } catch (Exception ex) {
            //do nothing
        }
        
        return resource;

    }

}
