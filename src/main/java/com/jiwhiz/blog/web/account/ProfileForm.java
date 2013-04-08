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
package com.jiwhiz.blog.web.account;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.jiwhiz.blog.domain.account.UserAccount;

/**
 * 
 * @author Yuan Ji
 * 
 */
public class ProfileForm {
    private String userId;

    @NotEmpty
    private String displayName;

    @Email
    private String email;

    private String webSite;

    private String imageUrl;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the webSite
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * @param webSite
     *            the webSite to set
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl
     *            the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProfileForm() {

    }

    public ProfileForm(UserAccount account) {
        this.userId = account.getUserId();
        this.displayName = account.getDisplayName();
        this.email = account.getEmail();
        this.webSite = account.getWebSite();
        this.imageUrl = account.getImageUrl();
    }

}
