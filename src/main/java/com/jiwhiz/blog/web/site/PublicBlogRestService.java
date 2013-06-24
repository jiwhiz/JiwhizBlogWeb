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
package com.jiwhiz.blog.web.site;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.CommentStatusType;
import com.jiwhiz.blog.web.AbstractRestController;
import com.jiwhiz.blog.web.dto.BlogPostDTO;
import com.jiwhiz.blog.web.dto.CommentPostDTO;

/**
 * RESTful Service for public blog list and dislay pages.
 * <p>API: 'rest/public/blogs/:action:year/:month/:path'
 * 
 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping("/rest/public/blogs")
public class PublicBlogRestService extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(PublicBlogRestService.class);

    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    @ResponseBody
    public BlogPostDTO getLatestBlog() {
        logger.debug("==>PublicBlogController.getLatestBlog()");
        counterService.logVisit();

        List<BlogPost> blogPosts = blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc();
        if (blogPosts.size() > 0) {
            BlogPost latestBlog = blogPosts.get(0);
            UserAccount author = this.userAccountRepository.findOne(latestBlog.getAuthorKey());
            logger.debug("Found lastest blog, title is '" + latestBlog.getTitle() + "', id is "
                    + latestBlog.getPostId() + ", author is " + author.getDisplayName());
            return new BlogPostDTO(latestBlog, commentPostRepository.countByBlogPostKeyAndStatus(latestBlog.getKey(),
                    CommentStatusType.APPROVED), author);
        }
        logger.debug("No blog found, return null.");
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<BlogPostDTO> getPublishedBlogs() {
        logger.debug("==>PublicBlogController.getPublishedBlogs()");
        counterService.logVisit();
        List<BlogPostDTO> result = new ArrayList<BlogPostDTO>();
        for (BlogPost blog : blogPostRepository.findByPublishedIsTrueOrderByPublishedTimeDesc()) {
            UserAccount author = this.userAccountRepository.findOne(blog.getAuthorKey());
            result.add(new BlogPostDTO(blog, commentPostRepository.countByBlogPostKeyAndStatus(blog.getKey(),
                    CommentStatusType.APPROVED), author));
        }

        return result;
    }

    @RequestMapping(value = "/{year}/{month}/{path}", method = RequestMethod.GET)
    @ResponseBody
    public BlogPostDTO getPublicBlogPostByPath(@PathVariable("year") int year, @PathVariable("month") int month,
            @PathVariable("path") String path) {
        logger.debug("==>PublicBlogController.getPostByPath(), year=" + year + ", month=" + month + ", path=" + path);
        counterService.logVisit();
        BlogPost blog = blogPostRepository.findByPublishedYearAndPublishedMonthAndPublishedPath(year, month, path);

        if (blog == null) {
            return null;
        }
        counterService.logBlogPostVisit(blog.getPostId());
        UserAccount author = this.userAccountRepository.findOne(blog.getAuthorKey());
        BlogPostDTO blogPostDto = new BlogPostDTO(blog, commentPostRepository.countByBlogPostKeyAndStatus(blog.getKey(),
                CommentStatusType.APPROVED), author);
        List<CommentPostDTO> commentDtos = new ArrayList<CommentPostDTO>();
        List<CommentPost> comments = commentPostRepository.findByBlogPostKeyAndStatus(blog.getKey(), 
                CommentStatusType.APPROVED, new Sort(Direction.ASC, "createdTime"));
        for (CommentPost comment : comments) {
            UserAccount user = userAccountRepository.findOne(comment.getAuthorKey());
            commentDtos.add(new CommentPostDTO(comment, user, null));
        }
        blogPostDto.setComments(commentDtos);

        return blogPostDto;
    }
}
