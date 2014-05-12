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
package com.jiwhiz.mail;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.CommentPost;

/**
 * @author Yuan Ji
 *
 */
public interface CommentNotificationSender {
    /**
     * Send message to user when a new comment was posted to the blog.
     * 
     * @param receivingUser
     * @param commentUser
     * @param comment
     * @param blog
     */
    void send(UserAccount receivingUser, UserAccount commentUser, CommentPost comment, BlogPost blog);
}
