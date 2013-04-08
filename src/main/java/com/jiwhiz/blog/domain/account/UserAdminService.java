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

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Domain Service interface for user administration. It also extends SocialUserDetailsService,
 * UserDetailsService and UserIdSource.
 * 
 * @author Yuan Ji
 *
 */
public interface UserAdminService extends SocialUserDetailsService, UserDetailsService, UserIdSource{
    
    /**
     * Creates a new UserAccount with user social network account Connection Data.
     * Default has ROLE_USER
     * 
     * @param data
     * @return
     */
    UserAccount createUserAccount(ConnectionData data);

    /**
     * Add Author role to user account or remove Author role from user account.
     * 
     * @param userId
     * @param isAuthor
     */
    void setAuthor(String userId, boolean isAuthor);
    
    /**
     * Override SocialUserDetailsService.loadUserByUserId(String userId) to 
     * return UserAccount.
     */
    UserAccount loadUserByUserId(String userId) throws UsernameNotFoundException;
    
    /**
     * Gets current logged in user. Reload UserAccount object from userId in SecurityContextHolder. 
     * 
     * @return UserAccount object from database for current user
     */
    UserAccount getCurrentUser();

}
