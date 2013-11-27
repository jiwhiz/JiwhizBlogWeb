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
package com.jiwhiz.blog.domain.account.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionData;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserRoleType;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * 
 * @author Yuan Ji
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {
    static final String USERID = "jsmith";
    
    @Mock
    UserAccountRepository userAccountRepositoryMock;
    
    @Mock
    CounterService counterServiceMock;
    
    @Mock
    UserIdSource userIdSourceMock;
    
    UserAccountServiceImpl service;

    @Before
    public void setup() {
        service = new UserAccountServiceImpl(userAccountRepositoryMock, counterServiceMock, userIdSourceMock);
    }

    // -------------------------------------------------------------------------

    @Test
    public void testCreateFirstUserAccount() throws Exception {
        when(counterServiceMock.getNextUserIdSequence()).thenReturn(1l);
        when(userAccountRepositoryMock.save(any(UserAccount.class))).then(returnsFirstArg());
        
        ConnectionData data = new ConnectionData("providerId", "providerUserId", "John", "url", "url",  null, null, null, null);

        UserAccount account = service.createUserAccount(data);
        
        verify(counterServiceMock).getNextUserIdSequence();
        verify(userAccountRepositoryMock).save(account);
        assertTrue(account.isAuthor());
        assertTrue(account.isAdmin());
        assertEquals("user1", account.getUserId());
        assertEquals("John", account.getDisplayName());
        assertEquals("url", account.getImageUrl());
    }
    
    @Test
    public void testCreateNonFirstUserAccount() throws Exception {
        when(counterServiceMock.getNextUserIdSequence()).thenReturn(2l);
        when(userAccountRepositoryMock.save(any(UserAccount.class))).then(returnsFirstArg());
        
        ConnectionData data = new ConnectionData("providerId", "providerUserId", "Peter", "url", "url",  null, null, null, null);
        UserAccount account = service.createUserAccount(data);
        
        verify(counterServiceMock).getNextUserIdSequence();
        verify(userAccountRepositoryMock).save(account);
        assertFalse(account.isAuthor());
        assertFalse(account.isAdmin());
        assertEquals("user2", account.getUserId());
        assertEquals("Peter", account.getDisplayName());
        assertEquals("url", account.getImageUrl());
    }

    @Test
    public void testAddAuthorRole() throws Exception {
        UserAccount testAccount = new UserAccount(USERID, new UserRoleType[]{UserRoleType.ROLE_USER});
        assertFalse(testAccount.isAuthor());
        when(userAccountRepositoryMock.findByUserId(USERID)).thenReturn(testAccount);
        when(userAccountRepositoryMock.save(any(UserAccount.class))).then(returnsFirstArg());
        
        UserAccount returnAccount = service.setAuthor(USERID, true);
        
        assertEquals(USERID, returnAccount.getUserId());
        assertTrue(returnAccount.isAuthor());
    }

    @Test
    public void testRemoveAuthorRole() throws Exception {
        UserAccount testAccount = new UserAccount(USERID, new UserRoleType[]{UserRoleType.ROLE_USER, UserRoleType.ROLE_AUTHOR});
        assertTrue(testAccount.isAuthor());
        when(userAccountRepositoryMock.findByUserId(USERID)).thenReturn(testAccount);
        when(userAccountRepositoryMock.save(any(UserAccount.class))).then(returnsFirstArg());
        
        UserAccount returnAccount = service.setAuthor(USERID, false);
        
        assertEquals(USERID, returnAccount.getUserId());
        assertFalse(returnAccount.isAuthor());
    }
}
