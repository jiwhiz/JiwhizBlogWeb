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
package com.jiwhiz.blog.web.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserSocialConnection;

public class UserAccountDTO {
    private String userId;

    private String displayName;

    private String imageUrl;

    private String webSite;

    private boolean admin;

    private boolean author;
    
    private boolean accountLocked;
    
    private boolean trustedAccount;
    
    private String email;

    private Map<String, SocialConnectionDTO> socialConnections;
    
    private List<CommentPostDTO> comments;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, SocialConnectionDTO> getSocialConnections() {
        return socialConnections;
    }

    public List<CommentPostDTO> getComments() {
        return comments;
    }
    
    public void setComments(List<CommentPostDTO> comments) {
        this.comments = comments;
    }

    public UserAccountDTO() {}
    
    public UserAccountDTO(UserAccount userAccount) {
        this(userAccount, false);
    }
    
    public UserAccountDTO(UserAccount userAccount, boolean admin) {
        this.userId = userAccount.getUserId();
        this.displayName = userAccount.getDisplayName();
        this.imageUrl = userAccount.getImageUrl();
        this.webSite = userAccount.getWebSite();
        this.admin = userAccount.isAdmin();
        this.author = userAccount.isAuthor();
        if (admin) {
            this.trustedAccount = userAccount.isTrustedAccount();
            this.accountLocked = userAccount.isAccountLocked();
            this.email = userAccount.getEmail();
        }
    }

    public void setSocialConnections(List<UserSocialConnection> connections) {
        if (connections != null) {
            this.socialConnections = new HashMap<String, SocialConnectionDTO>();
            for (UserSocialConnection connection : connections) {
                this.socialConnections.put(connection.getProviderId(), new SocialConnectionDTO(connection));
            }
        }
    }
    
    public static UserAccountDTO transferForAccountOverview(UserAccount userAccount, 
            List<UserSocialConnection> connections) {
        UserAccountDTO userAccountDto = new UserAccountDTO(userAccount);
        userAccountDto.setTrustedAccount(userAccount.isTrustedAccount());
        userAccountDto.setAccountLocked(userAccount.isAccountLocked());
        userAccountDto.setEmail(userAccount.getEmail());
        userAccountDto.setSocialConnections(connections);
        return userAccountDto;
    }
 
//    public static UserAccountDTO transferForPublicProfile(UserAccount userAccount, 
//            List<UserSocialConnection> connections, List<CommentPost> userComments) {
//        UserAccountDTO userAccountDto = new UserAccountDTO(userAccount);
//        userAccountDto.setSocialConnections(connections);
//        userAccountDto.setComments(userComments);
//        return userAccountDto;
//    }
    
    public static UserAccountDTO transferForProfileUpdate(UserAccount userAccount) {
        UserAccountDTO userAccountDto = new UserAccountDTO(userAccount);
        userAccountDto.setEmail(userAccount.getEmail());
        return userAccountDto;
    }
    
    public static UserAccountDTO transferFor(UserAccount userAccount) {
        UserAccountDTO userAccountDto = new UserAccountDTO(userAccount);
        userAccountDto.setEmail(userAccount.getEmail());
        return userAccountDto;
    }

}

