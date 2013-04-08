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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.blog.domain.account.AccountUtils;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.SlidePost;
import com.jiwhiz.blog.message.Message;
import com.jiwhiz.blog.message.MessageType;
import com.jiwhiz.blog.web.AbstractPublicPageController;
import com.jiwhiz.blog.web.CommentNotificationSender;
import com.jiwhiz.blog.web.PageWrapper;

/**
 * Controller for public blog list, blog view pages. Handle posting comment function.
 * 
 * @author Yuan Ji
 * 
 */
@Controller
public class BlogController extends AbstractPublicPageController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Inject
    CommentNotificationSender commentNotificationSender;
    
    /**
     * @return the commentNotificationSender
     */
    public CommentNotificationSender getCommentNotificationSender() {
        return commentNotificationSender;
    }

    /**
     * @param commentNotificationSender the commentNotificationSender to set
     */
    public void setCommentNotificationSender(CommentNotificationSender commentNotificationSender) {
        this.commentNotificationSender = commentNotificationSender;
    }

    public BlogController() {
    }

    /**
     * Display list of all public blogs. 
     * 
     * @param uiModel
     * @param request
     * @return
     */
    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public String blog(Model uiModel, HttpServletRequest request, Pageable pageable) {
        counterService.logVisit();
        PageWrapper<BlogPost> page = new PageWrapper<BlogPost>(blogPostService.getAllPublishedPosts(pageable), "/blog");
        uiModel.addAttribute("page", page);
        return "site/blogs";
    }

    /**
     * Display list of all public blogs with specified tag.
     * @param tag
     * @param uiModel
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/blog/tag/{tag}", method = RequestMethod.GET)
    public String blogWithTag(@PathVariable("tag") String tag, Model uiModel, HttpServletRequest request, Pageable pageable) {
        counterService.logVisit();
        PageWrapper<BlogPost> page = new PageWrapper<BlogPost>(blogPostService.getPublishedPostsByTag(tag, pageable), "/blog/tag/"+tag);
        uiModel.addAttribute("page", page);
        uiModel.addAttribute("message", 
                new Message(MessageType.INFO, "Blog posts with tag <span class=\"label label-success\">"+tag+"</span>:"));

        return "site/blogs";
    }

    /**
     * For single blog post page.
     * 
     * @param year
     * @param month
     * @param path
     * @param uiModel
     * @param request
     * @return
     */
    @RequestMapping(value = "/post/{year}/{month}/{path}", produces = "text/html")
    public String displayPost(@PathVariable("year") int year, @PathVariable("month") int month,
            @PathVariable("path") String path, Model uiModel, HttpServletRequest request) {
        counterService.logVisit();
        BlogPost blogPost = blogPostService.getPostByPublishedPath(year, month, path);

        if (blogPost == null) {
            return "redirect:/resourceNotFound";
        }
        counterService.logBlogPostVisit(blogPost.getId());
        // this.adminService.recordAccess(getUserIpAddress(request), blogPost.getFullPublishedPath());
        uiModel.addAttribute("blog", blogPost);
        uiModel.addAttribute("commentForm", new CommentForm());
        return "site/post";
    }

    /**
     * Post comment from blog post page.
     * 
     * @param id
     * @param commentForm
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.POST, produces = "text/html")
    public String postComment(@PathVariable("id") String id,
            @ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        logger.debug("==>BlogController.postComment() for blog id=" + id);
        
        //get login user id
        UserAccount account = AccountUtils.getLoginUserAccount();
        if (account == null){
            logger.warn("Cannot post comment without login");
            return "redirect:/signin";
        }
        
        CommentPost comment = commentPostService.addComment(id, commentForm.getContent());
        if (comment == null){
            return "redirect:/blog";
        }

        BlogPost blogPost = comment.getBlogPost();
        
        if (this.commentNotificationSender != null){
            //send notification email to author
            comment.setBlogPost(blogPost);
            comment.setAuthorAccount(account);
            this.commentNotificationSender.send(comment);
        }
        
        if (!comment.isPublished()){
            uiModel.addAttribute("blog", blogPost);
            uiModel.addAttribute("commentForm", new CommentForm());
            uiModel.addAttribute("message", 
                     new Message(MessageType.INFO, "Thank you for the feedback. Your comment will be published after approval."));
            return "site/post";
        }

        return "redirect:/post/" + blogPost.getFullPublishedPath();
    }
    
    /**
     * For single slide page.
     * 
     * @param year
     * @param month
     * @param path
     * @param uiModel
     * @param request
     * @return
     */
    @RequestMapping(value = "/presentation/{path}", produces = "text/html")
    public String displaySlide(@PathVariable("path") String path, Model uiModel, HttpServletRequest request) {
        SlidePost slidePost = slidePostService.getSlideByPublishedPath(path);

        if (slidePost == null) {
            return "redirect:/resourceNotFound";
        }
        counterService.logSlidePostVisit(slidePost.getId());
        // this.adminService.recordAccess(getUserIpAddress(request), blogPost.getFullPublishedPath());
        uiModel.addAttribute("slide", slidePost);
        //TODO return html template based on type.
        return "deckjsPresentation";
    }

}
