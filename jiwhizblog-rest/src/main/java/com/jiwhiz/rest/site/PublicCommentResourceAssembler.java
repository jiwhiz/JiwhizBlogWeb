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

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Component
public class PublicCommentResourceAssembler implements ResourceAssembler<CommentPost, PublicCommentResource> {

    @Override
    public PublicCommentResource toResource(CommentPost entity) {
        PublicCommentResource resource = new PublicCommentResource(entity);

        try {
            resource.add(linkTo(methodOn(PublicBlogRestController.class).getBlogApprovedCommentPostById(entity.getBlogPostId(), entity.getId()))
                    .withSelfRel());
            resource.add(linkTo(methodOn(PublicBlogRestController.class).getPublicBlogPostById(entity.getBlogPostId()))
                    .withRel(PublicCommentResource.LINK_NAME_BLOG));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getUserProfile(entity.getAuthorId()))
                    .withRel(PublicCommentResource.LINK_NAME_AUTHOR));
        } catch (ResourceNotFoundException ex) {
            //do nothing
        }
        return resource;

    }

}
