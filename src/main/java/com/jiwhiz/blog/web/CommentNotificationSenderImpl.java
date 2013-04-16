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
package com.jiwhiz.blog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.AlphaMailServiceException;
import com.comfirm.alphamail.services.client.entities.EmailContact;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.mail.AbstractMailSender;

/**
 * @author Yuan Ji
 *
 */
public class CommentNotificationSenderImpl extends AbstractMailSender implements CommentNotificationSender {
    final static Logger logger = LoggerFactory.getLogger(CommentNotificationSenderImpl.class);
    
    public CommentNotificationSenderImpl(AlphaMailService mailService){
        super(mailService);
    }

    /* (non-Javadoc)
     * @see com.jiwhiz.blog.web.CommentNotificationSender#send(com.jiwhiz.blog.domain.post.CommentPost)
     */
    @Override
    public void send(CommentPost comment) {
    	UserAccount blogAuthor = comment.getBlogPost().getAuthorAccount();
    	
    	EmailContact sender = new EmailContact(comment.getAuthorAccount().getDisplayName(), comment.getAuthorAccount().getEmail());
        EmailContact receiver = new EmailContact(blogAuthor.getDisplayName(), blogAuthor.getEmail());
        EmailContact admin = new EmailContact(getAdminName(), getAdminEmail());
        Payload payload  = new Payload();
        payload.author = comment.getBlogPost().getAuthorAccount().getDisplayName();
        payload.user = comment.getAuthorAccount().getDisplayName();
        payload.post = comment.getBlogPost().getFullPublishedPath();
        payload.comment = comment.getContent();

        try {
            if (StringUtils.isEmpty(blogAuthor.getEmail())){
                logger.info(String.format("Cannot send comment notification email. User %s email address is not set.", blogAuthor.getUserId()));
            } else {
                doSend(sender, receiver, payload);
            }
            
            if (!admin.getEmail().equals(receiver.getEmail())){
                payload.author = "admin";
                doSend(sender, admin, payload);
            }
        } catch (AlphaMailServiceException e) {
            e.printStackTrace();
            String logMessage = String.format("AlphaMail returned exception: Error Code: %s, Error Message: %s", e
                    .getResponse().getErrorCode(), e.getResponse().getMessage());
            logger.warn(logMessage);
            // TODO display error in UI and let user contact admin?
        }

    }
    
    public class Payload {
        public String author;
        public String user;
        public String post;
        public String comment;
        
        public String toString() {
            return String.format("Payload{author:'%s', user:'%s', post:'%s', comment:'%s'", author, user, post, comment);
        }
    }

}
