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
package com.jiwhiz.domain.account;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Domain Service interface for user administration. It also extends SocialUserDetailsService,
 * and UserDetailsService.
 * 
 * @author Yuan Ji
 *
 */
public interface UserAccountService extends SocialUserDetailsService, UserDetailsService {
    
    /**
     * Creates a new UserAccount with user social network account Connection Data and UserProfile.
     * Default has ROLE_USER role.
     * 
     * @param data
     * @param profile
     * @return
     */
    UserAccount createUserAccount(ConnectionData data, UserProfile profile);

    /**
     * Add role to user account.
     * 
     * @param userId
     * @param role
     */
    UserAccount addRole(String userId, UserRoleType role);

    /**
     * Remove role from user account.
     * 
     * @param userId
     * @param role
     */
    UserAccount removeRole(String userId, UserRoleType role);

    /**
     * Override SocialUserDetailsService.loadUserByUserId(String userId) to 
     * return UserAccount.
     */
    UserAccount loadUserByUserId(String userId) throws UsernameNotFoundException;
    
    /**
     * Gets current logged in user. Reload UserAccount object from database. 
     * 
     * @return UserAccount object from database for current user. Null if no logged in user.
     */
    UserAccount getCurrentUser();

}
