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

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Component
public class CommentResourceAssembler implements ResourceAssembler<CommentPost, CommentResource> {

    @Override
    public CommentResource toResource(CommentPost commentPost) {
        CommentResource resource = new CommentResource(commentPost);
        
        try {
            resource.add(linkTo(methodOn(CommentRestController.class).getCommentPostById(commentPost.getId()))
                    .withSelfRel());
            resource.add(linkTo(methodOn(UserRestController.class).getUserAccountByUserId(commentPost.getAuthorId()))
                    .withRel(CommentResource.LINK_NAME_AUTHOR));
            resource.add(linkTo(methodOn(BlogRestController.class).getBlogPostById(commentPost.getBlogPostId()))
                    .withRel(CommentResource.LINK_NAME_BLOG));
        } catch (ResourceNotFoundException ex) {
            //do nothing
        }
        return resource;
    }

}
