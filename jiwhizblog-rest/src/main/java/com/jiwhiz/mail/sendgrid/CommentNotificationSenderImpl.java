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

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.mail.CommentNotificationSender;

/**
 * SendGrid impl for CommentNotificationSender.
 * 
 * @author Yuan Ji
 *
 */
public class CommentNotificationSenderImpl extends AbstractMailSender implements CommentNotificationSender {
    
    @Override
    public void send(UserAccount blogAuthor, UserAccount commentUser, CommentPost comment, BlogPost blog) {
        String subject = "You got comment on your blog post "+blog.getTitle();
        String message = commentUser.getDisplayName()+ " posted a comment on your blog: " + comment.getContent();
        doSend(getSystemEmail(), blogAuthor.getEmail(), subject, message);        
    }

}
