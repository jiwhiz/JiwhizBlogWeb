package com.jiwhiz.blog.domain.account;
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
