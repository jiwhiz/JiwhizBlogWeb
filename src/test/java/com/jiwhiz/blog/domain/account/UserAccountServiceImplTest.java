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
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.social.connect.ConnectionData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiwhiz.blog.domain.account.impl.UserAccountServiceImpl;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * 
 * @author Yuan Ji
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.jiwhiz.blog.TestConfig.class })
public class UserAccountServiceImplTest {
    static final String USERID = "jsmith";
    
    UserAccountRepository mockRepository;
    CounterService mockCounterService;
    UserAccountServiceImpl service;

    @Before
    public void setup() {
        mockRepository = mock(UserAccountRepository.class);
        mockCounterService = mock(CounterService.class);
        service = new UserAccountServiceImpl(mockRepository, mockCounterService);
    }

    // -------------------------------------------------------------------------

    @Test
    public void testCreateFirstUserAccount() throws Exception {
        when(mockCounterService.getNextUserIdSequence()).thenReturn(1l);
        when(mockRepository.save(any(UserAccount.class))).then(returnsFirstArg());
        
        ConnectionData data = new ConnectionData("providerId", "providerUserId", "John", "url", "url",  null, null, null, null);

        UserAccount account = service.createUserAccount(data);
        
        verify(mockCounterService).getNextUserIdSequence();
        verify(mockRepository).save(account);
        assertTrue(account.isAuthor());
        assertTrue(account.isAdmin());
        assertEquals("user1", account.getUserId());
        assertEquals("John", account.getDisplayName());
        assertEquals("url", account.getImageUrl());
    }
    
    @Test
    public void testCreateNonFirstUserAccount() throws Exception {
        when(mockCounterService.getNextUserIdSequence()).thenReturn(2l);
        when(mockRepository.save(any(UserAccount.class))).then(returnsFirstArg());
        
        ConnectionData data = new ConnectionData("providerId", "providerUserId", "Peter", "url", "url",  null, null, null, null);
        UserAccount account = service.createUserAccount(data);
        
        verify(mockCounterService).getNextUserIdSequence();
        verify(mockRepository).save(account);
        assertFalse(account.isAuthor());
        assertFalse(account.isAdmin());
        assertEquals("user2", account.getUserId());
        assertEquals("Peter", account.getDisplayName());
        assertEquals("url", account.getImageUrl());
    }

    @Test
    public void testAddAuthorRole() throws Exception {
        UserAccount account = new UserAccount();
        
        when(mockRepository.findByUserId(USERID)).thenReturn(account);
        
    }

}
