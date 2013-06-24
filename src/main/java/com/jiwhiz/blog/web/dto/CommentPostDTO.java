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
package com.jiwhiz.blog.web.dto;

import java.util.Date;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;

public class CommentPostDTO {
    private String commentId;

    private String content;
    
    private Date postedTime;
    
    private String statusString;
    
    private UserAccountDTO user;

    private BlogPostDTO blog;
    
    public String getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostedTime() {
        return postedTime;
    }

    public String getStatusString() {
        return statusString;
    }
    
    public UserAccountDTO getUser() {
        return user;
    }

    public void setUser(UserAccountDTO user) {
        this.user = user;
    }

    public BlogPostDTO getBlog() {
        return blog;
    }

    public void setBlog(BlogPostDTO blog) {
        this.blog = blog;
    }

    public CommentPostDTO() {}
    
    public CommentPostDTO(CommentPost comment) {
        this.commentId = comment.getPostId();
        this.content = comment.getContent();
        this.postedTime = comment.getCreatedTime();
        this.statusString = comment.getStatus().name();
    }
    
    public CommentPostDTO(CommentPost comment, UserAccount user, BlogPost blog) {
        this(comment);
        if (user != null) {
            this.user = new UserAccountDTO(user);
        }
        if (blog != null) {
            this.blog = new BlogPostDTO();
            this.blog.setTitle(blog.getTitle());
            this.blog.setFullPublishedPath(blog.getFullPublishedPath());
        }
    }    
}
