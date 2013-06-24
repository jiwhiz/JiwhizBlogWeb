package com.jiwhiz.blog.web;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;

/**
* Send system messages to web site administrator.
* 
* @author Yuan Ji
*
*/
public interface SystemMessageSender {
    /**
     * Send message to admin when a new user registered by auto signup.
     * @param user
     */
    void sendNewUserRegistered(UserAccount user);
    
    /**
     * Send message to admin when a new blog post was published.
     * @param author
     * @param blog
     */
    void sendNewPostPublished(UserAccount author, BlogPost blog);
}
