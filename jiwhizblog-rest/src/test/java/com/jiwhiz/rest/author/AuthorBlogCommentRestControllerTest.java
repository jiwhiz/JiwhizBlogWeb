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
package com.jiwhiz.rest.author;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.jiwhiz.config.TestUtils;
import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class AuthorBlogCommentRestControllerTest extends AbstractRestControllerTest {
    @Inject
    UserAccountRepository userAccountRepositoryMock;
    
    @Inject
    BlogPostRepository blogPostRepositoryMock;
    
    @Inject
    CommentPostRepository commentPostRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(userAccountRepositoryMock);
        Mockito.reset(blogPostRepositoryMock);
        Mockito.reset(commentPostRepositoryMock);
        super.setup();
    }
    
    @Test
    public void getCommentPostsByBlogPostId_ShouldReturnAllCommentPostsForBlog() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        BlogPost blog = new BlogPost(user.getUserId(), "My Test Blog", "Hello!", "TEST");
        blog.setId(BLOG_ID);
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        
        CommentPost comment1 = new CommentPost("author1", BLOG_ID, "My question comment...");
        CommentPost comment2 = new CommentPost("author2", BLOG_ID, "My answer comment...");
        when(commentPostRepositoryMock.findByBlogPostIdOrderByCreatedTimeDesc(eq(BLOG_ID), any(Pageable.class)))
            .thenReturn(new PageImpl<CommentPost>(Arrays.asList(comment1, comment2), new PageRequest(0, 10), 1));
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG_COMMENTS, BLOG_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.commentPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].authorId", is ("author1")))
                .andExpect(jsonPath("$._embedded.commentPostList[0].content", is ("My question comment...")))
                .andExpect(jsonPath("$._embedded.commentPostList[1].blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].authorId", is ("author2")))
                .andExpect(jsonPath("$._embedded.commentPostList[1].content", is ("My answer comment...")))
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith(BLOG_ID+"/comments{?page,size,sort}")))
                ;
    }

    @Test
    public void getCommentPostById_ShouldReturnCommentPost() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        final String BLOG_ID = "blog12345";
        BlogPost blog = new BlogPost(user.getUserId(), "My Test Blog", "Hello!", "TEST");
        blog.setId(BLOG_ID);
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        
        final String COMMENT_ID = "comment123";
        CommentPost comment = new CommentPost("userABC", BLOG_ID, "My question comment...");
        comment.setId(COMMENT_ID);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (COMMENT_ID)))
                .andExpect(jsonPath("$.blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is ("userABC")))
                .andExpect(jsonPath("$.content", is ("My question comment...")))
                .andExpect(jsonPath("$._links.self.href", endsWith(BLOG_ID+"/comments/"+COMMENT_ID)))
                .andExpect(jsonPath("$._links.blog.href", endsWith(BLOG_ID)))
                .andExpect(jsonPath("$._links.author.href", endsWith("userABC")))
                ;
    }

    @Test
    public void updateCommentPost_ShouldUpdateCommentPostAndReturn204() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        final String BLOG_ID = "123";
        BlogPost blogPost = new BlogPost(user.getUserId(), "Test Blog", "Hello", "TEST, Hello");
        blogPost.setId(BLOG_ID);
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blogPost);
        

        final String COMMENT_ID = "comment123";
        CommentPost comment = new CommentPost("userABC", BLOG_ID, "My question comment...");
        comment.setId(COMMENT_ID);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        when(commentPostRepositoryMock.save(comment)).thenReturn(comment);
        
        Map<String, String> testUpdates = new HashMap<String, String>();
        testUpdates.put("content", "Updated blog text...");
        
        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(testUpdates))
                )
                .andExpect(status().isNoContent());
    }

}
