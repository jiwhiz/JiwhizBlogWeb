/* 
 * Copyright 2013 JIWHIZ Consulting Inc.
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
package com.jiwhiz.blog.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Application Service Interface for CommentPost.
 * 
 * @author Yuan Ji
 *
 */
public interface CommentPostService {
    
    /**
     * Gets CommentPost object by id. The CommentPost object will be pre-loaded with authorAccount
     * and blogPost 
     * 
     * @param id
     * @return
     */
    CommentPost getCommentById(String id);

    /**
     * Gets first (count) number of published CommentPost. 
     * 
     * @param count
     * @return
     */
    List<CommentPost> getPublishedComments(int count);

    /**
     * Gets all published comments for the specified BlogPost. Ordered by createdTime.
     * 
     * @param postId
     * @return
     */
    List<CommentPost> getPublishedCommentsForPost(String postId);
    
    /**
     * Gets all comments for the specified BlogPost.
     * SECURITY: Current logged in user must have ROLE_AUTHOR and must be
     * the author of the blog post.
     * 
     * @param blogPostId
     * @return
     */
    List<CommentPost> getAllCommentsForPost(String blogPostId);

    /**
     * Gets all published comments for the specified UserAccount. Ordered by createdTime.
     * 
     * @param userId
     * @return
     */
    List<CommentPost> getPublishedCommentsForUser(String userId);
    
    /**
     * Gets all comments for the specified UserAccount.
     * SECURITY: Current logged in user must have ROLE_ADMIN or current logged in user has ROLE_USER with same userId.
     * 
     * @param postId
     * @param pageable
     * @return
     */
    Page<CommentPost> getAllCommentsForUser(String userId, Pageable pageable);

    /**
     * Gets all comments in the system.
     * SECURITY: Current logged in user must have ROLE_ADMIN.
     * 
     * @param pageable
     * @return
     */
    Page<CommentPost> getAllComments(Pageable pageable);

    /**
     * Adds a comment to specific BlogPost from current logged in user.
     * SECURITY: Current logged in user must have ROLE_USER.
     * 
     * @param blogPostId
     * @param content
     * @return
     */
    CommentPost addComment(String blogPostId, String content);

    /**
     * Updates a comment.
     * SECURITY: Current logged in user must have ROLE_ADMIN;
     * Or logged in user is the original author of the comment.
     * 
     * @param commentPostId
     * @param content
     * @return
     */
    CommentPost updateComment(String commentPostId, String content);

    /**
     * Sets the comment published flag to true or false; if the comment is published,
     * it will be displayed at the public blog post page.
     * 
     * SECURITY: Current logged in user must have ROLE_ADMIN; Or Current logged in user has ROLE_AUTHOR and
     * the comment is for the post authored by the user.
     * 
     * @param id
     * @param published
     * @return
     */
    CommentPost setCommentPublished(String id, boolean published);
    
    /**
     * Sets the comment spam flag to true or false.
     * 
     * SECURITY: Current logged in user must have ROLE_ADMIN; Or Current logged in user has ROLE_AUTHOR and
     * the comment is for the post authored by the user.
     * 
     * @param id
     * @param spam
     * @return
     */
    CommentPost setCommentSpam(String id, boolean spam);
    
    /**
     * Deletes the comment.
     * SECURITY: Current logged in user must have ROLE_ADMIN.
     * 
     * @param id
     */
    void deleteComment(String id);


}
