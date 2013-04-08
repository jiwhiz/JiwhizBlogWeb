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
package com.jiwhiz.blog.web.post;

import com.jiwhiz.blog.domain.post.BlogPost;

/**
 * 
 * @author Yuan Ji
 *
 */
public class BlogForm {
    private String id;
    
    private String title;
    
    private String content;
    
    private String tagString;
    
    private int publishedYear;

    private int publishedMonth;

    private String publishedPath;
    
    private boolean published;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }


    public String getPublishedPath() {
        return publishedPath;
    }

    public void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }
    
    public boolean isPublished() {
        return published;
    }

    public BlogForm(){
        
    }
    
    public BlogForm(BlogPost blogPost){
        this.id = blogPost.getId();
        this.published = blogPost.isPublished();
        this.publishedYear = blogPost.getPublishedYear();
        this.publishedMonth = blogPost.getPublishedMonth();
        this.publishedPath = blogPost.getPublishedPath();
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.tagString = blogPost.getFormatTagString();
    }

    public String getYearMonthPath(){
        return "/"+this.publishedYear+"/"+this.publishedMonth+"/";
    }
}
