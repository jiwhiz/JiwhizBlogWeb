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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Domain Entity for blog post.
 * 
 * @author Yuan Ji
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "BlogPost")
public class BlogPost extends AbstractPost {

    private String title;

    @Indexed
    private Date publishedTime;

    @Indexed
    private int publishedYear;

    @Indexed
    private int publishedMonth;

    @Indexed
    private String publishedPath;

    private List<String> tags;

    @Transient
    private int commentCount;

    @Transient
    private List<CommentPost> comments;

    @Transient
    private long visits;
    
    public long getVisits() {
        return visits;
    }

    void setVisits(long visits) {
        this.visits = visits;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getPublishedMonth() {
        return publishedMonth;
    }

    void setPublishedMonth(int publishedMonth) {
        this.publishedMonth = publishedMonth;
    }

    public String getPublishedPath() {
        return publishedPath;
    }

    void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<CommentPost> getComments() {
        return comments;
    }

    public void setComments(List<CommentPost> comments) {
        this.comments = comments;
    }

    public BlogPost() {
        this.tags = new ArrayList<String>();
        this.comments = new ArrayList<CommentPost>();
    }

    public BlogPost(String authorId, String title, String content, String tagString) {
        super(authorId, content);
        this.title = title;
        this.tags = new ArrayList<String>();
        parseAndSetTags(tagString);
        this.comments = new ArrayList<CommentPost>();
    }

    void updateContent(String content) {
        setContent(content);
        triggerModified();
    }

    void updateMeta(String title, String tagString) {
        this.title = title;
        parseAndSetTags(tagString);
        triggerModified();
    }

    void parseAndSetTags(String tagString) {
        tags.clear();
        for (String tag : tagString.split(",")) {
            String newTag = tag.trim();
            if (newTag.length() > 0) {
                tags.add(newTag);
            }
        }
    }

    void publish(String path) {
        assert isPublished() == false;

        setPublished(true);
        publishedPath = path;
        publishedTime = new Date();
        publishedYear = Calendar.getInstance().get(Calendar.YEAR);
        publishedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        // month starts at 0 for January, but we want to display 1.
        triggerModified();
    }

    void unpublish() {
        assert isPublished() == true;
        setPublished(false);
        triggerModified();
    }

    public String getPublishedDateString() {
        if (!isPublished()) {
            return "";
        }
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return formatter.format(publishedTime);
    }

    public String getPublishedTimeString() {
        if (!isPublished()) {
            return "";
        }
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        return formatter.format(publishedTime);
    }

    public String getPublishedDateTimeString() {
        if (!isPublished()) {
            return "";
        }
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return formatter.format(publishedTime);
    }

    public String getFormatTagString() {
        StringBuffer sb = new StringBuffer();
        if (tags.size() > 0) {
            Iterator<String> iter = tags.iterator();
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(", ");
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    public String getFullPublishedPath() {
        return getPublishedYear() + "/" + getPublishedMonth() + "/" + getPublishedPath();
    }

    /**
     * TODO better way to get summary or first paragraph of the blog post.
     * 
     * @return
     */
    public String getContentFirstParagraph() {
        int start = getContent().indexOf("<p>");
        int end = getContent().indexOf("</p>");
        if (start < 0){
            //no <p> in the post, get the whole content
            return getContent();
        }
        
        if (start > 50){
            return getContent().substring(0, start);
        }
        
        if (end < 0) { //no </p>
            end = getContent().length();
        }
        
        return getContent().substring(start+3, end);
    }

    public String toString() {
        return String.format("BlogPost{id='%s';path='%s';title='%s'}", getId(), getFullPublishedPath(), getTitle());
    }
}
