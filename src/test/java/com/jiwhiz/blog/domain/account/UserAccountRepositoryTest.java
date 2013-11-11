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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Yuan Ji
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.jiwhiz.blog.TestConfig.class })
public class UserAccountRepositoryTest {
    private String userId1 = "jsmith";

    @Inject
    UserAccountRepository accountRepository;

    @Before()
    public void setUp() {

    }

    @After
    public void shutdown() {
        this.accountRepository.deleteAll();
    }

    // -------------------------------------------------------------------------

    @Test
    public void testAccountCRUD() {
        // create account
        UserAccount account = new UserAccount();
        account.setUserId(userId1);
        account.setRoles(new UserRoleType[] { UserRoleType.ROLE_ADMIN, UserRoleType.ROLE_AUTHOR });
        account.setDisplayName("John");
        accountRepository.save(account);
        String key = account.getKey();
        assertTrue(accountRepository.exists(key));

        // read
        UserAccount accountInDb = accountRepository.findOne(key);
        assertEquals(accountInDb.getUsername(), userId1);
        assertEquals("John", accountInDb.getDisplayName());

        // update
        String newWebSite = "www.hello.com";
        account.setWebSite(newWebSite);
        accountRepository.save(account);
        accountInDb = accountRepository.findOne(key);
        assertEquals(newWebSite, accountInDb.getWebSite());

        // delete
        accountRepository.delete(account);
        accountInDb = accountRepository.findOne(key);
        assertNull(accountInDb);
        assertFalse(accountRepository.exists(key));
    }
    
    @Test
    public void testFindById() {
        UserAccount account = new UserAccount();
        account.setUserId(userId1);
        account.setRoles(new UserRoleType[] { UserRoleType.ROLE_ADMIN, UserRoleType.ROLE_AUTHOR });
        account.setDisplayName("John");
        accountRepository.save(account);

        UserAccount accountInDb = accountRepository.findByUserId(userId1);
        assertEquals(accountInDb.getUsername(), userId1);
        assertEquals("John", accountInDb.getDisplayName());
    }
}
