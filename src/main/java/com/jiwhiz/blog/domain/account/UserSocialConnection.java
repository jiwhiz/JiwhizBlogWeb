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

import org.springframework.data.mongodb.core.mapping.Document;

import com.jiwhiz.blog.domain.BaseEntity;

/**
 * Domain Entity for user social connection.
 * 
 * @author Yuan Ji
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "UserSocialConnection")
public class UserSocialConnection extends BaseEntity {
    private String userId;
    private String providerId;
    private String providerUserId;
    private String displayName;
    private String profileUrl;
    private String imageUrl;
    private String accessToken;
    private String secret;
    private String refreshToken;
    private Long expireTime;

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
     * @return the providerId
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId
     *            the providerId to set
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the providerUserId
     */
    public String getProviderUserId() {
        return providerUserId;
    }

    /**
     * @param providerUserId
     *            the providerUserId to set
     */
    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
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
     * @return the profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * @param profileUrl
     *            the profileUrl to set
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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

    /**
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken
     *            the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret
     *            the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return the refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken
     *            the refreshToken to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * @return the expireTime
     */
    public Long getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime
     *            the expireTime to set
     */
    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public UserSocialConnection() {
        super();
    }

    public UserSocialConnection(String userId, String providerId, String providerUserId, int rank,
            String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
            String refreshToken, Long expireTime) {
        super();
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public String toString() {
        return String.format(
                "UserSocialConnection { userId : '%s', providerId : '%s', providerUserId : '%s', displayName : '%s'",
                userId, providerId, providerUserId, displayName);
    }
}
