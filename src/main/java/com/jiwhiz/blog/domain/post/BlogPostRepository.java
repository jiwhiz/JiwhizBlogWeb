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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Yuan Ji
 *
 */
public interface BlogPostRepository extends MongoRepository<BlogPost, String>{
    
    List<BlogPost> findByPublishedIsTrue(Sort sort);
    
    Page<BlogPost> findByPublishedIsTrueOrderByPublishedTimeDesc(Pageable pageable);
    
    Page<BlogPost> findByPublishedIsTrueAndTagsOrderByPublishedTimeDesc(String tag, Pageable pageable);
    
    Page<BlogPost> findByAuthorId(String authorId, Pageable pageable);
    
    BlogPost findByPublishedYearAndPublishedMonthAndPublishedPath(int year, int month, String path);
    
    List<BlogPost> findByTitleRegexIgnoreCase(String title);
    
    @Query(value="{ 'published' : true }", fields="{ 'content' : 0}")
    List<BlogPost> findAllPublishedPostsWithoutContent();

    @Query(value="{ 'published' : true }", fields="{ 'tags' : 1}")
    List<BlogPost> findAllPublishedPostsIncludeTags();
}
