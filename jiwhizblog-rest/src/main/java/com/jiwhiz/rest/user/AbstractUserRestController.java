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
package com.jiwhiz.rest.user;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
@RequestMapping( value = ApiUrls.API_ROOT, produces = "application/hal+json" )
public class AbstractUserRestController {
    private final UserAccountService userAccountService;
    
    @Inject
    public AbstractUserRestController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    protected UserAccount getCurrentAuthenticatedUser() {
        UserAccount currentUser = this.userAccountService.getCurrentUser();
        if (currentUser == null) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in.");
        }
        return currentUser;
    }

}
