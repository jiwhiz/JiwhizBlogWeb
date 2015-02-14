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
package com.jiwhiz.rest.user;

import org.springframework.beans.BeanUtils;

import com.jiwhiz.domain.account.UserAccount;

/**
 * @author Yuan Ji
 */
public class ProfileForm {
    private String displayName;
    private String email;
    private String imageUrl;
    private String webSite;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ProfileForm() {
        
    }
    
    public ProfileForm(UserAccount account) {
        BeanUtils.copyProperties(account, this);
    }
    
    public String toString() {
        return String.format("ProfileForm { displayName : '%s', email : '%s', webSite : '%s'}", displayName, email, webSite);
    }
}
