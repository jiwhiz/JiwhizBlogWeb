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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.rest.ApiUrls;

/**
 * RESTful API for AdminAccountResource.
 * 
 * @author Yuan Ji
 */
@Controller
public class AdminAccountRestController extends AbstractAdminRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAccountRestController.class);

    private final AdminAccountResourceAssembler adminAccountResourceAssembler;
    
    @Inject
    public AdminAccountRestController(
            UserAccountService userAccountService,
            AdminAccountResourceAssembler adminAccountResourceAssembler) {
        super(userAccountService);
        this.adminAccountResourceAssembler = adminAccountResourceAssembler;
    }

    /**
     * Get admin info, which contains role, links to other api.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ApiUrls.URL_ADMIN)
    public HttpEntity<AdminAccountResource> getAdminAccount() {
        LOGGER.debug("==>SystemAdminRestController.getAdminInfo()");
        
        UserAccount currentUser = getCurrentAuthenticatedAdmin();
        AdminAccountResource resource = adminAccountResourceAssembler.toResource(currentUser);
        
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
