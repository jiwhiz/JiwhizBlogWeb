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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;

import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class BlogRestControllerTest extends AbstractRestControllerTest {
    @Inject
    BlogPostRepository blogPostRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(blogPostRepositoryMock);
        super.setup();
    }

    @Test
    public void getBlogPosts_ShouldReturnAllBlogPostsInSystem() throws Exception {        
        Page<BlogPost> page = new PageImpl<BlogPost>(getTestPublishedBlogPostList(), new PageRequest(0, 10), 2);
        
        when(blogPostRepositoryMock.findAll(any(Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_BLOGS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.blogPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].id", is (BLOGS_1_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].authorId", is (BLOGS_1_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].title", is (BLOGS_1_TITLE)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].id", is (BLOGS_2_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].authorId", is (BLOGS_2_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].title", is (BLOGS_2_TITLE)))
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_BLOGS+"{?page,size,sort}")))
                ;
    }

    @Test
    public void getBlogPostById_ShouldReturnBlogPost() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        blog.setId(BLOG_ID);
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_BLOGS_BLOG, BLOG_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is (BLOG_AUTHOR_ID)))
                .andExpect(jsonPath("$.title", is (BLOG_TITLE)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_BLOGS+"/"+BLOG_ID)))
                ;
    }

}
