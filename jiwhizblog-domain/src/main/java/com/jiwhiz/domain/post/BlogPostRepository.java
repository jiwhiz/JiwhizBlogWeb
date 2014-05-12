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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * MongoDB Repository for BlogPost entity.
 * 
 * @author Yuan Ji
 *
 */
public interface BlogPostRepository extends MongoRepository<BlogPost, String>{
    
    Page<BlogPost> findByPublishedIsTrueOrderByPublishedTimeDesc(Pageable pageable);

    Page<BlogPost> findByAuthorIdOrderByCreatedTimeDesc(String authorId, Pageable pageable);
    
    @Query(value="{ 'published' : true }", fields="{ 'tags' : 1}")
    List<BlogPost> findAllPublishedPostsIncludeTags();
    
    Long countByPublishedIsTrue();
}
