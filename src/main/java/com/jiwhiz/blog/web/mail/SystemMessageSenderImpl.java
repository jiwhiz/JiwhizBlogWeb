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
import com.jiwhiz.blog.web.SystemMessageSender;

public class SystemMessageSenderImpl extends AbstractMailSender implements SystemMessageSender {
    final static Logger logger = LoggerFactory.getLogger(SystemMessageSenderImpl.class);
    
    public SystemMessageSenderImpl(AlphaMailService mailService){
        super(mailService);
    }

    @Override
    public void sendNewUserRegistered(UserAccount user) {
        Payload payload = new Payload();
        payload.message = String.format("A new user '%s' was registered. See %s", user.getDisplayName(), 
                getUserProfileBaseUrl()+user.getUserId());
        doSend(new EmailContact(getSystemName(), getSystemEmail()),
               new EmailContact(getAdminName(), getAdminEmail()),
               payload);
        
    }

    @Override
    public void sendNewPostPublished(UserAccount author, BlogPost blog) {
        Payload payload = new Payload();
        payload.message = String.format("'%s' published a new blog. See %s", 
                author.getDisplayName(), 
                getBlogBaseUrl()+blog.getFullPublishedPath());
        doSend(new EmailContact(getSystemName(), getSystemEmail()),
               new EmailContact(getAdminName(), getAdminEmail()),
               payload);
        
    }
    
    public class Payload {
        public String message;
    }

}
