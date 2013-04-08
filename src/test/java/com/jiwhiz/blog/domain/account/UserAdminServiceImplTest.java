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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.social.connect.ConnectionData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiwhiz.blog.domain.system.CounterService;

/**
 * 
 * @author Yuan Ji
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.jiwhiz.blog.domain.TestConfig.class })
public class UserAdminServiceImplTest {
    static final String USERID = "jsmith";
    
    UserAccountRepository mockRepository;
    CounterService mockCounterService;
    UserAdminServiceImpl service;

    @Before
    public void setup() {
        mockRepository = mock(UserAccountRepository.class);
        mockCounterService = mock(CounterService.class);
        service = new UserAdminServiceImpl(mockRepository, mockCounterService);
    }

    // -------------------------------------------------------------------------

    @Test
    public void testCreateAuthorAccount() throws Exception {
        
        when(mockCounterService.getNextUserIdSequence()).thenReturn(1l);
        
        ConnectionData data = new ConnectionData("providerId", "providerUserId", "John", "url", "url",  null, null, null, null);

        UserAccount account = service.createUserAccount(data);
        verify(mockCounterService).getNextUserIdSequence();
        verify(mockRepository).save(account);
        assertFalse(account.isAuthor());
        assertFalse(account.isAdmin());
        assertEquals("user1", account.getUserId());
    }
    
    @Test
    public void testAddAuthorRole() throws Exception {
        UserAccount account = new UserAccount();
        
        when(mockRepository.findByUserId(USERID)).thenReturn(account);
        
    }

}
