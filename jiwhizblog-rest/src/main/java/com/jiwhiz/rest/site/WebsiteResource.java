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
package com.jiwhiz.rest.site;

import org.springframework.hateoas.ResourceSupport;

/**
 * @author Yuan Ji
 */
public class WebsiteResource extends ResourceSupport {
    public static final String LINK_NAME_BLOGS = "blogs";
    public static final String LINK_NAME_BLOG = "blog";
    public static final String LINK_NAME_CURRENT_USER = "currentUser";
    public static final String LINK_NAME_LATEST_BLOG = "latestBlog";
    public static final String LINK_NAME_RECENT_BLOGS = "recentBlogs";
    public static final String LINK_NAME_RECENT_COMMENTS = "recentComments";
    public static final String LINK_NAME_TAG_CLOUD = "tagCloud";
    public static final String LINK_NAME_PROFILE = "profile";

    public static final String LINK_NAME_POST_COMMENT = "postComment";
    
    private boolean authenticated = false;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
}
