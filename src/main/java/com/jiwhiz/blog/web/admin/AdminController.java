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
package com.jiwhiz.blog.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.web.AbstractPageController;
import com.jiwhiz.blog.web.PageWrapper;
import com.jiwhiz.blog.web.site.CommentForm;

/**
 * Controller for admin pages.
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends AbstractPageController{
    //private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController() {
    }

//    @RequestMapping(value = "/accessReport", method = RequestMethod.GET, produces = "text/html")
//    public String listAccess(Model uiModel) {
//        uiModel.addAttribute("records", adminService.getLastDayAccess());
//        return "admin/accessReport";
//    }

    @RequestMapping(value = "/listUsers" , method = RequestMethod.GET, produces = "text/html")
    public String listUsers(Model uiModel, Pageable pageable) {
        PageWrapper<UserAccount> page = new PageWrapper<UserAccount>(accountService.getAllUsers(pageable), "/admin/listUsers");
        for (UserAccount account : page.getContent()){
            account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        }
        uiModel.addAttribute("page", page);
        return "admin/listUsers";
    }

    @RequestMapping(value = "/lockUser/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String lockUser(@PathVariable("userId") String userId, Model uiModel) {
        accountService.lockAccount(userId, true);
        return "redirect:/admin/listUsers";
    }


    @RequestMapping(value = "/unlockUser/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String unlockUser(@PathVariable("userId") String userId, Model uiModel) {
        accountService.lockAccount(userId, false);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/trustUser/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String trustUser(@PathVariable("userId") String userId, Model uiModel) {
        accountService.trustAccount(userId, true);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/untrustUser/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String untrustUser(@PathVariable("userId") String userId, Model uiModel) {
        accountService.trustAccount(userId, false);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/setAuthor/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String setAuthor(@PathVariable("userId") String userId, Model uiModel) {
        accountService.setAuthor(userId, true);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/unsetAuthor/{userId}", method = RequestMethod.GET, produces = "text/html")
    public String unsetAuthor(@PathVariable("userId") String userId, Model uiModel) {
        accountService.setAuthor(userId, false);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/listComments" , method = RequestMethod.GET, produces = "text/html")
    public String listComments(Model uiModel, Pageable pageable) {
        PageWrapper<CommentPost> page = new PageWrapper<CommentPost>(commentPostService.getAllComments(pageable), "/admin/listComments");
        uiModel.addAttribute("page", page);
        return "admin/listComments";
    }
    
    @RequestMapping(value = "/updateComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String updateCommentForm(@PathVariable("id") String id, Model uiModel) {
        uiModel.addAttribute("commentForm", new CommentForm(commentPostService.getCommentById(id)));
        return "admin/editComment";
    }

    @RequestMapping(value = "/updateComment", method = RequestMethod.PUT, produces = "text/html")
    public String updateComment(@ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("commentForm", commentForm);
            return "admin/editComment";
        }
        
        commentPostService.updateComment(commentForm.getId(), commentForm.getContent());
        return "redirect:/admin/listComments";
    }

    @RequestMapping(value = "/publishComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String publishComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentPublished(id, true);
        return "redirect:/admin/listComments";
    }

    @RequestMapping(value = "/unpublishComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String unpublishComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentPublished(id, false);
        return "redirect:/admin/listComments";
    }

    @RequestMapping(value = "/spamComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String spamComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentSpam(id, true);
        return "redirect:/admin/listComments";
    }

    @RequestMapping(value = "/unspamComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String unspamComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.setCommentSpam(id, false);
        return "redirect:/admin/listComments";
    }

    @RequestMapping(value = "/deleteComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String deleteComent(@PathVariable("id") String id, Model uiModel) {
        commentPostService.deleteComment(id);
        return "redirect:/admin/listComments";
    }

}
