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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.MediaTypes;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class AuthorAccountRestControllerTest extends AbstractRestControllerTest {
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
    public void getCurrentAuthor_ShouldReturnCurrentUserAndLinks() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.displayName", is (USER_DISPLAY_NAME)))
                .andExpect(jsonPath("$.admin", is(false)))
                .andExpect(jsonPath("$.author", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_AUTHOR)))
                .andExpect(jsonPath("$._links.blogs.templated", is(true)))
                .andExpect(jsonPath("$._links.blogs.href", endsWith(ApiUrls.URL_AUTHOR_BLOGS+"{?page,size,sort}")))
                .andExpect(jsonPath("$._links.blog.templated", is(true)))
                .andExpect(jsonPath("$._links.blog.href", endsWith(ApiUrls.URL_AUTHOR_BLOGS_BLOG)))
                ;
    } 

    @Test
    public void getCurrentAuthor_ShouldReturn403IfLoggedInUserIsNotAuthor() throws Exception {
        UserAccount user = getTestLoggedInUser();
        
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR))
                .andExpect(status().isForbidden())
                ;
    } 

}
