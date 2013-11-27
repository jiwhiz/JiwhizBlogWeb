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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiwhiz.blog.RepositoryTestConfig;

/**
 *
 * @author Yuan Ji
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class })
public class UserSocialConnectionRepositoryTest {

    @Inject
    UserSocialConnectionRepository userSocialConnectionRepository;

    @Before()
    public void setUp() {

    }

    @After
    public void shutdown() {
        this.userSocialConnectionRepository.deleteAll();
    }

    private void setupUserSocialConnectionList(){
        UserSocialConnection userSocialConnection = new UserSocialConnection
                ("jiwhiz", "google", "google-jiwhiz", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);
        userSocialConnection = new UserSocialConnection
                ("jiwhiz2", "google", "google-jiwhiz2", 2, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);
        
        userSocialConnection = new UserSocialConnection
                ("jiwhiz", "facebook", "facebook-jiwhiz", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);
        userSocialConnection = new UserSocialConnection
                ("jiwhiz2", "facebook", "facebook-jiwhiz2", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);

        userSocialConnection = new UserSocialConnection
                ("jiwhiz", "twitter", "twitter-jiwhiz", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);
        userSocialConnection = new UserSocialConnection
                ("jiwhiz2", "twitter", "twitter-jiwhiz2", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);

        userSocialConnection = new UserSocialConnection
                ("other", "facebook", "facebook-other", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);
        userSocialConnection = new UserSocialConnection
                ("other", "twitter", "twitter-other", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);
        userSocialConnectionRepository.save(userSocialConnection);

    }
    
    // -------------------------------------------------------------------------

    @Test
    public void testUserSocialConnectionCRUD() {
        // create user connection
        UserSocialConnection userSocialConnection = new UserSocialConnection
                ("jiwhiz", "google", "google-jiwhz", 1, "Yuan", "http://", "http://", "token", "secret", "token", 1000000l);

        userSocialConnectionRepository.save(userSocialConnection);
        String key = userSocialConnection.getKey();
        assertTrue(userSocialConnectionRepository.exists(key));

        // read
        UserSocialConnection userSocialConnectionInDb = userSocialConnectionRepository.findOne(key);
        assertEquals("jiwhiz", userSocialConnectionInDb.getUserId());

        // update
        String newProfileUrl = "www.hello.com";
        userSocialConnection.setProfileUrl(newProfileUrl);
        userSocialConnectionRepository.save(userSocialConnection);
        userSocialConnectionInDb = userSocialConnectionRepository.findOne(key);
        assertEquals(newProfileUrl, userSocialConnectionInDb.getProfileUrl());

        // delete
        userSocialConnectionRepository.delete(userSocialConnection);
        userSocialConnectionInDb = userSocialConnectionRepository.findOne(key);
        assertNull(userSocialConnectionInDb);
        assertFalse(userSocialConnectionRepository.exists(key));
    }

    @Test
    public void testFindByUsername(){
        setupUserSocialConnectionList();
        
        List<UserSocialConnection> result = userSocialConnectionRepository.findByUserId("jiwhiz");
        assertEquals(3, result.size());

        result = userSocialConnectionRepository.findByUserId("other");
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByUsernameAndProviderId(){
        setupUserSocialConnectionList();
        
        List<UserSocialConnection> result = userSocialConnectionRepository.findByUserIdAndProviderId("jiwhiz", "google");
        assertEquals(1, result.size());

        result = userSocialConnectionRepository.findByUserIdAndProviderId("other", "google");
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByUsernameAndProviderIdAndProviderUserId(){
        setupUserSocialConnectionList();
        
        UserSocialConnection result = userSocialConnectionRepository.findByUserIdAndProviderIdAndProviderUserId("jiwhiz", "google", "google-jiwhiz");
        assertNotNull(result);

        result = userSocialConnectionRepository.findByUserIdAndProviderIdAndProviderUserId("jiwhiz", "google", "google-jiwhiz2");
        assertNull(result);
    }

    @Test
    public void testFindByProviderIdAndProviderUserIds(){
        setupUserSocialConnectionList();
        
        Set<String> providerUserIds = new HashSet<String>();
        providerUserIds.add("google-jiwhiz");
        providerUserIds.add("google-jiwhiz2");
        
        List<UserSocialConnection> result = userSocialConnectionRepository.findByProviderIdAndProviderUserIdIn("google", providerUserIds);
        assertEquals(2, result.size());

    }

}
