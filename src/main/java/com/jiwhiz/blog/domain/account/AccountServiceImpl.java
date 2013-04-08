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
package com.jiwhiz.blog.domain.account;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionData;


/**
 * Implementation for AccountService.
 * 
 * @author Yuan Ji
 * 
 */
public class AccountServiceImpl implements AccountService {
    final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final UserAccountRepository accountRepository;
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    private final UserAdminService userAdminService;

    @Inject
    public AccountServiceImpl(UserAccountRepository accountRepository, UserSocialConnectionRepository 
            userSocialConnectionRepository, UserAdminService userAdminService) {
        this.accountRepository = accountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userAdminService = userAdminService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#findByUsername(java.lang .String)
     */
    @Override
    public UserAccount findByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.account.AccountService#getAllUsers()
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserAccount> getAllUsers() {
        return accountRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.application.AccountService#getAllUsers(org.springframework.data.domain.Pageable)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<UserAccount> getAllUsers(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#getConnectionsByUserId(java.lang.String)
     */
    @Override
    public List<UserSocialConnection> getConnectionsByUserId(String userId){
        return this.userSocialConnectionRepository.findByUserId(userId);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#updateProfile(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserAccount updateProfile(String userId, String displayName, String email, String webSite, String imageUrl)
            throws UsernameNotFoundException {
        UserAccount account = userAdminService.loadUserByUserId(userId);
        account.updateProfile(displayName, email, webSite);
        account.setImageUrl(imageUrl);
        accountRepository.save(account);
        return account;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#updateProfile(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserAccount updateProfile(String userId, String displayName, String email, String webSite)
            throws UsernameNotFoundException {
        UserAccount account = userAdminService.loadUserByUserId(userId);
        account.updateProfile(displayName, email, webSite);
        accountRepository.save(account);
        return account;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#updateImageUrl(java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserAccount updateImageUrl(String userId, String imageUrl)
            throws UsernameNotFoundException {
        UserAccount account = userAdminService.loadUserByUserId(userId);
        account.setImageUrl(imageUrl);
        accountRepository.save(account);
        return account;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.application.AccountService#createUserAccount(org.springframework.social.connect.ConnectionData)
     */
    @Override
    public UserAccount createUserAccount(ConnectionData data) {
        return userAdminService.createUserAccount(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#lockAccount(java.lang.String, boolean)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void lockAccount(String userId, boolean locked) {
        UserAccount account = userAdminService.loadUserByUserId(userId);
            account.setAccountLocked(locked);
            this.accountRepository.save(account);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#trustAccount(java.lang.String, boolean)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void trustAccount(String userId, boolean trusted) {
        UserAccount account = userAdminService.loadUserByUserId(userId);
            account.setTrustedAccount(trusted);
            this.accountRepository.save(account);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jiwhiz.blog.domain.account.AccountService#setAuthor(java.lang.String, boolean)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setAuthor(String userId, boolean isAuthor) {
        userAdminService.setAuthor(userId, isAuthor);
    }
}
