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

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.AlphaMailServiceException;
import com.comfirm.alphamail.services.client.entities.EmailContact;
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
        EmailContact receiver = new EmailContact(getSenderName(), getSenderEmail());  //send to me now. TODO send to author
        Payload payload  = new Payload();
        payload.author = comment.getBlogPost().getAuthorId();
        payload.user = comment.getAuthorAccount().getDisplayName();
        payload.post = comment.getBlogPost().getFullPublishedPath();
        payload.comment = comment.getContent();
                
        try {
            doSend(receiver, payload);
        } catch (AlphaMailServiceException e) {
            e.printStackTrace();
            String logMessage = String.format("AlphaMail returned exception: Error Code: %s, Error Message: %s", e
                    .getResponse().getErrorCode(), e.getResponse().getMessage());
            logger.warn(logMessage);
            // TODO display error in UI and let user contact Yuan?
        }

    }
    
    public class Payload {
        public String author;
        public String user;
        public String post;
        public String comment;
    }

}
