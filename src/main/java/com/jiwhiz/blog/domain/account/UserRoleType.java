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
package com.jiwhiz.blog.domain.account;

import org.springframework.security.core.GrantedAuthority;

/**
 * Security role type for UserAccount.
 * 
 * @author Yuan Ji
 *
 */
public enum UserRoleType implements GrantedAuthority{
    ROLE_ADMIN,  // can manage user account, all posts
    ROLE_AUTHOR, // can manage own posts
    ROLE_USER   // can edit own comment, can edit own profile
    ;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
