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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAccountService;
import com.jiwhiz.blog.domain.account.UserSocialConnection;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.web.dto.UserAccountDTO;


/**
 * RESTful Service for adim user managing system users.
 * 
 * <p>API: '<b>rest/admin/users/:action:userId/:userAction</b>'</p>
 * <p><b>:action</b> can be
 * <ul>
 * <li>'list' - return all users in the system.</li>
 * </ul>
 * </p>
 * <p><b>:userAction</b> can be
 * <ul>
 * <li>'lock' - lock user account.</li>
 * <li>'unlock' - unlock user account.</li>
 * <li>'trust' - set user as trusted.</li>
 * <li>'untrust' - set user as not trusted.</li>
 * <li>'addAuthorRole' - add AUTHOR role to user.</li>
 * <li>'removeAuthorRole' - remove AUTHOR role from user.</li>
 * </ul>
 * </p>
 * 
 * @author Yuan Ji
 *
 */
@Controller
@RequestMapping("/rest/admin/users")
public class ManageUserRestService {
	private static final Logger logger = LoggerFactory.getLogger(ManageUserRestService.class);
	
	private final UserAccountRepository userAccountRepository;
	private final UserSocialConnectionRepository userSocialConnectionRepository;
	private final UserAccountService userAccountService;
	
    @Inject
    public ManageUserRestService(UserAccountRepository userAccountRepository,
            UserSocialConnectionRepository userSocialConnectionRepository, UserAccountService userAccountService) {
        this.userAccountRepository = userAccountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userAccountService = userAccountService;
    }

    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    @ResponseBody
    public List<UserAccountDTO> listUsers() {
    	logger.debug("==>ManageUserController.listUsers()");
    	List<UserAccountDTO> result = new ArrayList<UserAccountDTO>();
        List<UserAccount> users = userAccountRepository.findAll(new Sort(Direction.ASC, "userId"));
        
        for (UserAccount userAccount : users){
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            result.add(UserAccountDTO.transferForAccountOverview(userAccount, connections)); 
        }
        return result;
    }

    @RequestMapping(value = "/{userId}/lock", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO lockUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount.setAccountLocked(true);
            userAccount = userAccountRepository.save(userAccount);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

    @RequestMapping(value = "/{userId}/unlock", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO unlockUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount.setAccountLocked(false);
            userAccount = userAccountRepository.save(userAccount);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

    @RequestMapping(value = "/{userId}/trust", method = RequestMethod.PUT)
    @ResponseBody
    public  UserAccountDTO trustUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount.setTrustedAccount(true);
            userAccount = userAccountRepository.save(userAccount);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

    @RequestMapping(value = "/{userId}/untrust", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO untrustUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount.setTrustedAccount(false);
            userAccount = userAccountRepository.save(userAccount);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

    @RequestMapping(value = "/{userId}/addAuthorRole", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO addAuthorRole(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount = userAccountService.setAuthor(userId, true);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

    @RequestMapping(value = "/{userId}/removeAuthorRole", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountDTO removeAuthorRole(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        if (userAccount != null) {
            userAccount = userAccountService.setAuthor(userId, false);
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            return UserAccountDTO.transferForAccountOverview(userAccount, connections);
        }
        return null; //throw exception???
    }

}
