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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
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
@ContextConfiguration(classes = { com.jiwhiz.blog.RepositoryTestConfig.class })
public class BlogPostRepositoryTest {
    @Inject
    UserAccountRepository accountRepository;
    @Inject
    BlogPostRepository blogPostRepository;
    @Inject
    CommentPostRepository commentPostRepository;

    UserAccount testAccount;
    String username = "jsmith";
    String firstTitle = "Test Blog";
    String secondTitle = "Test Blog Again";

    @Before
    public void setUp() {
        // create a test account
        testAccount = new UserAccount();
        testAccount.setUserId(username);
        testAccount.setRoles(new UserRoleType[] { UserRoleType.ROLE_AUTHOR });
        testAccount = accountRepository.save(testAccount);
    }

    @After
    public void shutdown() {
        blogPostRepository.deleteAll();
        accountRepository.delete(testAccount);
    }

    // -------------------------------------------------------------------------
    @Test
    public void testBlogCRUD() {
        // create blog
        BlogPost blogPost = new BlogPost("post1", testAccount, firstTitle, "This is a test blog content.", "MongoDB, Spring Data");
        blogPostRepository.save(blogPost);
        String key = blogPost.getKey();
        assertTrue(blogPostRepository.exists(key));

        // read
        BlogPost blogInDB = blogPostRepository.findOne(key);
        assertEquals(blogInDB.getTitle(), firstTitle);

        // update
        blogPost.updateMeta(secondTitle, "Test_Blog", "MongoDB, Spring Data");
        blogPostRepository.save(blogPost);
        blogInDB = blogPostRepository.findOne(key);
        assertEquals(blogInDB.getTitle(), secondTitle);

        // delete
        blogPostRepository.delete(blogPost);
        blogInDB = blogPostRepository.findOne(key);
        assertNull(blogInDB);
        assertFalse(blogPostRepository.exists(key));
    }

    @Test
    public void testFindFirstBlog() {
        // create 10 blogs
        int i = 0;
        while (i < 10) {
            i++;
            BlogPost blogPost = new BlogPost("post"+i, testAccount, "Blog Number " + i, "This is a test blog number " + i,
                    "MongoDB, Spring Data");
            blogPostRepository.save(blogPost);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                // ignore
            }
        }

        // get first blog
        List<BlogPost> blogIter = blogPostRepository.findAll(new Sort("createdTime"));
        BlogPost first = blogIter.get(0);
        assertEquals("Blog Number 1", first.getTitle());

    }

    @Test
    public void testFindByAuthorId() {
        // create blog
        BlogPost blogPost = new BlogPost("post1", testAccount, firstTitle, "This is a test blog content.", "MongoDB, Spring Data");
        blogPostRepository.save(blogPost);
        String key = blogPost.getKey();
        assertTrue(blogPostRepository.exists(key));

        // test query
//        Page<BlogPost> result = blogPostRepository.findByAuthorKey(testAccount.getKey(), new PageRequest(0, 10));
//        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindByPublishedPath() {
        // create blog
        BlogPost blogPost = new BlogPost("post1", testAccount, firstTitle, "This is a test blog content.", "MongoDB, Spring Data");
        blogPost.publish("this_is_a_test", 0, 0);
        blogPostRepository.save(blogPost);
        String key = blogPost.getKey();
        assertTrue(blogPostRepository.exists(key));

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        assertEquals(year, blogPost.getPublishedYear());
        assertEquals(month, blogPost.getPublishedMonth());
        assertEquals(year + "/" + month + "/this_is_a_test", blogPost.getFullPublishedPath());

        // test query
        BlogPost result = blogPostRepository.findByPublishedYearAndPublishedMonthAndPublishedPath(year, month,
                "this_is_a_test");
        assertNotNull(result);
        assertEquals(blogPost, result);

    }
    
    @Test
    public void testFindTitleRegIgnoreCase() {
        // create 10 blogs
        int i = 0;
        while (i < 10) {
            i++;
            BlogPost blogPost = new BlogPost("post"+i, testAccount, "Blog Number " + i, "This is a test blog number " + i,
                    "MongoDB, Spring Data");
            blogPostRepository.save(blogPost);
        }

//        List<BlogPost> blogList = blogPostRepository.findByTitleRegexIgnoreCase("blog");  //ignore case does not work now
//        assertEquals(0, blogList.size()); //should return 10 blog posts. wait for next release of Spring MongoDB

    }

//    @Test
//    public void testFindAllPublishedPostWithoutContent() throws Exception {
//        int i = 0;
//        while (i < 10) {
//            i++;
//            BlogPost blogPost = new BlogPost("post"+i, testAccount, "Blog Number " + i, "This is a test blog number #" + i,
//                    "MongoDB, SpringData");
//            if (i % 2 == 0){
//                blogPost.setPublished(true);
//            }
//            blogPostRepository.save(blogPost);
//        }
//        
//        List<BlogPost> blogList = blogPostRepository.findAllPublishedPostsWithoutContent();
//        assertEquals(5, blogList.size());
//        for (BlogPost blogPost : blogList){
//            assertNull(blogPost.getContent());
//        }
//
//    }
    
//    @Test
//    public void testFindByPublishedIsTrueAndTagsOrderByPublishedTimeDesc() throws Exception {
//        int i = 0;
//        while (i < 10) {
//            BlogPost blogPost = new BlogPost("post"+i, testAccount, "Blog Number " + i, "This is a test blog number #" + i,
//                    ( (i%3 == 0) ? "MongoDB" :"SpringData"));
//            if (i % 2 == 0){
//                blogPost.setPublished(true);
//            }
//            blogPostRepository.save(blogPost);
//            i++;
//        }
//        
//        Page<BlogPost> blogList = blogPostRepository.findByPublishedIsTrueAndTagsOrderByPublishedTimeDesc("MongoDB", new PageRequest(0, 10));
//        assertEquals(2, blogList.getContent().size());
//        for (BlogPost blogPost : blogList){
//            boolean found = false;
//            for (String tag : blogPost.getTags()){
//                if (tag.equals("MongoDB")){
//                    found = true;
//                    break;
//                }
//            }
//            assertTrue(found);
//        }
//
//    }
}
