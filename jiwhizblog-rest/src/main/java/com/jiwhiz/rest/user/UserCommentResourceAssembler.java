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
package com.jiwhiz.rest.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.site.PublicBlogRestController;

/**
 * @author Yuan Ji
 */
@Component
public class UserCommentResourceAssembler implements ResourceAssembler<CommentPost, UserCommentResource>{

    @Override
    public UserCommentResource toResource(CommentPost commentPost) {
        UserCommentResource resource = new UserCommentResource(commentPost);
        
        try {
            resource.add(linkTo(methodOn(UserCommentRestController.class).getCommentPostById(commentPost.getId()))
                    .withSelfRel());
            resource.add(linkTo(methodOn(UserAccountRestController.class).getCurrentUserAccount())
                    .withRel(UserCommentResource.LINK_NAME_USER));
            resource.add(linkTo(methodOn(PublicBlogRestController.class).getPublicBlogPostById(commentPost.getBlogPostId()))
                    .withRel(UserCommentResource.LINK_NAME_BLOG));
        } catch (ResourceNotFoundException ex) {
            //do nothing
        }
        return resource;
    }

}
