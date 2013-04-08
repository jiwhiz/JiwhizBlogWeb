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
package com.jiwhiz.blog.web.account;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.jiwhiz.blog.domain.account.AccountUtils;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.message.Message;
import com.jiwhiz.blog.message.MessageType;
import com.jiwhiz.blog.web.AbstractPageController;
import com.jiwhiz.blog.web.PageWrapper;
import com.jiwhiz.blog.web.site.CommentForm;

/**
 * Controller for account pages.
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/myAccount")
public class MyAccountController extends AbstractPageController {
    private static final Logger logger = LoggerFactory.getLogger(MyAccountController.class);

    public MyAccountController() {
    }

    /**
     * Overview page, display login user account profile info and social connections.
     * 
     * @param uiModel
     * @param request
     * @return
     */
    @RequestMapping(produces = "text/html")
    public String overview(Model uiModel, WebRequest request) {
        UserAccount account = AccountUtils.getLoginUserAccount();
        //reload user
        account = accountService.findByUserId(account.getUserId());
        account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        uiModel.addAttribute("userAccount", account);
        
        //check duplicate connection error
        if (request.getParameter("social.addConnection.duplicate") != null){
            uiModel.addAttribute("duplicateConnectionError", Boolean.TRUE);
        }
        
        return "myAccount/overview";
    }

    @RequestMapping(value = "/editProfile", produces = "text/html")
    public String editProfileForm(Model uiModel) {
        logger.debug("==>MyAccountController.editProfileForm()");
        UserAccount account = AccountUtils.getLoginUserAccount();
        //reload user
        account = accountService.findByUserId(account.getUserId());
        uiModel.addAttribute("userAccount", account);
        uiModel.addAttribute("profileForm", new ProfileForm(account));
        return "myAccount/editProfile";
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.PUT, produces = "text/html")
    public String editProfile(@ModelAttribute("profileForm") @Valid ProfileForm profileForm,
                    BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("userAccount", AccountUtils.getLoginUserAccount());
            uiModel.addAttribute("profileForm", profileForm);
            return "myAccount/editProfile";
        }
        
        UserAccount account = AccountUtils.getLoginUserAccount();
        if (!account.getUserId().equals(profileForm.getUserId())){
            logger.warn("Update other user's profile by logged in user "+account.getUserId());
            return "redirect:/myAccount";
        }
        uiModel.asMap().clear();

        try {
            if (account.isAdmin()){
                account = accountService.updateProfile(profileForm.getUserId(), profileForm.getDisplayName(),
                            profileForm.getEmail(), profileForm.getWebSite(), profileForm.getImageUrl());
            } else {
                account = accountService.updateProfile(profileForm.getUserId(), profileForm.getDisplayName(),
                        profileForm.getEmail(), profileForm.getWebSite());
            }
            AccountUtils.getLoginUserAccount().setDisplayName(profileForm.getDisplayName()); //update display name. TODO refresh logged in user?
        } catch (UsernameNotFoundException ex) {
            logger.error("Something wrong, update profile with invalid account " + profileForm.getUserId());
            // TODO ? re-authenticate the user ?
            return "redirect:/";
        }
        return "redirect:/myAccount";
    }
    
    @RequestMapping(value = "/useGoogleImage", method = RequestMethod.GET, produces = "text/html")
    public String useGoogleImage(Model uiModel) {
        UserAccount account = AccountUtils.getLoginUserAccount();
        logger.debug("==>MyAccountController.useGoogleImage(), userId=" + account.getUserId());
        account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        if (account.isHasGoogleConnection()){
            accountService.updateImageUrl(account.getUserId(), account.getGoogleConnection().getImageUrl());
        }
        return "redirect:/myAccount";
    }

    @RequestMapping(value = "/useTwitterImage", method = RequestMethod.GET, produces = "text/html")
    public String useTwitterImage(Model uiModel) {
        UserAccount account = AccountUtils.getLoginUserAccount();
        logger.debug("==>MyAccountController.useTwitterImage(), userId=" + account.getUserId());
        account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        if (account.isHasTwitterConnection()){
            accountService.updateImageUrl(account.getUserId(), account.getTwitterConnection().getImageUrl());
        }
        return "redirect:/myAccount";
    }

    @RequestMapping(value = "/useFacebookImage", method = RequestMethod.GET, produces = "text/html")
    public String useFacebookImage(Model uiModel) {
        UserAccount account = AccountUtils.getLoginUserAccount();
        logger.debug("==>MyAccountController.useFacebookImage(), userId=" + account.getUserId());
        account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        if (account.isHasFacebookConnection()){
            accountService.updateImageUrl(account.getUserId(), account.getFacebookConnection().getImageUrl());
        }
        return "redirect:/myAccount";
    }

    @RequestMapping(value = "/myComments" , method = RequestMethod.GET, produces = "text/html")
    public String listComments(Model uiModel, Pageable pageable) {
        UserAccount account = AccountUtils.getLoginUserAccount();
        logger.debug("==>MyAccountController.listComments(), userId=" + account.getUserId());
        PageWrapper<CommentPost> page = new PageWrapper<CommentPost>(
                commentPostService.getAllCommentsForUser(account.getUserId(), pageable), "/myAccount/myComments");
        uiModel.addAttribute("page", page);
        return "myAccount/myComments";
    }
    
    @RequestMapping(value = "/updateComment/{id}", method = RequestMethod.GET, produces = "text/html")
    public String updateCommentForm(@PathVariable("id") String id, Model uiModel) {
        uiModel.addAttribute("commentForm", new CommentForm(commentPostService.getCommentById(id)));
        return "myAccount/editComment";
    }

    @RequestMapping(value = "/updateComment", method = RequestMethod.PUT, produces = "text/html")
    public String updateComment(@ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        logger.debug("==>MyAccountController.updateComment(), comment id=" + commentForm.getId());
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("commentForm", commentForm);
            return "myAccount/editComment";
        }
        try{
            commentPostService.updateComment(commentForm.getId(), commentForm.getContent());
            return "redirect:/myAccount/myComments";
        } catch (Exception ex){
            uiModel.addAttribute("message", new Message(MessageType.ERROR, "Error while update comment: "+ex.getMessage()));
            uiModel.addAttribute("commentForm", commentForm);
            return "myAccount/editComment";
        }
    }

}
