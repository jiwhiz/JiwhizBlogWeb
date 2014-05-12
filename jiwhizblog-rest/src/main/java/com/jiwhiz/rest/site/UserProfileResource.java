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
package com.jiwhiz.rest.site;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.ResourceSupport;

import com.jiwhiz.domain.account.UserAccount;

/**
 * @author Yuan Ji
 */
public class UserProfileResource extends ResourceSupport {
    public static final String LINK_NAME_COMMENTS = "comments";
    
    private String userId;
    
    private String displayName;

    private String imageUrl;

    private String webSite;

    private boolean admin;

    private boolean author;
    
    private boolean accountLocked;
    
    private boolean trustedAccount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
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

    public UserProfileResource() {
    }
    
    public UserProfileResource(UserAccount account) {
        BeanUtils.copyProperties(account, this);
    }

}
