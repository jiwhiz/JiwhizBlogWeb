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

import java.io.StringReader;
import java.text.DateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import com.jiwhiz.blog.domain.BaseEntity;
import com.jiwhiz.blog.domain.account.UserAccount;

/**
 * Abstract super class for domain entities related to post, like BlogPost, CommentPost, SlidePost.
 * 
 * @author Yuan Ji
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractPost extends BaseEntity{

    @Indexed
    private String authorId;
    
    private String content;
    
    private Date createdTime;
    
    private Date lastModifiedTime;
    
    private boolean published;
    
    @Transient
    private UserAccount authorAccount;

    public String getAuthorId() {
        return authorId;
    }

    void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    
    public String getContent() {
        return content;
    }
    
    void setContent(String content){
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public boolean isPublished() {
        return published;
    }
    
    void setPublished(boolean published){
        this.published = published;
    }

    public UserAccount getAuthorAccount() {
        return authorAccount;
    }

    public void setAuthorAccount(UserAccount authorAccount) {
        this.authorAccount = authorAccount;
    }
    
    public AbstractPost() {
        
    }

    public AbstractPost(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.createdTime = new Date();
    }

    public String getCreatedDateTimeString() {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return formatter.format(createdTime);
    }

    public String getLastModifiedDateTimeString() {
        if (lastModifiedTime == null) {
            return "";
        }
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return formatter.format(lastModifiedTime);
    }

    public String getAuthorName() {
        if (authorAccount != null) {
            return authorAccount.getDisplayName();
        }
        return authorId;
    }

    public String getAuthorNameLink() {
        if (authorAccount != null) {
            return authorAccount.getNameLink();
        }
        return authorId;
    }

    public boolean isHasImageUrl(){
        if (authorAccount != null && StringUtils.hasLength(authorAccount.getImageUrl())){
            return true;
        }
        return false;
    }

    public String getImageUrl(){
        if (authorAccount != null && StringUtils.hasLength(authorAccount.getImageUrl())){
            return authorAccount.getImageUrl();
        }
        return null;
    }

    void triggerModified(){
        this.lastModifiedTime = new Date();
    }
    
    /**
     * Validates content by XML parser.
     * TODO: find a better way to do the validation for HTML5.
     * 
     * @return
     */
    public String validateHtmlContent() {
        String htmlContent = "<?xml version=\"1.0\"?><xml>" + getContent() + "</xml>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(htmlContent)));
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return null;
    }

}
