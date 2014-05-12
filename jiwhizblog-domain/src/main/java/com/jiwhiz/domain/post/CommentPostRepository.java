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
package com.jiwhiz.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB Repository for CommentPost entity.
 * 
 * @author Yuan Ji
 *
 */
public interface CommentPostRepository extends MongoRepository<CommentPost, String>{
    
    Page<CommentPost> findByBlogPostId(String blogPostId, Pageable pageable);
    
    Page<CommentPost> findByStatusOrderByCreatedTimeDesc(CommentStatusType status, Pageable pageable);
    
    int countByBlogPostIdAndStatus(String blogPostId, CommentStatusType status);
    
    int countByBlogPostId(String blogPostId);
    
    Page<CommentPost> findByBlogPostIdOrderByCreatedTimeDesc(String blogPostId, Pageable pageable);

    Page<CommentPost> findByAuthorIdAndStatusOrderByCreatedTimeDesc(String authorId, CommentStatusType status, Pageable pageable);
    
    Page<CommentPost> findByAuthorIdOrderByCreatedTimeDesc(String authorId, Pageable pageable);
    
    Page<CommentPost> findByBlogPostIdAndStatusOrderByCreatedTimeAsc(String blogPostId, CommentStatusType status, Pageable pageable);
}
