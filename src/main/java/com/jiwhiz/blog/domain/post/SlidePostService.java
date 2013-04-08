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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Application Service Interface for SlidePost.
 * 
 * @author Yuan Ji
 *
 */
public interface SlidePostService {

    /**
     * Gets SlidePost object by public access path.
     * 
     * @param year
     * @param month
     * @param path
     * @return
     */
    SlidePost getSlideByPublishedPath(String path);

    /**
     * Gets all slides for current user.
     * 
     * @param pageable
     * @return
     */
    Page<SlidePost> getSlidePostsForCurrentUser(Pageable pageable);
    
    /**
     * Gets SlidePost object by id.
     * SECURITY: logged in user must has ROLE_AUTHOR and must be the author of the slide;
     * Or logged in user has ROLE_ADMIN.
     * 
     * @param id
     * @return
     */
    SlidePost getSlideById(String id);

    /**
     * Create a SlidePost by login user.
     * SECURITY: logged in user must has ROLE_AUTHOR.
     * 
     * @param title
     * @param content
     * @return new SlidePost object with author as login user's userId.
     */
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    SlidePost createSlide(StyleType type, String title, String content, String path);

    /**
     * Update SlidePost by signed in user.
     * SECURITY: logged in user must has ROLE_AUTHOR and must be the author of the slide;
     * Or logged in user has ROLE_ADMIN.
     * 
     * @param SlidePost
     * @return
     */
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    SlidePost updateSlide(SlidePost slidePost);


}
