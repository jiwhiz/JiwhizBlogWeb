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
package com.jiwhiz.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jiwhiz.config.TestRestServiceConfig;
import com.jiwhiz.config.TestRestServiceWebConfig;
import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.account.UserRoleType;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentStatusType;

/**
 * @author Yuan Ji
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestRestServiceConfig.class, TestRestServiceWebConfig.class })
@WebAppConfiguration
public abstract class AbstractRestControllerTest {
    protected MockMvc mockMvc;
    
    @Inject
    protected WebApplicationContext webApplicationContext;

    @Inject
    protected UserAccountService userAccountServiceMock;

    @Before
    public void setup() {
        Mockito.reset(userAccountServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected static final String USER_ID = "user123";
    protected static final String USER_USERNAME = "john.doe@company.com";
    protected static final String USER_DISPLAY_NAME = "John Doe";
    
    protected UserAccount getTestLoggedInUser() {
        UserAccount user = new UserAccount();
        user.setUserId(USER_ID);
        user.setRoles(new UserRoleType[]{UserRoleType.ROLE_USER});
        user.setDisplayName(USER_DISPLAY_NAME);
        return user;
    }

    protected UserAccount getTestLoggedInUserWithAdminRole() {
        UserAccount user = new UserAccount();
        user.setUserId(USER_ID);
        user.setRoles(new UserRoleType[]{UserRoleType.ROLE_ADMIN, UserRoleType.ROLE_USER});
        user.setDisplayName(USER_DISPLAY_NAME);
        return user;
    }

    protected UserAccount getTestLoggedInUserWithAuthorRole() {
        UserAccount user = new UserAccount();
        user.setUserId(USER_ID);
        user.setRoles(new UserRoleType[]{UserRoleType.ROLE_AUTHOR, UserRoleType.ROLE_USER});
        user.setDisplayName(USER_DISPLAY_NAME);
        return user;
    }

    protected static final String USERS_1_USER_ID = "user001";
    protected static final String USERS_1_DISPLAY_NAME = "John Doe";
    protected static final String USERS_2_USER_ID = "user002";
    protected static final String USERS2_TITLE = "Test Blog Two";
    protected static final String USERS_2_DISPLAY_NAME = "Jane Doe";
    
    protected List<UserAccount> getTestUserAccountList() {
        UserAccount user1 = new UserAccount();
        user1.setUserId(USERS_1_USER_ID);
        user1.setRoles(new UserRoleType[]{UserRoleType.ROLE_ADMIN, UserRoleType.ROLE_USER});
        user1.setDisplayName(USERS_1_DISPLAY_NAME);

        UserAccount user2 = new UserAccount();
        user2.setUserId(USERS_2_USER_ID);
        user2.setRoles(new UserRoleType[]{UserRoleType.ROLE_AUTHOR, UserRoleType.ROLE_USER});
        user2.setDisplayName(USERS_2_DISPLAY_NAME);
        
        return Arrays.asList(user1, user2);
    }

    protected static final String BLOG_ID = "blog123";
    protected static final String BLOG_TITLE = "My First Post";
    protected static final String BLOG_AUTHOR_ID = "author123";
    
    protected BlogPost getTestSinglePublishedBlogPost() {
        BlogPost blog = new BlogPost();
        blog.setId(BLOG_ID);
        blog.setAuthorId(BLOG_AUTHOR_ID);
        blog.setTitle(BLOG_TITLE);
        blog.setPublished(true);
        return blog;
    }
    
    protected static final String BLOGS_1_ID = "blog001";
    protected static final String BLOGS_1_TITLE = "Test Blog One";
    protected static final String BLOGS_1_AUTHOR_ID = "author123";
    protected static final String BLOGS_2_ID = "blog002";
    protected static final String BLOGS_2_TITLE = "Test Blog Two";
    protected static final String BLOGS_2_AUTHOR_ID = "author456";
    
    protected List<BlogPost> getTestPublishedBlogPostList() {
        BlogPost blog1 = new BlogPost();
        blog1.setId(BLOGS_1_ID);
        blog1.setTitle(BLOGS_1_TITLE);
        blog1.setAuthorId(BLOGS_1_AUTHOR_ID);
        blog1.setPublished(true);
        
        BlogPost blog2 = new BlogPost();
        blog2.setId(BLOGS_2_ID);
        blog2.setTitle(BLOGS_2_TITLE);
        blog2.setAuthorId(BLOGS_2_AUTHOR_ID);
        blog2.setPublished(true);
        
        return Arrays.asList(blog1, blog2);
    }

    protected static final String COMMENT_ID = "comment001";
    protected static final String COMMENT_CONTENT = "Test comment...";
    protected static final String COMMENT_AUTHOR_ID = "author123";

    protected CommentPost getTestApprovedCommentPost() {
        CommentPost comment = new CommentPost();
        comment.setId(COMMENT_ID);
        comment.setAuthorId(COMMENT_AUTHOR_ID);
        comment.setBlogPostId(BLOG_ID);
        comment.setContent(COMMENT_CONTENT);
        comment.setStatus(CommentStatusType.APPROVED);
        return comment;
    }
    
    protected static final String COMMENTS_1_ID = "comment001";
    protected static final String COMMENTS_1_CONTENT = "My comment...";
    protected static final String COMMENTS_1_AUTHOR_ID = "author123";
    protected static final String COMMENTS_2_ID = "comment002";
    protected static final String COMMENTS_2_CONTENT = "Another comment...";
    protected static final String COMMENTS_2_AUTHOR_ID = "author456";
    
    protected List<CommentPost> getTestApprovedCommentPostList() {
        CommentPost comment1 = new CommentPost();
        comment1.setId(COMMENTS_1_ID);
        comment1.setAuthorId(COMMENTS_1_AUTHOR_ID);
        comment1.setBlogPostId(BLOG_ID);
        comment1.setContent(COMMENTS_1_CONTENT);
        comment1.setStatus(CommentStatusType.APPROVED);
        
        CommentPost comment2 = new CommentPost();
        comment2.setId(COMMENTS_2_ID);
        comment2.setAuthorId(COMMENTS_2_AUTHOR_ID);
        comment2.setBlogPostId(BLOG_ID);
        comment2.setContent(COMMENTS_2_CONTENT);
        comment2.setStatus(CommentStatusType.APPROVED);
        
        return Arrays.asList(comment1, comment2);
    }

    protected List<CommentPost> getTestUserCommentPostList() {
        CommentPost comment1 = new CommentPost();
        comment1.setId(COMMENTS_1_ID);
        comment1.setAuthorId(USER_ID);
        comment1.setBlogPostId(BLOGS_1_ID);
        comment1.setContent(COMMENTS_1_CONTENT);
        comment1.setStatus(CommentStatusType.APPROVED);
        
        CommentPost comment2 = new CommentPost();
        comment2.setId(COMMENTS_2_ID);
        comment2.setAuthorId(USER_ID);
        comment2.setBlogPostId(BLOGS_2_ID);
        comment2.setContent(COMMENTS_2_CONTENT);
        comment2.setStatus(CommentStatusType.PENDING);
        
        return Arrays.asList(comment1, comment2);
    }
}
