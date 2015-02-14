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

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.jiwhiz.domain.account.UserAccount;
import com.jiwhiz.domain.account.UserAccountRepository;
import com.jiwhiz.rest.AbstractRestControllerTest;
import com.jiwhiz.rest.ApiUrls;

/**
 * @author Yuan Ji
 */
public class UserRestControllerTest extends AbstractRestControllerTest {
    @Inject
    UserAccountRepository userAccountRepositoryMock;
    
    @Before
    public void setup() {
        Mockito.reset(userAccountRepositoryMock);
        super.setup();
    }

    
    @Test
    public void getUserAccounts_ShouldReturnAllUserAccounts() throws Exception {
        Page<UserAccount> page = new PageImpl<UserAccount>(getTestUserAccountList() , new PageRequest(0, 10), 1);
        
        when(userAccountRepositoryMock.findAll(any(Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_USERS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.userAccountList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.userAccountList[0].userId", is (USERS_1_USER_ID)))
                .andExpect(jsonPath("$._embedded.userAccountList[1].userId", is (USERS_2_USER_ID)))
                .andExpect(jsonPath("$._links.self.templated", is(true)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_USERS + "{?page,size,sort}")))
                ;
    }

    @Test
    public void getUserAccountById_ShouldReturnUserAccount() throws Exception {
        UserAccount user = getTestLoggedInUser();
        when(userAccountRepositoryMock.findByUserId(USER_ID)).thenReturn(user);
        
        mockMvc.perform(get(ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_USERS_USER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userId", is (USER_ID)))
                .andExpect(jsonPath("$.displayName", is (USER_DISPLAY_NAME)))
                .andExpect(jsonPath("$._links.self.href", endsWith(ApiUrls.URL_ADMIN_USERS+"/"+USER_ID)))
                .andExpect(jsonPath("$._links.profile.href", endsWith("public/profiles/"+USER_ID)))
                .andExpect(jsonPath("$._links.socialConnections.href", endsWith("admin/users/"+USER_ID+"/socialConnections")))
                ;
    }
    
    @Test
    public void updateUserAccount_Lock_ShouldLockUserAccount() throws Exception {
        UserAccount user = getTestLoggedInUser();
        user.setAccountLocked(false);
        
        when(userAccountRepositoryMock.findByUserId(USER_ID)).thenReturn(user);
        
        mockMvc.perform
                (request(HttpMethod.PATCH, ApiUrls.API_ROOT + ApiUrls.URL_ADMIN_USERS_USER, USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"command\" : \"lock\"}")
                )
                .andExpect(status().isNoContent());
        
        Assert.assertTrue(user.isAccountLocked());
    }

}
