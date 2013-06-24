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
import java.util.List;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;

public class BlogPostDTO {
    private String blogId;

    private String title;

    private String content;

    private String contentFirstParagraph;

    private boolean published;

    private Date publishedTime;

    private int publishedYear;

    private int publishedMonth;

    private String publishedPath;

    private String fullPublishedPath;

    private List<String> tags;

    private String tagString;

    private int totalCommentCount;

    private int approvedCommentCount;

    private int pendingCommentCount;

    private List<CommentPostDTO> comments;

    private UserAccountDTO author;

    public String getBlogId() {
        return blogId;
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

    public String getContentFirstParagraph() {
        return this.contentFirstParagraph;
    }

    public boolean isPublished() {
        return published;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getPublishedMonth() {
        return publishedMonth;
    }

    public void setPublishedMonth(int publishedMonth) {
        this.publishedMonth = publishedMonth;
    }

    public String getPublishedPath() {
        return publishedPath;
    }

    public void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }

    public String getFullPublishedPath() {
        return fullPublishedPath;
    }

    public void setFullPublishedPath(String fullPublishedPath) {
        this.fullPublishedPath = fullPublishedPath;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public int getTotalCommentCount() {
        return totalCommentCount;
    }

    public int getApprovedCommentCount() {
        return approvedCommentCount;
    }

    public int getPendingCommentCount() {
        return pendingCommentCount;
    }

    public List<CommentPostDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentPostDTO> comments) {
        this.comments = comments;
    }

    public UserAccountDTO getAuthor() {
        return author;
    }

    public BlogPostDTO() {}

    public BlogPostDTO(BlogPost blog) {
        this(blog, true);
    }

    public BlogPostDTO(BlogPost blog, boolean complete) {
        this.blogId = blog.getPostId();
        this.title = blog.getTitle();
        this.fullPublishedPath = blog.getFullPublishedPath();
        this.contentFirstParagraph = blog.getContentFirstParagraph();
        this.published = blog.isPublished();

        if (complete) {
            this.content = blog.getContent();
            this.publishedTime = blog.getPublishedTime();
            this.publishedYear = blog.getPublishedYear();
            this.publishedMonth = blog.getPublishedMonth();
            this.publishedPath = blog.getPublishedPath();
            this.tags = blog.getTags();
            this.tagString = blog.getFormatTagString();
        }
    }

    public BlogPostDTO(BlogPost blog, int totalCommentCount, int approvedCommentCount, int pendingCommentCount) {
        this(blog);
        this.totalCommentCount = totalCommentCount;
        this.approvedCommentCount = approvedCommentCount;
        this.pendingCommentCount = pendingCommentCount;
    }

    public BlogPostDTO(BlogPost blog, int approvedCommentCount, UserAccount author) {
        this(blog);
        this.approvedCommentCount = approvedCommentCount;
        if (author != null) {
            this.author = new UserAccountDTO(author);
        }
    }

}
