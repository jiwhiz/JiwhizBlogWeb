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

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Yuan Ji (jiwhiz@gmail.com)
 *
 */
public interface CommentPostRepository extends MongoRepository<CommentPost, String>{
    
    List<CommentPost> findAll(Sort sort);
    
    List<CommentPost> findByBlogPostKey(String blogPostKey, Sort sort);
    
    List<CommentPost> findByBlogPostKeyAndStatus(String blogPostKey, CommentStatusType status, Sort sort);
    
    List<CommentPost> findByAuthorKey(String authorKey, Sort sort);
    
    List<CommentPost> findByAuthorKeyAndStatus(String authorKey, CommentStatusType status, Sort sort);
    
    List<CommentPost> findByStatus(CommentStatusType status, Sort sort);
    
    CommentPost findByPostId(String postId); 
    
    int countByBlogPostKeyAndStatus(String blogPostKey, CommentStatusType status);
    
    int countByBlogPostKey(String blogPostKey);
}
