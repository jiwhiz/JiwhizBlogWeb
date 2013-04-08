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

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Domain Entity for slide post.
 * 
 * @author Yuan Ji
 *
 */
@SuppressWarnings("serial")
@Document(collection = "SlidePost")
public class SlidePost extends AbstractPost{
    
    private StyleType type;
    
    private String title;
    
    @Indexed
    private String publishedPath;
    
    @Transient
    private long visits;
    
    public long getVisits() {
        return visits;
    }

    void setVisits(long visits) {
        this.visits = visits;
    }

    public String getPublishedPath() {
        return publishedPath;
    }

    public void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StyleType getType() {
        return type;
    }

    void setType(StyleType type) {
        this.type = type;
    }

    public SlidePost() {
        super();
    }
    
    public SlidePost (String authorId, StyleType type, String title, String content, String path) {
        super(authorId, content);
        this.type = type;
        this.title = title;
        this.publishedPath = path;
    }
    
    public void update(String path, String title, String content) {
        this.publishedPath = path;
        this.title = title;
        setContent(content);
        triggerModified();
    }
    
}
