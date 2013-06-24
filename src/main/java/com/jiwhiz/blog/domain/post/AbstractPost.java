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
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
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

    /*
     * Reference to UserAccount object of the post author.
     */
    @Indexed
    private String authorKey;

    /*
     * Application generated unique ID for public usage.
     */
    @Indexed(unique=true)
    private String postId;

    private String content;
    
    private Date createdTime;
    
    private Date lastModifiedTime;
    
    @Transient
    private UserAccount authorAccount;

    public String getAuthorKey() {
        return authorKey;
    }

    public String getPostId() {
        return postId;
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

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public UserAccount getAuthorAccount() {
        return authorAccount;
    }

    public void setAuthorAccount(UserAccount authorAccount) {
        this.authorAccount = authorAccount;
    }
    
    public AbstractPost() {
        
    }

    /**
     * Constructor for creating a new post object.
     * 
     * @param postId Application generated unique ID
     * @param author UserAccount object of author
     * @param content
     */
    public AbstractPost(String postId, UserAccount author, String content) {
        this.postId = postId;
        this.authorAccount = author;
        this.authorKey = author.getKey();
        this.content = content;
        this.createdTime = new Date();
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
