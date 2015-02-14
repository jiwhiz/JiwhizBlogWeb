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
package com.jiwhiz.rest.admin;

import org.springframework.hateoas.Resource;

import com.jiwhiz.domain.account.UserAccount;

/**
 * REST resource for admin user account with links to users, blogs, comments.
 * 
 * @author Yuan Ji
 */
public class AdminAccountResource extends Resource<UserAccount> {
    public static final String LINK_NAME_USERS = "users";
    public static final String LINK_NAME_BLOGS = "blogs";
    public static final String LINK_NAME_COMMENTS = "comments";
    
    public AdminAccountResource(UserAccount account) {
        super(account);
    }


}
