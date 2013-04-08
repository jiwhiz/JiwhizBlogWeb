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
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jiwhiz.blog.domain.account.AccountUtils;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for CommentPostService.
 * 
 * @author Yuan Ji
 * 
 */
public class CommentPostServiceImpl extends AbstractPostServiceImpl implements CommentPostService {
    final static Logger logger = LoggerFactory.getLogger(CommentPostServiceImpl.class);

    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;

    @Inject
    public CommentPostServiceImpl(UserAccountRepository accountRepository, BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, UserAdminService userAdminService,
            CounterService counterService) {
        super(accountRepository, userAdminService, counterService);
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#getCommentById(java.lang.String)
     */
    @Override
    public CommentPost getCommentById(String id) {
        CommentPost comment = commentPostRepository.findOne(id);
        if (comment != null) {
            loadAuthorProfile(comment);
            comment.setBlogPost(blogPostRepository.findOne(comment.getPostId()));
            return comment;
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#getPublishedComments(int)
     */
    @Override
    public List<CommentPost> getPublishedComments(int count) {
        List<CommentPost> commentList = commentPostRepository.findByPublishedIsTrue(new Sort(Sort.Direction.DESC,
                "createdTime"));
        List<CommentPost> returnList = new ArrayList<CommentPost>();
        Iterator<CommentPost> iter = commentList.iterator();
        while (count > 0 && iter.hasNext()) {
            CommentPost comment = iter.next();
            loadAuthorProfile(comment);
            comment.setBlogPost(blogPostRepository.findOne(comment.getPostId()));
            returnList.add(comment);
            count--;
        }

        return returnList;
    }

    @Override
    public List<CommentPost> getPublishedCommentsForPost(String postId) {
        List<CommentPost> comments = commentPostRepository.findByPostIdAndPublishedIsTrue(postId, new Sort(
                Sort.Direction.DESC, "createdTime"));
        loadAuthorProfile(comments);
        return comments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#getAllCommentsForPost(java.lang.String) 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public List<CommentPost> getAllCommentsForPost(String blogPostId) {
        BlogPost blog = blogPostRepository.findOne(blogPostId);
        checkIsAuthorOfPost(blog);
        List<CommentPost> comments = commentPostRepository.findByPostId(blogPostId, 
                new Sort(Sort.Direction.DESC, "createdTime"));
        loadAuthorProfile(comments);
        return comments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#getPublishedCommentsForUser(java.lang.String)
     */
    @Override
    public List<CommentPost> getPublishedCommentsForUser(String userId) {
        List<CommentPost> comments = commentPostRepository.findByAuthorIdAndPublishedIsTrue(
                userId, new Sort(Sort.Direction.DESC, "createdTime"));
        loadAuthorProfile(comments);
        loadBlogPostForComment(comments);
        return comments;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<CommentPost> getAllCommentsForUser(String userId, Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), 
                new Sort(Sort.Direction.DESC, "createdTime"));
        Page<CommentPost> comments = commentPostRepository.findByAuthorId(userId, pageable);
        loadAuthorProfile(comments);
        loadBlogPostForComment(comments);
        return comments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#getAllComments(org.springframework.data.domain.Pageable)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<CommentPost> getAllComments(Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), 
                new Sort(Sort.Direction.DESC, "createdTime"));
        Page<CommentPost> comments = commentPostRepository.findAll(pageable);
        loadAuthorProfile(comments);
        loadBlogPostForComment(comments);
        return comments;
    }

    private void loadBlogPostForComment(Iterable<CommentPost> commentList) {
        for (CommentPost comment : commentList) {
            comment.setBlogPost(blogPostRepository.findOne(comment.getPostId()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#addComment(java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentPost addComment(String blogPostId, String content) {
        String userId = AccountUtils.getLoginUserId();
        assert userId != null;

        UserAccount account = this.accountRepository.findByUserId(userId);
        assert account != null;

        BlogPost blogPost = blogPostRepository.findOne(blogPostId);
        if (blogPost == null) {
            return null;
        }

        // TODO use Factory?
        CommentPost comment = new CommentPost(blogPost.getId(), account.getUserId(), content);
        logger.debug("Add comment by " + account.getUserId());

        if (account.isTrustedAccount()) {
            comment.setPublished(true);
        }

        comment = commentPostRepository.save(comment);
        comment.setAuthorAccount(account);
        comment.setBlogPost(blogPost);

        return comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#updateComment(java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public CommentPost updateComment(String commentPostId, String content) {
        CommentPost comment = commentPostRepository.findOne(commentPostId);
        checkIsAdminOrAuthorOfPost(comment);

        comment.setContent(content);
        comment = commentPostRepository.save(comment);
        return comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#setCommentPublished(java.lang.String, boolean)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public CommentPost setCommentPublished(String commentPostId, boolean published) {
        CommentPost comment = commentPostRepository.findOne(commentPostId);
        checkIsAdminOrAuthorOfPost(comment);
        
        comment.setPublished(published);
        comment = commentPostRepository.save(comment);
        return comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#setCommentSpam(java.lang.String, boolean)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public CommentPost setCommentSpam(String id, boolean spam) {
        CommentPost comment = commentPostRepository.findOne(id);
        checkIsAdminOrAuthorOfPost(comment);
        
        comment.setSpam(spam);
        comment = commentPostRepository.save(comment);
        return comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.post.CommentPostService#deleteComment(java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteComment(String id) {
        CommentPost comment = commentPostRepository.findOne(id);
        if (comment != null) {
            commentPostRepository.delete(comment);
        }
    }

}
