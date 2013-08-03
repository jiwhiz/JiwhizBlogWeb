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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.account.UserSocialConnection;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.domain.post.BlogPost;
import com.jiwhiz.blog.domain.post.BlogPostRepository;
import com.jiwhiz.blog.domain.post.CommentPost;
import com.jiwhiz.blog.domain.post.CommentPostRepository;
import com.jiwhiz.blog.domain.post.CommentStatusType;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.dto.BlogPostDTO;
import com.jiwhiz.blog.web.dto.CommentPostDTO;
import com.jiwhiz.blog.web.dto.CurrentUser;
import com.jiwhiz.blog.web.dto.UserAccountDTO;

/**
 * RESTful Service for public contact or profile page. Also for retrieving current user info.
 * 
 * <p>
 * API: 'rest/public/currentUser' - get current logged in user info
 * <p>
 * API: 'rest/public/profile/:userId' - get user public profile
 * <p>
 * API: 'rest/contact' - send contact message
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/rest/public")
public class PublicSiteRestService {
    private static final Logger logger = LoggerFactory.getLogger(PublicSiteRestService.class);

    private final UserAccountRepository userAccountRepository;
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    private final BlogPostRepository blogPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final UserAccountService userAccountService;
    private final ContactMessageSender contactMessageSender;

    @Inject
    public PublicSiteRestService(UserAccountRepository userAccountRepository,
            UserSocialConnectionRepository userSocialConnectionRepository, BlogPostRepository blogPostRepository,
            CommentPostRepository commentPostRepository, UserAccountService userAccountService,
            ContactMessageSender contactMessageSender) {
        this.userAccountRepository = userAccountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.blogPostRepository = blogPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.userAccountService = userAccountService;
        this.contactMessageSender = contactMessageSender;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    String handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Got ResourceNotFoundException:" + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public @ResponseBody
    CurrentUser getCurrentUserAccount() {
        logger.debug("==>PublicSiteController.getCurrentUserAccount()");
        UserAccount account = userAccountService.getCurrentUser();
        if (account != null) {
            logger.debug("      user authenticated, name is " + account.getDisplayName());
            return new CurrentUser(userAccountRepository.findByUserId(account.getUserId()));
        }
        logger.debug("      user not logged in!");
        return new CurrentUser();
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    @ResponseBody
    public String submitContactMessage(@RequestBody ContactForm contactForm) {
        logger.debug("==>PublicSiteController.submitContactMessage()");
        logger.debug("contact is " + contactForm);
        contactMessageSender.send(contactForm);
        return "";
    }

    /**
     * Retrieve public profile info by userId.
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public UserAccountDTO getUserProfile(@PathVariable("userId") String userId) {
        UserAccount account = this.userAccountRepository.findByUserId(userId);
        if (account == null) {
            logger.warn("Access profle page with wrong userId : " + userId);
            return null;
        }

        UserAccountDTO userDto = new UserAccountDTO(account);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(account.getUserId());
        userDto.setSocialConnections(connections);
        List<CommentPostDTO> commentDtos = new ArrayList<CommentPostDTO>();
        List<CommentPost> comments = commentPostRepository.findByAuthorKeyAndStatus(account.getKey(),
                CommentStatusType.APPROVED, new Sort(Direction.DESC, "createdTime"));
        for (CommentPost comment : comments) {
            BlogPost blog = blogPostRepository.findOne(comment.getBlogPostKey());
            BlogPostDTO blogDto = new BlogPostDTO(blog, false);
            CommentPostDTO commentDto = new CommentPostDTO(comment);
            commentDto.setBlog(blogDto);
            commentDtos.add(commentDto);
        }
        userDto.setComments(commentDtos);
        return userDto;
    }
}
