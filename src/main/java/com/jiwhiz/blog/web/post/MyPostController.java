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
import org.springframework.web.util.HtmlUtils;

import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.SlidePost;
import com.jiwhiz.blog.message.Message;
import com.jiwhiz.blog.message.MessageType;
import com.jiwhiz.blog.web.AbstractPageController;
import com.jiwhiz.blog.web.PageWrapper;

/**
 * Controller for managing blog/slide posts.
 * 
 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping("/myPost")
public class MyPostController extends AbstractPageController{
    private static final Logger logger = LoggerFactory.getLogger(MyPostController.class);

    public MyPostController() {
    }

    /**
     * List all blogs belong to current login author.
     */
    @RequestMapping(value={"/", "/listBlogPosts"}, method = RequestMethod.GET, produces = "text/html")
    public String listBlogPosts(Model uiModel, Pageable pageable) {
        PageWrapper<BlogPost> page = new PageWrapper<BlogPost>(blogPostService.getPostsForCurrentUser(pageable), "/myPost/listBlogPosts");
        uiModel.addAttribute("page", page);
        return "myPost/listBlogPosts";
    }

    /**
     * create a new blog form and go to new blog page
     * 
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/createBlogPost", method = RequestMethod.GET, produces = "text/html")
    public String createBlogPostForm(Model uiModel) {
        uiModel.addAttribute("blogForm", new BlogForm());
        return "myPost/createBlogPost";
    }

    /**
     * Post new blog data. If no error, save to data store and go to list page.
     * If error, go back to new blog page.
     * 
     * @param blogForm
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/createBlogPost", method = RequestMethod.POST, produces = "text/html")
    public String createBlogPost(@ModelAttribute("blogForm") @Valid BlogForm blogForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        uiModel.asMap().clear(); //why???

        BlogPost newBlog = blogPostService.createPost(blogForm.getTitle(), blogForm.getContent(), blogForm.getTagString());
        logger.debug("After save, blog id=" + newBlog.getId());
        
        String validationError = newBlog.validateHtmlContent();
        
        if (validationError == null){
            return "redirect:/myPost/viewBlogPost/" + encodeUrlPathSegment(newBlog.getId().toString(), httpServletRequest);
        } else {
            //direct to Edit Blog Post Content page if html content has error
            return "redirect:/myPost/updateBlogPostContent/" + encodeUrlPathSegment(newBlog.getId().toString(), httpServletRequest);
        }
    }

    @RequestMapping(value = "/viewBlogPost/{id}", method = RequestMethod.GET, produces = "text/html")
    public String viewBlogPost(@PathVariable("id") String id, Model uiModel, HttpServletRequest httpServletRequest) {
        BlogPost blogPost = blogPostService.getPostById(id);
        if (blogPost == null){
            return "redirect:/";
        }
        
        String validationError = blogPost.validateHtmlContent();
        
        if (validationError != null){
            return "redirect:/myPost/updateBlogPostContent/" + encodeUrlPathSegment(blogPost.getId().toString(), httpServletRequest);
        }

        uiModel.addAttribute("blog", blogPost);
        uiModel.addAttribute("publishForm", new PublishForm(blogPost));
        return "myPost/viewBlogPost";
    }

    @RequestMapping(value = "/publishBlogPost", method = RequestMethod.PUT, produces = "text/html")
    public String publishPost(@ModelAttribute("blogForm") @Valid PublishForm publishForm, BindingResult bindingResult, Model uiModel) {
        BlogPost blogPost = blogPostService.getPostById(publishForm.getId());
        
        if (blogPost == null) {
            return "redirect:/";
        }
        
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("blog", blogPost);
            uiModel.addAttribute("publishForm", publishForm);
            return "myPost/viewBlogPost";
        }

        blogPost = blogPostService.publish(publishForm.getId(), publishForm.getPath());
        
        uiModel.addAttribute("blog", blogPost);
        uiModel.addAttribute("publishForm", new PublishForm(blogPost));
        return "myPost/viewBlogPost";
    }

    @RequestMapping(value = "/unpublishBlogPost/{id}", method = RequestMethod.GET, produces = "text/html")
    public String unpublishPost(@PathVariable("id") String id, Model uiModel) {
        BlogPost blogPost = blogPostService.getPostById(id);
        if (blogPost == null){
            return "redirect:/";
        }
        
        blogPost = blogPostService.unpublish(id);
        
        uiModel.addAttribute("blog", blogPost);
        uiModel.addAttribute("publishForm", new PublishForm(blogPost));
        return "myPost/viewBlogPost";
    }

    @RequestMapping(value = "/updateBlogPostContent/{id}", method = RequestMethod.GET, produces = "text/html")
    public String updateBlogPostContentForm(@PathVariable("id") String id, Model uiModel) {
        BlogPost blogPost = blogPostService.getPostById(id);
        String validationError = blogPost.validateHtmlContent();
        
        if (validationError != null){
            uiModel.addAttribute("message", 
                    new Message(MessageType.ERROR, "Invalidate HTML content: " + HtmlUtils.htmlEscape(validationError)));
        }
        uiModel.addAttribute("blogForm", new BlogForm(blogPost));
        return "myPost/updateBlogPostContent";
    }

    @RequestMapping(value = "/updateBlogPostContent", method = RequestMethod.PUT, produces = "text/html")
    public String updateBlogPostContent(@ModelAttribute("blogForm") @Valid BlogForm blogForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        BlogPost blog = blogPostService.updateContent(blogForm.getId(), blogForm.getContent());
        if (blog == null) {
            //TODO display error message?? 
            return "redirect:/home";
        }
        String validationError = blog.validateHtmlContent();
        
        if (validationError == null){
            return "redirect:/myPost/viewBlogPost/" + encodeUrlPathSegment(blog.getId().toString(), httpServletRequest);
        } else {
            return "redirect:/myPost/updateBlogPostContent/" + encodeUrlPathSegment(blog.getId().toString(), httpServletRequest);
        }
    }

    @RequestMapping(value = "/updateBlogPostMeta/{id}", method = RequestMethod.GET, produces = "text/html")
    public String updateBlogPostMetaForm(@PathVariable("id") String id, Model uiModel) {
        BlogPost blogPost = blogPostService.getPostById(id);
        uiModel.addAttribute("blogForm", new BlogForm(blogPost));
        return "myPost/updateBlogPostMeta";
    }

    @RequestMapping(value = "/updateBlogPostMeta", method = RequestMethod.PUT, produces = "text/html")
    public String updateBlogPostMeta(@ModelAttribute("blogForm") @Valid BlogForm blogForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        BlogPost blog = blogPostService.updateMeta(blogForm.getId(), blogForm.getTitle(), blogForm.getTagString());
        if (blog == null) {
            //TODO display error message
            return "redirect:/home";
        }
        return "redirect:/myPost/viewBlogPost/" + encodeUrlPathSegment(blog.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/approveComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String publishComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentPublished(id, true);
        return "redirect:/myPost/listBlogPosts";
    }

    @RequestMapping(value = "/unapproveComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String unpublishComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentPublished(id, false);
        return "redirect:/myPost/listBlogPosts";
    }

    @RequestMapping(value = "/spamComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String spamComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentSpam(id, true);
        return "redirect:/myPost/listBlogPosts";
    }

    @RequestMapping(value = "/unspamComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String unspamComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentSpam(id, false);
        return "redirect:/myPost/listBlogPosts";
    }

   /**
     * List all slides belong to current login author.
     */
    @RequestMapping(value = "/listSlidePosts", method = RequestMethod.GET, produces = "text/html")
    public String listSlidePosts(Model uiModel, Pageable pageable) {
        PageWrapper<SlidePost> page = new PageWrapper<SlidePost>(slidePostService.getSlidePostsForCurrentUser(pageable), "/myPost/listSlidePosts");
        uiModel.addAttribute("page", page);
        return "myPost/listSlidePosts";
    }

    /**
     * create a new slide form and go to new slide page
     * 
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/createSlidePost", method = RequestMethod.GET, produces = "text/html")
    public String createSlidePostForm(Model uiModel) {
        uiModel.addAttribute("slideForm", new SlideForm());
        return "myPost/createSlidePost";
    }

    /**
     * Post new blog data. If no error, save to data store and go to list page.
     * If error, go back to new blog page.
     * 
     * @param blogForm
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/createSlidePost", method = RequestMethod.POST, produces = "text/html")
    public String createSlidePost(@ModelAttribute("slideForm") @Valid SlideForm slideForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        SlidePost newSlide = slidePostService.createSlide(slideForm.getType(), slideForm.getTitle(), slideForm.getContent(), slideForm.getPublishedPath());

        String validationError = newSlide.validateHtmlContent();
        
        if (validationError == null){
            return "redirect:/presentation/" + encodeUrlPathSegment(newSlide.getPublishedPath(), httpServletRequest);
        } else {
            return "redirect:/myPost/updateSlidePost/" + encodeUrlPathSegment(newSlide.getId().toString(), httpServletRequest);
        }

    }

    @RequestMapping(value = "/updateSlidePost/{id}", method = RequestMethod.GET, produces = "text/html")
    public String updateSlidePostForm(@PathVariable("id") String id, Model uiModel) {
        SlidePost slidePost = slidePostService.getSlideById(id);
        String validationError = slidePost.validateHtmlContent();
        
        if (validationError != null){
            uiModel.addAttribute("message", 
                    new Message(MessageType.ERROR, "Invalidate HTML content: "+validationError));
        }

       uiModel.addAttribute("slideForm", new SlideForm(slidePost));
        return "myPost/updateSlidePost";
    }

    @RequestMapping(value = "/updateSlidePost", method = RequestMethod.PUT, produces = "text/html")
    public String updateSlidePost(@ModelAttribute("slideForm") @Valid SlideForm slideForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        //TODO check publishPath
        
        SlidePost slidePost = slidePostService.getSlideById(slideForm.getId());
        if (slidePost == null) {
            //TODO display error message
            return "home";
        }

        slidePost.update(slideForm.getPublishedPath(), slideForm.getTitle(), slideForm.getContent());
        slidePost = slidePostService.updateSlide(slidePost);
        
        String validationError = slidePost.validateHtmlContent();
        
        if (validationError == null){
            return "redirect:/presentation/" + encodeUrlPathSegment(slidePost.getPublishedPath(), httpServletRequest);
        } else {
            return "redirect:/myPost/updateSlidePost/" + encodeUrlPathSegment(slidePost.getId().toString(), httpServletRequest);
        }
    }
}
