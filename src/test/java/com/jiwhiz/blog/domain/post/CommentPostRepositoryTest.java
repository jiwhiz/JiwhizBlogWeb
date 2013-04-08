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
package com.jiwhiz.blog.domain.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserRoleType;

/**
 * 
 * @author Yuan Ji
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.jiwhiz.blog.domain.TestConfig.class })
public class CommentPostRepositoryTest {
    @Inject
    UserAccountRepository accountRepository;
    @Inject
    BlogPostRepository blogPostRepository;
    @Inject
    CommentPostRepository commentPostRepository;

    UserAccount testAccount;
    BlogPost testBlogPost;
    String username = "jsmith";
    String firstTitle = "Test Blog";
    String secondTitle = "Test Blog Again";

    String testContent = "Test comment...";
    String testContentUpdate = "Test comment after update...";

    @Before
    public void setUp() {
        // create a test account
        testAccount = new UserAccount();
        testAccount.setUserId(username);
        testAccount.setRoles(new UserRoleType[] { UserRoleType.ROLE_USER });
        accountRepository.save(testAccount);
        
        //create a test blog post
        testBlogPost = new BlogPost(username, "Title", "This is a test blog content.", "MongoDB, Spring Data");
        blogPostRepository.save(testBlogPost);
    }

    @After
    public void shutdown() {
        commentPostRepository.deleteAll();
        blogPostRepository.deleteAll();
        accountRepository.delete(testAccount);
    }

    // -------------------------------------------------------------------------
    @Test
    public void testCommentCRUD() {
        
        // create comment
        CommentPost comment = new CommentPost(testBlogPost.getId(), testAccount.getId(), testContent);
        comment = commentPostRepository.save(comment);
        String id = comment.getId();
        assertTrue(commentPostRepository.exists(id));

        // read
        CommentPost commentInDB = commentPostRepository.findOne(id);
        assertEquals(commentInDB.getContent(), testContent);

        // update
        comment.setContent(testContentUpdate);
        comment = commentPostRepository.save(comment);
        commentInDB = commentPostRepository.findOne(id);
        assertEquals(commentInDB.getContent(), testContentUpdate);

        // delete
        commentPostRepository.delete(comment);
        commentInDB = commentPostRepository.findOne(id);
        assertNull(commentInDB);
        assertFalse(commentPostRepository.exists(id));
    }

    @Test
    public void testFindByAuthorId() {
        // create comment
        CommentPost comment = new CommentPost(testBlogPost.getId(), testAccount.getUserId(), testContent);
        comment = commentPostRepository.save(comment);
        String id = comment.getId();
        assertTrue(commentPostRepository.exists(id));

        // test query
        Page<CommentPost> result = commentPostRepository.findByAuthorId(testAccount.getUserId(), new PageRequest(0, 10));
        assertEquals(1, result.getContent().size());
    }

}
