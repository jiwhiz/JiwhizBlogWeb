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
package com.jiwhiz.domain.account;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import com.jiwhiz.domain.BaseAuditableEntity;

/**
 * Domain Entity for user account.
 * 
 * @author Yuan Ji
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "UserAccount")
public class UserAccount extends BaseAuditableEntity implements SocialUserDetails {
    
    @Indexed(unique=true)
    private String userId;
    
    private UserRoleType[] roles;
    
    private String email;
    
    private String displayName;
    
    private String imageUrl;
    
    private String webSite;
    
    private boolean accountLocked;
    
    private boolean trustedAccount;

    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }

    public UserRoleType[] getRoles() {
        return roles;
    }

    public void setRoles(UserRoleType[] roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isTrustedAccount() {
        return trustedAccount;
    }

    public void setTrustedAccount(boolean trustedAccount) {
        this.trustedAccount = trustedAccount;
    }

    public UserAccount() {
        this.roles = new UserRoleType[0];
    }
    
    public UserAccount(String userId, UserRoleType[] roles) {
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        // No password stored
        return null;
    }

    @Override
    public String getUsername() {
        return getUserId();
    }
    
    public boolean isAuthor(){
        for (UserRoleType role : getRoles()) {
            if (role == UserRoleType.ROLE_AUTHOR){
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(){
        for (UserRoleType role : getRoles()) {
            if (role == UserRoleType.ROLE_ADMIN){
                return true;
            }
        }        
        return false;
    }

    public void updateProfile(String displayName, String email, String webSite){
        setDisplayName(displayName);
        setEmail(email);
        setWebSite(webSite);
    }
    
    @Override
    public String toString() {
        String str = String.format("UserAccount{userId:'%s'; displayName:'%s';roles:[", getUserId(), getDisplayName());
        for (UserRoleType role : getRoles()) {
            str += role.toString() + ",";
        }
        return str + "]}";
    }

}
