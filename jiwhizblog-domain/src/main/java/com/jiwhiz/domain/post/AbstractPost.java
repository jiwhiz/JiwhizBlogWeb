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

import org.springframework.data.mongodb.core.index.Indexed;

import com.jiwhiz.domain.BaseAuditableEntity;


/**
 * Abstract super class for domain entities related to post, like BlogPost, CommentPost, SlidePost.
 * 
 * @author Yuan Ji
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractPost extends BaseAuditableEntity{

    /*
     * Reference to userId of UserAccount object as the post author.
     */
    @Indexed
    private String authorId;

    private String content;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content){
        this.content = content;
    }

    public AbstractPost() {
        
    }
    
    public AbstractPost(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

}
