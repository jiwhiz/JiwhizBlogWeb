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
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for UserAccount domain entity class.
 * 
 * @author Yuan Ji
 * 
 */
public class UserAccountTest {
    @Test
    public void testIsAuthor() {
        UserAccount normalUser = new UserAccount();
        assertFalse(normalUser.isAuthor());
        
        UserRoleType[] roles = new UserRoleType[]{UserRoleType.ROLE_USER, UserRoleType.ROLE_AUTHOR};
        UserAccount author = new UserAccount("1234", roles);
        assertTrue(author.isAuthor());
    }
    
    @Test
    public void testIsAdmin() {
        UserAccount normalUser = new UserAccount();
        assertFalse(normalUser.isAdmin());
        
        UserRoleType[] roles = new UserRoleType[]{UserRoleType.ROLE_USER, UserRoleType.ROLE_ADMIN};
        UserAccount author = new UserAccount("1234", roles);
        assertTrue(author.isAdmin());
    }
}
