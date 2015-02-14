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
package com.jiwhiz.rest.author;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.rest.site.WebsiteRestController;

/**
 * @author Yuan Ji
 */
@Component
public class AuthorBlogCommentResourceAssembler implements ResourceAssembler<CommentPost, AuthorBlogCommentResource> {

    @Override
    public AuthorBlogCommentResource toResource(CommentPost comment) {
        AuthorBlogCommentResource resource = new AuthorBlogCommentResource(comment);

        try {
            resource.add(linkTo(methodOn(AuthorBlogCommentRestController.class).getCommentPostById(comment.getBlogPostId(), comment.getId()))
                    .withSelfRel());
            resource.add(linkTo(methodOn(AuthorBlogRestController.class).getBlogPostById(comment.getBlogPostId()))
                    .withRel(AuthorBlogCommentResource.LINK_NAME_BLOG));
            resource.add(linkTo(methodOn(WebsiteRestController.class).getUserProfile(comment.getAuthorId()))
                    .withRel(AuthorBlogCommentResource.LINK_NAME_AUTHOR));
        } catch (Exception ex) {
            //do nothing
        }
        
        return resource;
    }

}
