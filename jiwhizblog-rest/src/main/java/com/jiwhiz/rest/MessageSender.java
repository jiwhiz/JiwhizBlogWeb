/* 
 * Copyright 2013-2015 JIWHIZ Consulting Inc.
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
package com.jiwhiz.rest;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.mail.ContactForm;
import com.jiwhiz.mail.EmailMessage;
import com.jiwhiz.mail.EmailProperties;
import com.jiwhiz.mail.EmailService;

@Service
public class MessageSender {

    @Inject
    EmailProperties properties;
    
    @Inject
    EmailService emailService;
    
    /**
     * Send message to blog author when a new comment was posted to the blog.
     * 
     * @param receivingUser
     * @param commentUser
     * @param comment
     * @param blog
     */
    public void notifyAuthorForNewComment(UserAccount blogAuthor, UserAccount commentUser, CommentPost comment, BlogPost blog) {
        String subject = "You got comment on your blog post " + blog.getTitle();
        String message = commentUser.getDisplayName() + " posted a comment on your blog: " + comment.getContent();
        emailService.sendEmail(new EmailMessage(properties, blogAuthor.getEmail(), blogAuthor.getDisplayName(), subject, message, null));
    }
    
    /**
     * Send contact messages by Web contact form in Contact page to administrator.
     * 
     * @param contact
     */
    public void notifyAdminForContactMessage(ContactForm contact) {
        String subject = "Contact message from " + contact.getName();
        String message = contact.getName() + " tried to contact you on your website: " + contact.getMessage();
        emailService.sendEmail(new EmailMessage(properties, subject, message, contact.getEmail()));
     }

    /**
     * Send message to admin when a new user registered by auto signup.
     * @param user
     */
    public void sendNewUserRegistered(UserAccount user) {
        String userEmail = user.getEmail();
        if (StringUtils.isEmpty(userEmail)){
            userEmail = "[not set]";
        }
        String subject = "New user registered";
        String message = "A new user registered: name is " + user.getDisplayName() + ", email is " + userEmail;
        emailService.sendEmail(new EmailMessage(properties, subject, message, null));
    }
    
    /**
     * Send message to admin when a new blog post was published.
     * @param author
     * @param blog
     */
    public void sendNewPostPublished(UserAccount author, BlogPost blog) {
        String subject = "New post published";
        String message = "A new post was published: title is " + blog.getTitle() + ", author is " + author.getDisplayName();
        emailService.sendEmail(new EmailMessage(properties, subject, message, null));
    }

}
