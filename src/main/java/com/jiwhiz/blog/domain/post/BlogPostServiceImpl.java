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
package com.jiwhiz.blog.domain.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for BlogPostService.
 * 
 * @author Yuan Ji
 *
 */
public class BlogPostServiceImpl extends AbstractPostServiceImpl implements BlogPostService {
    final static Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);

    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;

    @Inject
    public BlogPostServiceImpl(UserAccountRepository accountRepository, BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, UserAdminService userAdminService,
            CounterService counterService) {
        super(accountRepository, userAdminService, counterService);
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogService#getAllPublishedPosts(org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<BlogPost> getAllPublishedPosts(Pageable pageable) {
        Page<BlogPost> blogList = blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc(pageable);
        loadAuthorProfile(blogList);
        
        for (BlogPost blogPost : blogList){
            setComment(blogPost);
        }
        return blogList;
    }

    private void setComment(BlogPost blogPost){
        //load public comments TODO: use count query
        List<CommentPost> comments = this.commentPostRepository.findByPostIdAndPublishedIsTrue(
                blogPost.getId(), new Sort(Sort.Direction.ASC, "createdTime"));
        blogPost.setComments(comments);
        blogPost.setCommentCount(comments.size());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#getPublishedPosts(int)
     */
    @Override
    public List<BlogPost> getPublishedPosts(int count) {
        List<BlogPost> blogList = blogPostRepository.findByPublishedIsTrue(new Sort(Sort.Direction.DESC, "publishedTime"));
        List<BlogPost> returnList = new ArrayList<BlogPost>();
        Iterator<BlogPost> iter = blogList.iterator();
        while (count > 0 && iter.hasNext()) {
            BlogPost blogPost = iter.next();
            loadAuthorProfile(blogPost);
            setComment(blogPost);
            returnList.add(blogPost);
            count--;
        }

        return returnList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#getPublishedPostsByTag(java.lang.String, org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<BlogPost> getPublishedPostsByTag(String tag, Pageable pageable) {
        Page<BlogPost> blogList = blogPostRepository.findByPublishedIsTrueAndTagsOrderByPublishedTimeDesc(tag, pageable);
        loadAuthorProfile(blogList);
        
        for (BlogPost blogPost : blogList){
            setComment(blogPost);
        }
        return blogList;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#getTagCloud()
     */
    @Override
    public Map<String, Integer> getTagCloud() {
        Map<String, Integer> tagCloud = new HashMap<String, Integer>();
        List<BlogPost> blogList = blogPostRepository.findAllPublishedPostsIncludeTags();
        for (BlogPost blog : blogList) {
            for (String tag : blog.getTags()) {
                tag = tag.trim();
                if (tagCloud.containsKey(tag)){
                    tagCloud.put(tag,  tagCloud.get(tag) + 1);
                } else {
                    tagCloud.put(tag,  1);
                }
            }
        }
        
        return tagCloud;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#getPostByPublishedPath(int, int, java.lang.String)
     */
    @Override
    public BlogPost getPostByPublishedPath(int year, int month, String path) {
        BlogPost blogPost = blogPostRepository.findByPublishedYearAndPublishedMonthAndPublishedPath(year, month, path);
        if (blogPost == null || !blogPost.isPublished()) {
            return null;
        }

        return loadPublishBlogPost(blogPost);
    }

    private BlogPost loadPublishBlogPost(BlogPost blogPost){
        loadAuthorProfile(blogPost);
        List<CommentPost> comments = this.commentPostRepository.findByPostIdAndPublishedIsTrue(blogPost.getId(),
                        new Sort(Sort.Direction.ASC, "createdTime"));
        loadAuthorProfile(comments);
        blogPost.setComments(comments);
        blogPost.setCommentCount(comments.size());
    	return blogPost;
    }
    
    /* (non-Javadoc)
     * 
	 * @see com.jiwhiz.blog.domain.post.BlogPostService#getPublishedPostById(java.lang.String)
	 */
	@Override
	public BlogPost getPublishedPostById(String id) {
		BlogPost blogPost = blogPostRepository.findOne(id);
		if (!blogPost.isPublished()){
			return null;
		}
		
		return loadPublishBlogPost(blogPost);
	}

	/*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogService#getPostById(java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AUTHOR')")
    public BlogPost getPostById(String id) {
        BlogPost blog = blogPostRepository.findOne(id);
        checkIsAdminOrAuthorOfPost(blog);
        loadAuthorProfile(blog);
        return blog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#getPostsForCurrentUser()
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public Page<BlogPost> getPostsForCurrentUser(Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), 
                new Sort(Sort.Direction.DESC, "createdTime"));
        Page<BlogPost> blogList = blogPostRepository.findByAuthorId(
                    userAdminService.getUserId(), pageable);
        loadAuthorProfile(blogList);
        for (BlogPost blogPost : blogList){
            // load all comments for each blog
            List<CommentPost> comments = this.commentPostRepository.findByPostId(blogPost.getId(),
                            new Sort(Sort.Direction.ASC, "createdTime"));
            loadAuthorProfile(comments);
            blogPost.setComments(comments);
            blogPost.setCommentCount(comments.size());
            
            //set visit count
            blogPost.setVisits(counterService.getBlogPostVisitCount(blogPost.getId()));
        }
        
        return blogList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogService#createPost(java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public BlogPost createPost(String title, String content, String tagString) {
        BlogPost post = new BlogPost(userAdminService.getUserId(), title, content, tagString);
        blogPostRepository.save(post);
        return post;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#updateContent(java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public BlogPost updateContent(String id, String content) {
        BlogPost blog = blogPostRepository.findOne(id);
        checkIsAuthorOfPost(blog);

        blog.updateContent(content);
        blog = blogPostRepository.save(blog);
        return blog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.BlogPostService#updateMeta(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public BlogPost updateMeta(String id, String title, String tagString) {
        BlogPost blog = blogPostRepository.findOne(id);
        checkIsAuthorOfPost(blog);
        
        blog.updateMeta(title, tagString);
        blog = blogPostRepository.save(blog);
        return blog;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public BlogPost publish(String id, String path) {
        BlogPost blog = blogPostRepository.findOne(id);
        checkIsAuthorOfPost(blog);
        
        blog.publish(path);
        blog = blogPostRepository.save(blog);
        return blog;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public BlogPost unpublish(String id) {
        BlogPost blog = blogPostRepository.findOne(id);
        checkIsAuthorOfPost(blog);

        blog.unpublish();
        blog = blogPostRepository.save(blog);
        return blog;
    }

}
