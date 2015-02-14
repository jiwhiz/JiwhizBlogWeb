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
package com.jiwhiz.rest.admin;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.rest.ApiUrls;

/**
 * Super class for all admin related rest controller classes.
 * 
 * @author Yuan Ji
 */
@RequestMapping( value = ApiUrls.API_ROOT, produces = "application/hal+json" )
public abstract class AbstractAdminRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdminRestController.class);
    
    protected final UserAccountService userAccountService;
    
    @Inject
    public AbstractAdminRestController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    protected UserAccount getCurrentAuthenticatedAdmin() {
        UserAccount currentUser = this.userAccountService.getCurrentUser();
        if (currentUser == null || !currentUser.isAdmin()) {
            //ERROR! Should not happen.
            LOGGER.error("Fatal Error! Unauthorized data access!");
            throw new AccessDeniedException("User not logged in or not ADMIN.");
        }
        return currentUser;
    }

}
