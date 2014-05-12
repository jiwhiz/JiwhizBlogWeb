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
package com.jiwhiz.rest.site;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.post.BlogPost;
import com.jiwhiz.domain.post.BlogPostRepository;
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class WebsiteRestControllerTest extends AbstractRestControllerTest {
    
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
    public void getPublicWebsiteResource_ShouldReturnWebsiteResource() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();

        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.authenticated", is(true)))
                //check links
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE)))
                .andExpect(jsonPath("$._links.blogs.templated", is(true)))
                .andExpect(jsonPath("$._links.blogs.href", endsWith(ApiUrls.URL_SITE_BLOGS+"{?page,size,sort}")))
                .andExpect(jsonPath("$._links.blog.templated", is(true)))
                .andExpect(jsonPath("$._links.blog.href", endsWith(ApiUrls.URL_SITE_BLOGS_BLOG)))
                .andExpect(jsonPath("$._links.profile.templated", is(true)))
                .andExpect(jsonPath("$._links.profile.href", endsWith(ApiUrls.URL_SITE_PROFILES_USER)))
                .andExpect(jsonPath("$._links.currentUser.href", endsWith(ApiUrls.URL_SITE_CURRENT_USER)))
                .andExpect(jsonPath("$._links.latestBlog.href", endsWith(ApiUrls.URL_SITE_LATEST_BLOG)))
                .andExpect(jsonPath("$._links.recentBlogs.href", endsWith(ApiUrls.URL_SITE_RECENT_BLOGS)))
                .andExpect(jsonPath("$._links.recentComments.href", endsWith(ApiUrls.URL_SITE_RECENT_COMMENTS)))
                .andExpect(jsonPath("$._links.tagCloud.href", endsWith(ApiUrls.URL_SITE_TAG_CLOUDS)))
                .andExpect(jsonPath("$._links.postComment.templated", is(true)))
                .andExpect(jsonPath("$._links.postComment.href", endsWith(ApiUrls.URL_USER_BLOGS_BLOG_COMMENTS)))
                ;
    }
    
    @Test
    public void getPublicWebsiteResource_ShouldReturnWebsiteResourceWithoutAuthenticatedUser() throws Exception {
        when(userAccountServiceMock.getCurrentUser()).thenReturn(null);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.authenticated", is(false)))
                //check links
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE)))
                .andExpect(jsonPath("$._links.blogs.templated", is(true)))
                .andExpect(jsonPath("$._links.blogs.href", endsWith(ApiUrls.URL_SITE_BLOGS+"{?page,size,sort}")))
                .andExpect(jsonPath("$._links.blog.templated", is(true)))
                .andExpect(jsonPath("$._links.blog.href", endsWith(ApiUrls.URL_SITE_BLOGS_BLOG)))
                .andExpect(jsonPath("$._links.profile.templated", is(true)))
                .andExpect(jsonPath("$._links.profile.href", endsWith(ApiUrls.URL_SITE_PROFILES_USER)))
                .andExpect(jsonPath("$._links.currentUser.href", endsWith(ApiUrls.URL_SITE_CURRENT_USER)))
                .andExpect(jsonPath("$._links.latestBlog.href", endsWith(ApiUrls.URL_SITE_LATEST_BLOG)))
                .andExpect(jsonPath("$._links.recentBlogs.href", endsWith(ApiUrls.URL_SITE_RECENT_BLOGS)))
                .andExpect(jsonPath("$._links.recentComments.href", endsWith(ApiUrls.URL_SITE_RECENT_COMMENTS)))
                .andExpect(jsonPath("$._links.tagCloud.href", endsWith(ApiUrls.URL_SITE_TAG_CLOUDS)))
                .andExpect(jsonPath("$._links.postComment.templated", is(true)))
                .andExpect(jsonPath("$._links.postComment.href", endsWith(ApiUrls.URL_USER_BLOGS_BLOG_COMMENTS)))
                ;
    }

    @Test
    public void getCurrentUserAccount_ShouldReturnCurrentUser() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();

        when(userAccountServiceMock.getCurrentUser()).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_CURRENT_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.displayName", is (USER_DISPLAY_NAME)))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.author", is(false)))
                ;
    }

    @Test
    public void getCurrentUserAccount_ShouldReturnBlankIfNotAuthenticated() throws Exception {
        when(userAccountServiceMock.getCurrentUser()).thenReturn(null);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_CURRENT_USER))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                ;
    }

    @Test
    public void getUserProfile_ShouldReturnUserProfile() throws Exception {
        UserAccount user = getTestLoggedInUserWithAdminRole();

        when(userAccountRepositoryMock.findByUserId(USER_ID)).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_PROFILES_USER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.displayName", is (USER_DISPLAY_NAME)))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.author", is(false)))
                //check links
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE_PROFILES+"/"+USER_ID)))
                .andExpect(jsonPath("$._links.comments.templated", is(true)))
                .andExpect(jsonPath("$._links.comments.href", endsWith(
                        ApiUrls.URL_SITE_PROFILES+"/"+USER_ID+"/comments{?page,size,sort}")))
                ;
    }
    
    @Test
    public void getUserApprovedCommentPosts() throws Exception {
        Page<CommentPost> page = new PageImpl<CommentPost>(getTestApprovedCommentPostList(), new PageRequest(0, 10), 2);

        when(commentPostRepositoryMock.findByAuthorIdAndStatusOrderByCreatedTimeDesc(
                eq(USER_ID), eq(CommentStatusType.APPROVED), any(Pageable.class)))
            .thenReturn(page);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_PROFILES_USER_COMMENTS, USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.commentPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].id", is(COMMENTS_1_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].blogPostId", is(BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].authorId", is(COMMENTS_1_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].content", is (COMMENTS_1_CONTENT)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].id", is(COMMENTS_2_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].blogPostId", is(BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].authorId", is(COMMENTS_2_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].content", is(COMMENTS_2_CONTENT)))
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith("/comments{?page,size,sort}")))
                .andExpect(jsonPath("$.page.size", is(10)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                ;

        verify(commentPostRepositoryMock, times(1)).findByAuthorIdAndStatusOrderByCreatedTimeDesc(
                eq(USER_ID), eq(CommentStatusType.APPROVED), any(Pageable.class));
        verifyNoMoreInteractions(commentPostRepositoryMock);

    }
    
    @Test
    public void getLatestBlogPost_ShouldReturnBlogPost() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        
        when(blogPostRepositoryMock.findByPublishedIsTrueOrderByPublishedTimeDesc(any(Pageable.class)))
            .thenReturn(new PageImpl<BlogPost>(Arrays.asList(blog), new PageRequest(0, 1), 1));
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_LATEST_BLOG))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is (BLOG_ID)))
                .andExpect(jsonPath("$.title", is (BLOG_TITLE)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE_BLOGS+"/"+BLOG_ID)))
                .andExpect(jsonPath("$._links.comments.templated", is(true)))
                .andExpect(jsonPath("$._links.comments.href", endsWith(
                        ApiUrls.URL_SITE_BLOGS+"/"+BLOG_ID+"/comments{?page,size,sort}")))
                .andExpect(jsonPath("$._links.author.href", endsWith(ApiUrls.URL_SITE_PROFILES+"/"+BLOG_AUTHOR_ID)))
                ;
    }
    
    @Test
    public void getRecentPublicBlogPosts_ShouldReturnRecentBlogPosts() throws Exception {
        Pageable pageable = new PageRequest(0, 4);
        
        when(blogPostRepositoryMock.findByPublishedIsTrueOrderByPublishedTimeDesc(any(Pageable.class)))
            .thenReturn(new PageImpl<BlogPost>(getTestPublishedBlogPostList(), pageable, 2));
    
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_RECENT_BLOGS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.blogPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].id", is(BLOGS_1_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].authorId", is(BLOGS_1_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[0].title", is(BLOGS_1_TITLE)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].id", is(BLOGS_2_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].authorId", is(BLOGS_2_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.blogPostList[1].title", is(BLOGS_2_TITLE)))
                //check links
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE_RECENT_BLOGS)))
                ;
        
        verify(blogPostRepositoryMock, times(1)).findByPublishedIsTrueOrderByPublishedTimeDesc(pageable);
        verifyNoMoreInteractions(blogPostRepositoryMock);

    }

    @Test
    public void getRecentPublicCommentPosts_ShouldReturnRecentCommentPosts() throws Exception {
        Pageable pageable = new PageRequest(0, 4);
        
        when(commentPostRepositoryMock.findByStatusOrderByCreatedTimeDesc(eq(CommentStatusType.APPROVED), any(Pageable.class)))
            .thenReturn(new PageImpl<CommentPost>(getTestApprovedCommentPostList(), pageable, 2));
    
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_RECENT_COMMENTS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.commentPostList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].id", is(COMMENTS_1_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].blogPostId", is(BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].authorId", is(COMMENTS_1_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[0].content", is (COMMENTS_1_CONTENT)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].id", is(COMMENTS_2_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].blogPostId", is(BLOG_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].authorId", is(COMMENTS_2_AUTHOR_ID)))
                .andExpect(jsonPath("$._embedded.commentPostList[1].content", is(COMMENTS_2_CONTENT)))
                //check links
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_SITE_RECENT_COMMENTS)))
                ;
        
        verify(commentPostRepositoryMock, times(1)).findByStatusOrderByCreatedTimeDesc(CommentStatusType.APPROVED, pageable);
        verifyNoMoreInteractions(commentPostRepositoryMock);

    }

}
