/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
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
package com.jiwhiz.rest.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.account.UserRoleType;
import com.jiwhiz.domain.account.UserSocialConnection;
import com.jiwhiz.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.rest.ApiUrls;
import com.jiwhiz.rest.ResourceNotFoundException;
import com.jiwhiz.rest.UtilConstants;

/**
 * @author Yuan Ji
 */
@Controller
public class UserRestController extends AbstractAdminRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    
    private final UserAccountRepository userAccountRepository;
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    private final UserResourceAssembler userResourceAssembler;

    @Inject
    public UserRestController(
            UserAccountService userAccountService, 
            UserAccountRepository userAccountRepository,
            UserSocialConnectionRepository userSocialConnectionRepository,
            UserResourceAssembler userResourceAssembler) {
        super(userAccountService);
        this.userAccountRepository = userAccountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userResourceAssembler = userResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_USERS)
    public HttpEntity<PagedResources<UserResource>> getUserAccounts(
            @PageableDefault(size = UtilConstants.DEFAULT_RETURN_RECORD_COUNT, 
                             page = 0, sort="createdTime") Pageable pageable,
            PagedResourcesAssembler<UserAccount> assembler) {
        LOGGER.debug("==>AdminUserRestController.getUserAccounts()");
        
        Page<UserAccount> userAccounts = this.userAccountRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(userAccounts, userResourceAssembler), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_USERS_USER) 
    public HttpEntity<UserResource> getUserAccountByUserId(
            @PathVariable("userId") String userId) 
            throws ResourceNotFoundException {
        LOGGER.debug("==>AdminUserRestController.getUserAccountByUserId()");
        UserAccount userAccount = getUserByUserId(userId);
        return new ResponseEntity<>(userResourceAssembler.toResource(userAccount), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN_USERS_USER_SOCIAL_CONNECTIONS)
    public HttpEntity<Map<String, UserSocialConnection>> getUserSocialConnections(
            @PathVariable("userId") String userId) throws ResourceNotFoundException {
        LOGGER.debug("==>AdminUserRestController.getUserSocialConnections()");
        
        UserAccount user = getUserByUserId(userId);
        List<UserSocialConnection> socialConnections = userSocialConnectionRepository.findByUserId(user.getUserId());
        Map<String, UserSocialConnection> result = new HashMap<String, UserSocialConnection>();
        for (UserSocialConnection socialConnection : socialConnections) {
            result.put(socialConnection.getProviderId(), socialConnection);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    
    @RequestMapping(method = RequestMethod.PATCH, value = ApiUrls.URL_ADMIN_USERS_USER)
    public HttpEntity<Void> updateUserAccount(@PathVariable("userId") String userId, 
            @RequestBody Map<String, String> updateMap) 
            throws ResourceNotFoundException {
        String command = updateMap.get("command");
        LOGGER.info("==>AdminUserRestController.updateUserAccount() with " + command);
        UserAccount user = getUserByUserId(userId);
        
        if ("lock".equals(command)) {
            user.setAccountLocked(true);
            userAccountRepository.save(user);
        } else if ("unlock".equals(command)) {
            user.setAccountLocked(false);
            userAccountRepository.save(user);
        } else if ("trust".equals(command)) {
            user.setTrustedAccount(true);
            userAccountRepository.save(user);
        } else if ("untrust".equals(command)) {
            user.setTrustedAccount(false);
            userAccountRepository.save(user);
        } else if ("addAuthorRole".equals(command)) {
            userAccountService.addRole(userId, UserRoleType.ROLE_AUTHOR);
        } else if ("removeAuthorRole".equals(command)) {
            userAccountService.removeRole(userId, UserRoleType.ROLE_AUTHOR);
        }

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private UserAccount getUserByUserId(String userId) throws ResourceNotFoundException {
        UserAccount user = userAccountRepository.findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No such user for id "+user);
        }
        return user;
    }

}
