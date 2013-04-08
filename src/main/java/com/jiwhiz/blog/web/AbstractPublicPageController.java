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
package com.jiwhiz.blog.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;

/**
 * @author Yuan Ji
 *
 */
public class AbstractPublicPageController extends AbstractPageController{
    public static final int RECENT_POST_COUNT = 4;
    
    public AbstractPublicPageController(){
    }
    
    @ModelAttribute("recentPosts")
    public List<BlogPost> addRecentPosts() {
        return blogPostService.getPublishedPosts(RECENT_POST_COUNT);
    }
    
    @ModelAttribute("recentComments")
    public List<CommentPost> addRecentComments() {
        return commentPostService.getPublishedComments(RECENT_POST_COUNT);
    }

    @ModelAttribute("tagCloud")
    public Map<String, Integer> addTagCloud() {
        return blogPostService.getTagCloud();
    }

    @ModelAttribute("pageVisit")
    public long addPageVisit() {
        return counterService.getVisitCount();
    }

}
