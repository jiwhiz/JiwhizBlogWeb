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
package com.jiwhiz.blog.web.post;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.AbstractPost;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentStatusType;
import com.jiwhiz.blog.web.AbstractRestController;
import com.jiwhiz.blog.web.SystemMessageSender;
import com.jiwhiz.blog.web.dto.BlogPostDTO;

/**
 * RESTful Service for current logged in author managing blog posts.
 * 
 * <p>API: '<b>rest/myPost/blogs/:action:blogId/:blogAction</b>'
 * 
 * <p><b>:action</b> can be
 * <ul>
 * <li>'list' - return current user's blogs.</li>
 * <li>'create' - create a new blog.</li>
 * </ul>
 * </p>
 * <p><b>:blogAction</b> can be
 * <ul>
 * <li>'updateContent' - update blog content.</li>
 * <li>'updateMeta' - update blog meta data, like title, path, tags.</li>
 * <li>'publish' - publish blog.</li>
 * <li>'unpublish' - un-publish blog.</li>
 * </ul>
 * </p>

 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping("/rest/myPost/blogs")
public class MyBlogPostRestService extends AbstractRestController {
	private static final Logger logger = LoggerFactory.getLogger(MyBlogPostRestService.class);
	
	@Inject
	private SystemMessageSender systemMessageSender;
	
    @RequestMapping( value = "/list", method = RequestMethod.GET )
    @ResponseBody
    public List<BlogPostDTO> listCurrentUserBlogPosts() {
        List<BlogPostDTO> result = new ArrayList<BlogPostDTO>();
    	UserAccount currentUser = userAccountService.getCurrentUser();
    	List<BlogPost> blogs = blogPostRepository.findByAuthorKeyOrderByCreatedTimeDesc(currentUser.getKey());
    	for (BlogPost blog : blogs) {
    	    result.add(new BlogPostDTO(blog, 
    	            commentPostRepository.countByBlogPostKey(blog.getKey()),
    	            commentPostRepository.countByBlogPostKeyAndStatus(blog.getKey(), CommentStatusType.APPROVED), 
    	            commentPostRepository.countByBlogPostKeyAndStatus(blog.getKey(), CommentStatusType.PENDING)));
    	}
    	
    	return result;
    }

    @RequestMapping( value = "/{blogId}", method = RequestMethod.GET )
    @ResponseBody
    public BlogPostDTO getBlogForEdit(@PathVariable("blogId") String blogId) {
        BlogPost blog = blogPostRepository.findByPostId(blogId);
        return new BlogPostDTO(blog);
    }

    @RequestMapping( value = "/create", method = RequestMethod.POST )
    @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public BlogPostDTO createBlogPost(@RequestBody BlogPostDTO blogPostDto) {
    	UserAccount currentUser = userAccountService.getCurrentUser();
        BlogPost newBlog = blogPostService.createPost(
                currentUser, blogPostDto.getTitle(), blogPostDto.getContent(), blogPostDto.getTagString());
        logger.debug("After save, blog id=" + newBlog.getPostId());
        return new BlogPostDTO(newBlog);
    }

    @RequestMapping( value = "/{blogId}/updateContent", method = RequestMethod.PUT )
    @ResponseBody
    public BlogPostDTO updateBlogPostContent(@PathVariable("blogId") String blogId, @RequestBody BlogPostDTO blogPostDto) {
    	BlogPost blogPost = blogPostRepository.findByPostId(blogId);
    	checkIsAuthorOfPost(blogPost);
    	blogPost.updateContent(blogPostDto.getContent());
    	blogPost = blogPostRepository.save(blogPost);
    	return new BlogPostDTO(blogPost);
    }

    @RequestMapping( value = "/{blogId}/updateMeta", method = RequestMethod.PUT )
    @ResponseBody
    public BlogPostDTO updateBlogPostMeta(@PathVariable("blogId") String blogId, @RequestBody BlogPostDTO blogPostDto) {
    	BlogPost blogPost = blogPostRepository.findByPostId(blogId);
    	checkIsAuthorOfPost(blogPost);
    	blogPost.updateMeta(blogPostDto.getTitle(), blogPostDto.getPublishedPath(), blogPostDto.getTagString());
    	blogPost = blogPostRepository.save(blogPost);
        return new BlogPostDTO(blogPost);
    }

    @RequestMapping( value = "/{blogId}/publish", method = RequestMethod.PUT )
    @ResponseBody
    public BlogPostDTO publishBlog(@PathVariable("blogId") String blogId, @RequestBody BlogPostDTO blogPostDto) {
        BlogPost blogPost = blogPostRepository.findByPostId(blogId);
        checkIsAuthorOfPost(blogPost);
        blogPost.publish(blogPostDto.getPublishedPath(), blogPostDto.getPublishedYear(), blogPostDto.getPublishedMonth());
        blogPost = blogPostRepository.save(blogPost);
        
        systemMessageSender.sendNewPostPublished(userAccountService.getCurrentUser(), blogPost);
        return new BlogPostDTO(blogPost, 
                commentPostRepository.countByBlogPostKey(blogPost.getKey()),
                commentPostRepository.countByBlogPostKeyAndStatus(blogPost.getKey(), CommentStatusType.APPROVED), 
                commentPostRepository.countByBlogPostKeyAndStatus(blogPost.getKey(), CommentStatusType.PENDING));
    }

    @RequestMapping( value = "/{blogId}/unpublish", method = RequestMethod.PUT )
    @ResponseBody
    public BlogPostDTO unpublishBlog(@PathVariable("blogId") String blogId, @RequestBody BlogPostDTO blogPostDto) {
        BlogPost blogPost = blogPostRepository.findByPostId(blogId);
        checkIsAuthorOfPost(blogPost);
        blogPost.unpublish();
        blogPost = blogPostRepository.save(blogPost);
        return new BlogPostDTO(blogPost, 
                commentPostRepository.countByBlogPostKey(blogPost.getKey()),
                commentPostRepository.countByBlogPostKeyAndStatus(blogPost.getKey(), CommentStatusType.APPROVED), 
                commentPostRepository.countByBlogPostKeyAndStatus(blogPost.getKey(), CommentStatusType.PENDING));
    }
    
    protected void checkIsAuthorOfPost(AbstractPost post) throws AccessDeniedException {
        if (post == null || !post.getAuthorKey().equals(userAccountService.getCurrentUser().getKey())){
            throw new AccessDeniedException("Cannot access the post, becasue current user is not the author of the post.");
        }
    }

    
}
