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
package com.jiwhiz.domain.account.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import com.jiwhiz.domain.account.UserSocialConnection;
import com.jiwhiz.domain.account.UserSocialConnectionRepository;

/**
 * Implementation for Spring Social {@link UsersConnectionRepository}.
 *
 * @author Yuan Ji
 *
 */
public class MongoUsersConnectionRepositoryImpl implements UsersConnectionRepository{
    
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    
    private final SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;

    private final TextEncryptor textEncryptor;

    private ConnectionSignUp connectionSignUp;

    public MongoUsersConnectionRepositoryImpl(UserSocialConnectionRepository userSocialConnectionRepository, 
            SocialAuthenticationServiceLocator socialAuthenticationServiceLocator, TextEncryptor textEncryptor){
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.socialAuthenticationServiceLocator = socialAuthenticationServiceLocator;
        this.textEncryptor = textEncryptor;
    }
    
    /**
     * The command to execute to create a new local user profile in the event no user id could be mapped to a connection.
     * Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
     * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
     * @see #findUserIdsWithConnection(Connection)
     */
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<UserSocialConnection> userSocialConnectionList = 
                this.userSocialConnectionRepository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        List<String> localUserIds = new ArrayList<String>();
        for (UserSocialConnection userSocialConnection : userSocialConnectionList){
            localUserIds.add(userSocialConnection.getUserId());
        }
        
        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }
        return localUserIds;
    }

    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Set<String> localUserIds = new HashSet<String>();

        List<UserSocialConnection> userSocialConnectionList = 
                this.userSocialConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
        for (UserSocialConnection userSocialConnection : userSocialConnectionList){
            localUserIds.add(userSocialConnection.getUserId());
        }
        return localUserIds;
    }

    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new MongoConnectionRepositoryImpl(userId, userSocialConnectionRepository, socialAuthenticationServiceLocator, textEncryptor);
    }

}
