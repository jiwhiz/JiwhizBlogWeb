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

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jiwhiz.blog.domain.account.UserAccount;

/**
 * Domain Entity for blog comment.
 * 
 * @author Yuan Ji
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "CommentPost")
public class CommentPost extends AbstractPost {
    /*
     * Reference to BlogPost commented by this CommentPost
     */
    @Indexed
    private String blogPostKey;
    
    private CommentStatusType status;
    
    public String getBlogPostKey() {
        return blogPostKey;
    }

    public CommentStatusType getStatus() {
        return status;
    }

    public CommentPost() {

    }

    public CommentPost(String postId, UserAccount author, BlogPost blog, String content) {
        super(postId, author, content);
        this.blogPostKey = blog.getKey();
        this.status = CommentStatusType.PENDING;
    }
    
    public CommentPost update(String newContent) {
        setContent(newContent);
        return this;
    }
    
    public CommentPost approve() {
        assert status == CommentStatusType.PENDING;
        this.status = CommentStatusType.APPROVED;
        return this;
    }
    
    public CommentPost disapprove() {
        assert status == CommentStatusType.APPROVED;
        this.status = CommentStatusType.PENDING;
        return this;
    }
    
    public CommentPost markSpam() {
        assert status == CommentStatusType.PENDING;
        this.status = CommentStatusType.SPAM;
        return this;
    }
    
    public CommentPost unmarkSpam() {
        assert status == CommentStatusType.SPAM;
        this.status = CommentStatusType.PENDING;
        return this;
    }
    
    public String toString() {
        return String.format("Comment{ author : '%s'; status : '%s', created : '%tT'}", 
                (getAuthorAccount() == null) ? getAuthorKey() : getAuthorAccount().getDisplayName(), 
                getStatus(), getCreatedTime());
    }
}
