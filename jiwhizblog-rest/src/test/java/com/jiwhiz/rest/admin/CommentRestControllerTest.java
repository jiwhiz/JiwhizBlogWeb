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
package com.jiwhiz.rest.admin;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
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
public class CommentRestControllerTest extends AbstractRestControllerTest {
    @Inject
    CommentPostRepository commentPostRepositoryMock;

    @Before
    public void setup() {
        Mockito.reset(commentPostRepositoryMock);
        super.setup();
    }

    @Test
    public void getCommentPosts_ShouldReturnAllCommentPosts() throws Exception {
        Page<CommentPost> page = new PageImpl<CommentPost>(getTestApprovedCommentPostList(), new PageRequest(0, 10), 2);
        
        when(commentPostRepositoryMock.findAll(any(Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_COMMENTS+"?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.commentPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].id", is (COMMENTS_1_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].authorId", is (COMMENTS_1_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].content", is (COMMENTS_1_CONTENT)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].id", is (COMMENTS_2_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].authorId", is (COMMENTS_2_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].content", is (COMMENTS_2_CONTENT)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_COMMENTS+"{?page,size,sort}")))
                ;
    }

    @Test
    public void getCommentPostById_ShouldReturnCommentPost() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        CommentPost comment = getTestApprovedCommentPost();
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_COMMENTS_COMMENT, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (COMMENT_ID)))
                .andExpect(jsonPath("$.blogPostId", is (BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is (COMMENT_AUTHOR_ID)))
                .andExpect(jsonPath("$.content", is (COMMENT_CONTENT)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_COMMENTS+"/"+COMMENT_ID)))
                ;
    }

    @Test
    public void updateCommentPost_ShouldUpdateCommentPostAndReturn204() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        CommentPost comment = getTestApprovedCommentPost();
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        when(commentPostRepositoryMock.save(comment)).thenReturn(comment);
        
        Map<String, String> testUpdates = new HashMap<String, String>();
        testUpdates.put("content", "Updated blog text...");
        
        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_COMMENTS_COMMENT, COMMENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(testUpdates))
                )
                .andExpect(status().isNoContent());
    }

}
