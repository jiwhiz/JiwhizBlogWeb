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

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.account.UserSocialConnection;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.web.dto.UserAccountDTO;

/**
 * RESTful Service for current logged in user account management.
 * 
 * <p>
 * API: '<b>rest/myAccount/:action</b>'
 * </p>
 * <p>
 * <b>:action</b> can be
 * <ul>
 * <li>'overview' - return current logged in user account data, with social connections and all comments.</li>
 * <li>'profile' - retrieve or update logged in user profile.</li>
 * <li>'useSocialImage' - change user profile image to social account image.
 * </ul>
 * </p>
 * 
 * @author Yuan Ji
 * 
 */
@Controller
@RequestMapping("/rest/myAccount")
public class MyAccountRestService {
    private static final Logger logger = LoggerFactory.getLogger(MyAccountRestService.class);

    private final UserAccountRepository userAccountRepository;
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    private final UserAccountService userAccountService;

    @Inject
    public MyAccountRestService(UserAccountRepository userAccountRepository,
            UserSocialConnectionRepository userSocialConnectionRepository, UserAccountService userAccountService) {
        this.userAccountRepository = userAccountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userAccountService = userAccountService;
    }

    /**
     * Return current logged in user @{link UserAccountDTO} data, with connections of @{link SocialConnectionDTO} and
     * comments of @{link CommentPostDTO}
     * 
     * @return
     */
    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    @ResponseBody
    public UserAccountDTO getCurrentUserAccount() {
        UserAccount currentUser = userAccountService.getCurrentUser();
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(currentUser.getUserId());
        return UserAccountDTO.transferForAccountOverview(currentUser, connections);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    public UserAccountDTO getProfile() {
        UserAccount currentUser = userAccountService.getCurrentUser();
        return UserAccountDTO.transferForProfileUpdate(currentUser);
    }

    /**
     * Update logged in user profile data.
     * 
     * @param profileForm
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@RequestBody UserAccountDTO profile) {
        UserAccount currentUser = userAccountService.getCurrentUser();
        currentUser.updateProfile(profile.getDisplayName(), profile.getEmail(), profile.getWebSite());
        userAccountRepository.save(currentUser);
    }

    @RequestMapping(value = "/useSocialImage", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO useSocialImage(@RequestParam("provider") String provider) {
        UserAccount currentUser = userAccountService.getCurrentUser();
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(currentUser.getUserId());

        for (UserSocialConnection connection : connections) {
            if (connection.getProviderId().equals(provider)) {
                currentUser.setImageUrl(connection.getImageUrl());
                logger.debug("Change user image to social account image of " + provider);
                break;
            }
        }
        currentUser = userAccountRepository.save(currentUser);
        return UserAccountDTO.transferForAccountOverview(currentUser, connections);
    }

}
