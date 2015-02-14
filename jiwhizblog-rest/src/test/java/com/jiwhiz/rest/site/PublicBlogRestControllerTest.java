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
import com.jiwhiz.domain.post.CommentPost;
import com.jiwhiz.domain.post.CommentPostRepository;
import com.jiwhiz.domain.post.CommentStatusType;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */

public class PublicBlogRestControllerTest extends AbstractRestControllerTest {
    
    @Inject
    BlogPostRepository blogPostRepositoryMock;
    
    @Inject
    CommentPostRepository commentPostRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(blogPostRepositoryMock);
        Mockito.reset(commentPostRepositoryMock);
        super.setup();
    }

    @Test
    public void getPublicBlogPosts_ShouldReturnAllPublicBlogPosts() throws Exception {
        Pageable pageable = new PageRequest(0, 10);
        when(blogPostRepositoryMock.findByPublishedIsTrueOrderByPublishedTimeDesc(any(Pageable.class)))
            .thenReturn(new PageImpl<BlogPost>(getTestPublishedBlogPostList(), pageable, 2));
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS))
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
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith("public/blogs{?page,size,sort}")))
                //check page
                .andExpect(jsonPath("$.page.size", is(10)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                ;
        
        verify(blogPostRepositoryMock, times(1)).findByPublishedIsTrueOrderByPublishedTimeDesc(pageable);
        verifyNoMoreInteractions(blogPostRepositoryMock);
    }

    @Test
    public void getPublicBlogPostById_ShouldReturnBlogPost() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG, BLOG_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is(BLOG_ID)))
                .andExpect(jsonPath("$.authorId", is(BLOG_AUTHOR_ID)))
                .andExpect(jsonPath("$.title", is(BLOG_TITLE)))
                .andExpect(jsonPath("$._links.self.href", endsWith(BLOG_ID)))
                .andExpect(jsonPath("$._links.comments.templated", is(true)))
                .andExpect(jsonPath("$._links.comments.href", endsWith(ApiUrls.URL_SITE_BLOGS+"/"+BLOG_ID+"/comments{?page,size,sort}")))
                ;

        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);
    }
    
    @Test
    public void getPublicBlogPost_ShouldReturnHttpStatusCode404ForUnpublishedBlog() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        blog.setPublished(false);
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);

        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG, BLOG_ID))
                .andExpect(status().isNotFound())
                ;
        
        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);
    }
    
    @Test
    public void getBlogApprovedCommentPosts_ShouldReturnApprovedComments() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        Page<CommentPost> page = new PageImpl<CommentPost>(getTestApprovedCommentPostList(), new PageRequest(0, 10), 2);
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        when(commentPostRepositoryMock.findByBlogPostIdAndStatusOrderByCreatedTimeAsc(
                eq(BLOG_ID), eq(CommentStatusType.APPROVED), any(Pageable.class)))
            .thenReturn(page);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS, BLOG_ID))
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
                ;
        
        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);

        verify(commentPostRepositoryMock, times(1)).findByBlogPostIdAndStatusOrderByCreatedTimeAsc(
                eq(BLOG_ID), eq(CommentStatusType.APPROVED), any(Pageable.class));
        verifyNoMoreInteractions(commentPostRepositoryMock);

    }

    @Test
    public void getBlogApprovedCommentPostById_ShouldReturnApprovedComment() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        CommentPost comment = getTestApprovedCommentPost();
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is(COMMENT_ID)))
                .andExpect(jsonPath("$.authorId", is(COMMENT_AUTHOR_ID)))
                .andExpect(jsonPath("$.content", is(COMMENT_CONTENT)))
                .andExpect(jsonPath("$.blogPostId", is(BLOG_ID)))
                .andExpect(jsonPath("$.status", is(CommentStatusType.APPROVED.name())))
                ;

        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);

        verify(commentPostRepositoryMock, times(1)).findOne(COMMENT_ID);
        verifyNoMoreInteractions(commentPostRepositoryMock);
    }
    
    @Test
    public void getBlogApprovedCommentPostById_ShouldReturn404IfNoSuchComment() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(null);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID))
                .andExpect(status().isNotFound())
                ;

        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);

        verify(commentPostRepositoryMock, times(1)).findOne(COMMENT_ID);
        verifyNoMoreInteractions(commentPostRepositoryMock);
    }

    @Test
    public void getBlogApprovedCommentPostById_ShouldReturn404IfCommentIsNotForBlog() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        CommentPost comment = getTestApprovedCommentPost();
        comment.setBlogPostId("Other");
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID))
                .andExpect(status().isNotFound())
                ;

        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);

        verify(commentPostRepositoryMock, times(1)).findOne(COMMENT_ID);
        verifyNoMoreInteractions(commentPostRepositoryMock);
    }

    @Test
    public void getBlogApprovedCommentPostById_ShouldReturn404IfCommentIsNotApproved() throws Exception {
        BlogPost blog = getTestSinglePublishedBlogPost();
        CommentPost comment = getTestApprovedCommentPost();
        comment.setStatus(CommentStatusType.PENDING);
        
        when(blogPostRepositoryMock.findOne(BLOG_ID)).thenReturn(blog);
        when(commentPostRepositoryMock.findOne(COMMENT_ID)).thenReturn(comment);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_SITE_BLOGS_BLOG_COMMENTS_COMMENT, BLOG_ID, COMMENT_ID))
                .andExpect(status().isNotFound())
                ;

        verify(blogPostRepositoryMock, times(1)).findOne(BLOG_ID);
        verifyNoMoreInteractions(blogPostRepositoryMock);

        verify(commentPostRepositoryMock, times(1)).findOne(COMMENT_ID);
        verifyNoMoreInteractions(commentPostRepositoryMock);
    }

}
