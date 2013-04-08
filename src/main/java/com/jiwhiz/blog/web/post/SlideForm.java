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

import org.hibernate.validator.constraints.NotEmpty;

import com.jiwhiz.blog.domain.post.SlidePost;
import com.jiwhiz.blog.domain.post.StyleType;

/**
 * @author Yuan Ji
 *
 */
public class SlideForm {
    private String id;
    
    private StyleType type;
    
    @NotEmpty
    private String title;
    
    @NotEmpty
    private String content;
    
    @NotEmpty
    private String publishedPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StyleType getType() {
        return type;
    }

    public void setType(StyleType type) {
        this.type = type;
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

    public String getPublishedPath() {
        return publishedPath;
    }

    public void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }

    public SlideForm(){
        
    }
    
    public SlideForm(SlidePost slidePost){
        this.id = slidePost.getId();
        this.type = slidePost.getType();
        this.publishedPath = slidePost.getPublishedPath();
        this.title = slidePost.getTitle();
        this.content = slidePost.getContent();
    }


}
