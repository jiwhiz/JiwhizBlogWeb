/* 
 * Copyright 2013-2015 JIWHIZ Consulting Inc.
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
package com.jiwhiz.rest.user;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;

/**
 * @author Yuan Ji
 */
@Controller
public class UserAccountRestController extends AbstractUserRestController {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountResourceAssembler userAccountResourceAssembler;

    @Inject
    public UserAccountRestController(
            UserAccountService userAccountService, 
            UserAccountRepository userAccountRepository,
            UserAccountResourceAssembler userAccountResourceAssembler) {
        super(userAccountService);
        this.userAccountRepository = userAccountRepository;
        this.userAccountResourceAssembler = userAccountResourceAssembler;
    }

    /**
     * Get current user info. If not authenticated, return 401.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_USER)
    public HttpEntity<UserAccountResource> getCurrentUserAccount() {
        return new ResponseEntity<>(userAccountResourceAssembler.toResource(getCurrentAuthenticatedUser()), HttpStatus.OK);
    }

    /**
     * Updates current user profile, like name, email, website, imageUrl.
     * 
     * @param updateMap
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(method = RequestMethod.PATCH, value = ApiUrls.URL_USER_PROFILE)
    public HttpEntity<Void> patchUserProfile(@RequestBody Map<String, String> updateMap) throws ResourceNotFoundException {
        UserAccount currentUser = getCurrentAuthenticatedUser();
        String displayName = updateMap.get("displayName");
        if (displayName != null) {
            currentUser.setDisplayName(displayName);
        }
        String email = updateMap.get("email");
        if (email != null) {
            currentUser.setEmail(email);
        }
        String webSite = updateMap.get("webSite");
        if (webSite != null) {
            currentUser.setWebSite(webSite);
        }
        String imageUrl = updateMap.get("imageUrl");
        if (imageUrl != null) {
            currentUser.setImageUrl(imageUrl);
        }        
        userAccountRepository.save(currentUser);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
