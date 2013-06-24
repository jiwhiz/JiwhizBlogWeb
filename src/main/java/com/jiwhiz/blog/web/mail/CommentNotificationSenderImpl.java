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
package com.jiwhiz.blog.web.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.entities.EmailContact;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.CommentNotificationSender;

/**
 * @author Yuan Ji
 *
 */
public class CommentNotificationSenderImpl extends AbstractMailSender implements CommentNotificationSender {
    final static Logger logger = LoggerFactory.getLogger(CommentNotificationSenderImpl.class);
    
    public CommentNotificationSenderImpl(AlphaMailService mailService){
        super(mailService);
    }

    @Override
    public void send(UserAccount receivingUser, UserAccount commentUser, CommentPost comment, BlogPost blog) {    	
        Payload payload  = new Payload();
        payload.receiver = receivingUser.getDisplayName();
        payload.user = commentUser.getDisplayName();
        payload.postTitle = blog.getTitle();
        payload.postUrl = getBlogBaseUrl() + blog.getFullPublishedPath();
        payload.comment = comment.getContent();
        
        doSend(new EmailContact(getSystemName(), getSystemEmail()),
               new EmailContact(receivingUser.getDisplayName(), receivingUser.getEmail()),
               payload);
    }
    
    public class Payload {
        public String receiver;
        public String user;
        public String postUrl;
        public String postTitle;
        public String comment;
        
        public String toString() {
            return String.format("Payload{receiver:'%s', user:'%s', post:'%s', comment:'%s'", receiver, user, postTitle, comment);
        }
    }

}
