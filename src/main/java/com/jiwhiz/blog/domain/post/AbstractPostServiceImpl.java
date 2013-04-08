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
package com.jiwhiz.blog.domain.post;

import javax.inject.Inject;

import org.springframework.security.access.AccessDeniedException;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Super class for all post service implementation classes.
 * 
 * @author Yuan Ji
 *
 */
public abstract class AbstractPostServiceImpl {
    
    protected final UserAccountRepository accountRepository;
    protected final UserAdminService userAdminService;
    protected final CounterService counterService;

    @Inject
    public AbstractPostServiceImpl(UserAccountRepository accountRepository, UserAdminService userAdminService, CounterService counterService) {
        this.accountRepository = accountRepository;
        this.userAdminService = userAdminService;
        this.counterService = counterService;
    }
    
    protected void loadAuthorProfile(Iterable<? extends AbstractPost> postList) {
        for (AbstractPost post : postList) {
            loadAuthorProfile(post);
        }
    }

    protected void loadAuthorProfile(AbstractPost post) {
        if (post != null){
            post.setAuthorAccount(accountRepository.findByUserId(post.getAuthorId()));
        }
    }

    /**
     * Throws AccessDeniedException if current user is not the author of the post.
     * 
     * @param post
     * @throws AccessDeniedException
     */
    protected void checkIsAuthorOfPost(AbstractPost post) throws AccessDeniedException {
        if (post == null || !post.getAuthorId().equals(userAdminService.getUserId())){
            throw new AccessDeniedException("Cannot access the post by current user.");
        }
    }
    
    /**
     * Throws AccessDeniedException if current user is not admin and not the author of the post.
     * 
     * @param post
     * @throws AccessDeniedException
     */
    protected void checkIsAdminOrAuthorOfPost(AbstractPost post) throws AccessDeniedException{
        UserAccount currentUser = userAdminService.getCurrentUser();
        if (currentUser == null){
            throw new AccessDeniedException("No logged in user.");
        }
        
        if (currentUser.isAdmin()){
            return;
        }
        
        checkIsAuthorOfPost(post);
    }

}
