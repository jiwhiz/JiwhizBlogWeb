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

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB Repository for UserSocialConnection entity.
 * 
 * @author Yuan Ji
 *
 */
public interface UserSocialConnectionRepository extends MongoRepository<UserSocialConnection, String>{
    
    List<UserSocialConnection> findByUserId(String userId);
    
    List<UserSocialConnection> findByUserIdAndProviderId(String userId, String providerId);
    
    List<UserSocialConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);
    
    UserSocialConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);
    
    List<UserSocialConnection> findByProviderIdAndProviderUserIdIn(String providerId, Collection<String> providerUserIds);
}
