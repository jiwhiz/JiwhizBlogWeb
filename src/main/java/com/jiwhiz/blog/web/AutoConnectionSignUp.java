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
package com.jiwhiz.blog.web;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountService;

/**
 * Automatically sign up user who is already signin through other social network account (google or twitter).
 * Create a new UserAccount in database, populate user's profile data from provider.
 * 
 * @author Yuan Ji
 *
 */
public class AutoConnectionSignUp implements ConnectionSignUp{
    private static final Logger logger = LoggerFactory.getLogger(AutoConnectionSignUp.class);
    
    private final UserAccountService userAccountService;
    private final SystemMessageSender systemMessageSender;
    
    @Inject
    public AutoConnectionSignUp(UserAccountService userAccountService, SystemMessageSender systemMessageSender){
        this.userAccountService = userAccountService;
        this.systemMessageSender = systemMessageSender;
    }
    
    public String execute(Connection<?> connection) {
        ConnectionData data = connection.createData();
        UserAccount account = this.userAccountService.createUserAccount(data);
        systemMessageSender.sendNewUserRegistered(account);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Automatically create a new user account '"+account.getUserId()+"', for "+account.getDisplayName());
            logger.debug("connection data is from provider '"+data.getProviderId()+"', providerUserId is '"+data.getProviderUserId());
        }
        return account.getUserId();
    }
}
