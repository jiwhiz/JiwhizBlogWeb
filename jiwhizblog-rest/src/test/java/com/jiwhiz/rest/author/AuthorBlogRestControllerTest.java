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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import com.jiwhiz.config.TestUtils;
import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class AuthorBlogRestControllerTest extends AbstractRestControllerTest {
    @Inject
    UserAccountRepository userAccountRepositoryMock;
    
    @Inject
    BlogPostRepository blogPostRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(userAccountRepositoryMock);
        Mockito.reset(blogPostRepositoryMock);
        super.setup();
    }

    @Test
    public void getBlogPosts_ShouldReturnAllBlogPostsForCurrentUser() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        
        BlogPost blog1 = new BlogPost();
        blog1.setId(BLOGS_1_ID);
        blog1.setTitle(BLOGS_1_TITLE);
        blog1.setAuthorId(user.getUserId());
        blog1.setPublished(true);
        blog1.setContent("My first article...");
        
        BlogPost blog2 = new BlogPost();
        blog2.setId(BLOGS_2_ID);
        blog2.setTitle(BLOGS_2_TITLE);
        blog2.setAuthorId(user.getUserId());
        blog2.setPublished(true);
        blog2.setContent("My second article...");

        when(blogPostRepositoryMock.findByAuthorIdOrderByCreatedTimeDesc(eq(user.getUserId()), any(Pageable.class)))
            .thenReturn(new PageImpl<BlogPost>(Arrays.asList(blog1, blog2), new PageRequest(0, 10), 1));
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.blogPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].id", is (BLOGS_1_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].authorId", is (user.getUserId())))
                .andExpect(jsonPath("$._embedded.blogPostList[0].title", is (BLOGS_1_TITLE)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].content", is (blog1.getContent())))
                .andExpect(jsonPath("$._embedded.blogPostList[1].id", is (BLOGS_2_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].authorId", is (user.getUserId())))
                .andExpect(jsonPath("$._embedded.blogPostList[1].title", is (BLOGS_2_TITLE)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].content", is (blog2.getContent())))
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_AUTHOR_BLOGS+"{?page,size,sort}")))
                ;
    }

    @Test
    public void getBlogPostById_ShouldReturnBlogPost() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        BlogPost blog = getTestSinglePublishedBlogPost();
        blog.setAuthorId(user.getUserId());
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG, BLOG_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is (user.getUserId())))
                .andExpect(jsonPath("$.title", is (BLOG_TITLE)))
                .andExpect(jsonPath("$._links.self.href", endsWith(BLOG_ID)))
                .andExpect(jsonPath("$._links.comments.templated", is(true)))
                .andExpect(jsonPath("$._links.comments.href", endsWith("/author/blogs/"+BLOG_ID+"/comments{?page,size,sort}")))
                ;
    }

    @Test
    public void createBlogPost_ShouldAddBlogPostAndReturn200() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        BlogPostForm form = new BlogPostForm();
        form.setTitle("Test Blog");
        form.setContent("Hello");
        form.setTagString("TEST, Hello");
        BlogPost blogPost = new BlogPost(user.getUserId(), form.getTitle(), form.getContent(), form.getTagString());
        blogPost.setId(BLOG_ID);
        when(blogPostRepositoryMock.save(any(BlogPost.class))).thenReturn(blogPost);
        
        mockMvc.perform
                (post(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(form))
                )
                .andExpect(status().isCreated())
                .andExpect(
                    header().string("Location", 
                        endsWith(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS + "/" + BLOG_ID)
                    )
                );
        
    }

    @Test
    public void updateBlogPost_ShouldUpdateBlogPostAndReturn204() throws Exception {
        UserAccount user = getTestLoggedInUserWithAuthorRole();
        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);

        BlogPostForm form = new BlogPostForm();
        form.setTitle("Test Blog");
        form.setContent("Hello");
        form.setTagString("TEST, Hello");
        BlogPost blogPost = new BlogPost(user.getUserId(), form.getTitle(), form.getContent(), form.getTagString());
        blogPost.setId(BLOG_ID);
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blogPost);
        when(blogPostRepositoryMock.save(blogPost)).thenReturn(blogPost);
        
        mockMvc.perform
                (put(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS_BLOG, BLOG_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(form))
                )
                .andExpect(status().isNoContent())
                .andExpect(
                    header().string("Location", 
                        endsWith(ApiUrls.API_ROOT + ApiUrls.URL_AUTHOR_BLOGS + "/" + BLOG_ID)
                    )
                );
        
    }

}
