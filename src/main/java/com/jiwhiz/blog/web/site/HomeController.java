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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.message.Message;
import com.jiwhiz.blog.message.MessageType;
import com.jiwhiz.blog.web.AbstractPublicPageController;

/**
 * Controller for home page, about page, profile page, etc.
 * 
 * @author Yuan Ji
 *
 */
@Controller
public class HomeController extends AbstractPublicPageController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController() {
    }

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model uiModel, HttpServletRequest request) {
        counterService.logVisit();
        List<BlogPost> blogPosts = blogPostService.getPublishedPosts(1);
        if (blogPosts.size() > 0) {
            uiModel.addAttribute("latestBlog", blogPosts.get(0));
        } else {
            uiModel.addAttribute("latestBlog", null);
            uiModel.addAttribute("message", 
                    new Message(MessageType.INFO, "No blog post yet. Stay tuned..."));

        }

        return "site/home";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model uiModel, HttpServletRequest request) {
        counterService.logVisit();
        return "site/about";
    }

    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public String profile(@PathVariable("userId") String userId, Model uiModel, HttpServletRequest request) {
        counterService.logVisit();
        UserAccount account = this.accountService.findByUserId(userId);
        if (account == null){
            logger.warn("Access profle page with wrong userId : "+userId);
            return "resourceNotFound";
        }
        account.setConnections(accountService.getConnectionsByUserId(account.getUserId()));
        
        uiModel.addAttribute("profileUser", account);
        uiModel.addAttribute("comments", this.commentPostService.getPublishedCommentsForUser(userId));
        
        return "site/userProfile";
    }

    @RequestMapping(value="/signin", method=RequestMethod.GET)
    public String signin(Model uiModel) {
        return "site/signin";
    }
    
    /**
     * Handle exceptions.
     */
    @RequestMapping(value = "/uncaughtException", method = RequestMethod.GET)
    public String uncaughtException(Model uiModel) {
        return "site/uncaughtException";
    }

    @RequestMapping(value = "/resourceNotFound", method = RequestMethod.GET)
    public String resourceNotFound(Model uiModel) {
        return "site/resourceNotFound";
    }

}
