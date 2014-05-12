/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
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
package com.jiwhiz.rest.user;

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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.jiwhiz.config.TestUtils;
import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class UserCommentRestControllerTest extends AbstractRestControllerTest {

    @Inject
    CommentPostRepository commentPostRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(commentPostRepositoryMock);
        super.setup();
    }

    @Test
    public void getCurrentUserComments_ShouldReturnAllCommentsByUser() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();

        Page<CommentPost> page = new PageImpl<CommentPost>(getTestUserCommentPostList(), new PageRequest(0, 10), 2);
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        when(commentPostRepositoryMock.findByAuthorIdOrderByCreatedTimeDesc(eq(USER_ID), any(Pageable.class)))
            .thenReturn(page);
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.commentPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].id", is (COMMENTS_1_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].authorId", is (USER_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].blogPostId", is (BLOGS_1_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].id", is (COMMENTS_2_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].authorId", is (USER_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].blogPostId", is (BLOGS_2_ID)))
                ;
    }

    @Test
    public void getCommentPostById_ShouldReturnCommentPost() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        CommentPost comment = getTestApprovedCommentPost();
        comment.setAuthorId(user.getUserId());
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS_COMMENT, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (COMMENT_ID)))
                .andExpect(jsonPath("$.blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is (USER_ID)))
                .andExpect(jsonPath("$.content", is (COMMENT_CONTENT)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_USER_COMMENTS+"/"+COMMENT_ID)))
                .andExpect(jsonPath("$._links.user.href", endsWith(ApiUrls.URL_USER)))
                .andExpect(jsonPath("$._links.blog.href", endsWith(ApiUrls.URL_SITE_BLOGS+"/"+BLOG_ID)))
                ;
    }

    @Test
    public void getCommentPostById_ShouldReturn403IfAuthorIsNotCurrentUser() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        CommentPost comment = getTestApprovedCommentPost();
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS_COMMENT, COMMENT_ID))
                .andExpect(status().isForbidden())
                ;
    }

    @Test
    public void updateComment_ShouldReturnHttpStatusCode204() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        CommentPost comment = getTestApprovedCommentPost();
        comment.setAuthorId(user.getUserId());
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        when(commentPostRepositoryMock.findOne(eq(COMMENT_ID))).thenReturn(comment);
        
        Map<String, String> testUpdates = new HashMap<String, String>();
        testUpdates.put("content", "Updated blog text...");
        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS_COMMENT, COMMENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(testUpdates))
                )
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                ;
    } 
    
    @Test
    public void updateComment_ShouldReturn403IfCurrentUserIsNotAuthorOfComment() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        CommentPost comment = getTestApprovedCommentPost();
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        when(commentPostRepositoryMock.findOne(eq("comment1"))).thenReturn(comment);
        
        Map<String, String> testUpdates = new HashMap<String, String>();
        testUpdates.put("content", "Updated blog text...");

        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS_COMMENT, "comment1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(testUpdates))
                )
                .andExpect(status().isForbidden())
                .andExpect(content().string(""))
                ;
    } 

    @Test
    public void updateComment_ShouldReturn401IfUserNotLoggedIn() throws Exception {
        when(userAccountServiceMock.getCurrentUser()).thenReturn(null);
        
        CommentPost comment = new CommentPost("user2", "blog1", "This is a test");
        when(commentPostRepositoryMock.findOne(eq("comment1"))).thenReturn(comment);
        
        Map<String, String> testUpdates = new HashMap<String, String>();
        testUpdates.put("content", "Updated blog text...");

        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_USER_COMMENTS_COMMENT, "comment1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(testUpdates))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""))
                ;
    } 

}
