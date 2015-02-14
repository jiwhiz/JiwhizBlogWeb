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
package com.jiwhiz.rest;

/**
 * @author Yuan Ji
 */
public interface ApiUrls {
    String API_ROOT = "/api";
    
    String URL_SITE = "/public";
    String URL_SITE_CURRENT_USER = "/currentUser";
    String URL_SITE_PROFILES = "/public/profiles";
    String URL_SITE_PROFILES_USER = "/public/profiles/{userId}";
    String URL_SITE_PROFILES_USER_COMMENTS = "/public/profiles/{userId}/comments";
    String URL_SITE_LATEST_BLOG = "/public/latestBlog";
    String URL_SITE_RECENT_BLOGS = "/public/recentBlogs";
    String URL_SITE_RECENT_COMMENTS = "/public/recentComments";
    String URL_SITE_TAG_CLOUDS = "/public/tagClouds";
    String URL_SITE_CONTACT = "/public/contact";
    String URL_SITE_BLOGS = "/public/blogs";
    String URL_SITE_BLOGS_BLOG = "/public/blogs/{blogId}";
    String URL_SITE_BLOGS_BLOG_COMMENTS = "/public/blogs/{blogId}/comments";
    String URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT = "/public/blogs/{blogId}/comments/{commentId}";
    
    String URL_USER = "/user";
    String URL_USER_PROFILE = "/user/profile";
    String URL_USER_SOCIAL_CONNECTIONS = "/user/socialConnections";
    String URL_USER_BLOGS_BLOG_COMMENTS = "/user/blogs/{blogId}/comments";
    String URL_USER_COMMENTS = "/user/comments";
    String URL_USER_COMMENTS_COMMENT = "/user/comments/{commentId}";
    
    String URL_AUTHOR = "/author";
    String URL_AUTHOR_BLOGS = "/author/blogs";
    String URL_AUTHOR_BLOGS_BLOG = "/author/blogs/{blogId}";
    String URL_AUTHOR_BLOGS_BLOG_COMMENTS = "/author/blogs/{blogId}/comments";
    String URL_AUTHOR_BLOGS_BLOG_COMMENTS_COMMENT = "/author/blogs/{blogId}/comments/{commentId}";
    
    String URL_ADMIN = "/admin";
    String URL_ADMIN_USERS = "/admin/users";
    String URL_ADMIN_USERS_USER = "/admin/users/{userId}";
    String URL_ADMIN_USERS_USER_SOCIAL_CONNECTIONS = "/admin/users/{userId}/socialConnections";
    String URL_ADMIN_BLOGS = "/admin/blogs";
    String URL_ADMIN_BLOGS_BLOG = "/admin/blogs/{blogId}";
    String URL_ADMIN_BLOGS_BLOG_COMMENTS = "/admin/blogs/{blogId}/comments";
    String URL_ADMIN_COMMENTS = "/admin/comments";
    String URL_ADMIN_COMMENTS_COMMENT = "/admin/comments/{commentId}";
    
}
