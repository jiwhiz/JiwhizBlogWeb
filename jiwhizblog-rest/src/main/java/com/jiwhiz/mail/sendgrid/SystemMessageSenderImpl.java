/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
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
package com.jiwhiz.mail.sendgrid;

import org.springframework.util.StringUtils;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.mail.SystemMessageSender;

/**
 * @author Yuan Ji
 */
public class SystemMessageSenderImpl extends AbstractMailSender implements SystemMessageSender {

    @Override
    public void sendNewUserRegistered(UserAccount user) {
        String userEmail = user.getEmail();
        if (StringUtils.isEmpty(userEmail)){
            userEmail = "[not set]";
        }
        String subject = "New user registered";
        String message = "A new user registered: name is "+user.getDisplayName()+", email is "+userEmail;
        doSend(getSystemEmail(), getAdminEmail(), subject, message);        
    }

    @Override
    public void sendNewPostPublished(UserAccount author, BlogPost blog) {
        String subject = "New post published";
        String message = "A new post was published: title is "+blog.getTitle()+", author is "+author.getDisplayName();
        doSend(getSystemEmail(), getAdminEmail(), subject, message);        
        
    }

}
