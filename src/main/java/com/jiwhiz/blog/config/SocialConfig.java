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
package com.jiwhiz.blog.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.provider.OAuth1AuthenticationService;
import org.springframework.social.security.provider.OAuth2AuthenticationService;
import org.springframework.social.security.provider.SocialAuthenticationService;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.jiwhiz.blog.domain.account.AccountService;
import com.jiwhiz.blog.domain.account.AccountUtils;
import com.jiwhiz.blog.domain.account.MongoUsersConnectionRepositoryImpl;
import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.account.UserSocialConnectionRepository;
import com.jiwhiz.blog.web.AutoConnectionSignUp;

/**
 * Configuration for Spring Social.
 * 
 * @author Yuan Ji
 *
 */
@Configuration
public class SocialConfig {

    @Inject
    private Environment environment;

    @Inject
    private AccountService accountService;

    @Inject 
    private UserAdminService userAdminService;
        
    @Inject
    private UserSocialConnectionRepository userSocialConnectionRepository;

    /**
     * When a new provider is added to the app, register its {@link SocialAuthenticationService} here.
     * 
     */
    @Bean
    public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
        SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
        
        //add google
        OAuth2ConnectionFactory<Google> googleConnectionFactory = new GoogleConnectionFactory(environment.getProperty("google.clientId"),
                environment.getProperty("google.clientSecret"));
        OAuth2AuthenticationService<Google> googleAuthenticationService = new OAuth2AuthenticationService<Google>(googleConnectionFactory);
        googleAuthenticationService.setScope("https://www.googleapis.com/auth/userinfo.profile"); //get basic info only.
        registry.addAuthenticationService(googleAuthenticationService);

        //add twitter
        OAuth1ConnectionFactory<Twitter> twitterConnectionFactory = new TwitterConnectionFactory(environment.getProperty("twitter.consumerKey"),
                environment.getProperty("twitter.consumerSecret"));
        OAuth1AuthenticationService<Twitter> twitterAuthenticationService = new OAuth1AuthenticationService<Twitter>(twitterConnectionFactory);
        registry.addAuthenticationService(twitterAuthenticationService);

        //add facebook
        OAuth2ConnectionFactory<Facebook> facebookConnectionFactory = new FacebookConnectionFactory(environment.getProperty("facebook.clientId"),
                environment.getProperty("facebook.clientSecret"));
        OAuth2AuthenticationService<Facebook> facebookAuthenticationService = new OAuth2AuthenticationService<Facebook>(facebookConnectionFactory);
        facebookAuthenticationService.setScope(""); //???
        registry.addAuthenticationService(facebookAuthenticationService);

        return registry;
    }

    /**
     * Singleton data access object providing access to connections across all users.
     */
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        MongoUsersConnectionRepositoryImpl repository = new MongoUsersConnectionRepositoryImpl(userSocialConnectionRepository,
                socialAuthenticationServiceLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(autoConnectionSignUp());
        return repository;
    }

    /**
     * Request-scoped data access object providing access to the current user's connections.
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        UserAccount user = AccountUtils.getLoginUserAccount();
        return usersConnectionRepository().createConnectionRepository(user.getUsername());
    }

    /**
     * A proxy to a request-scoped object representing the current user's primary Google account.
     * 
     * @throws NotConnectedException
     *             if the user is not connected to Google.
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google() {
        Connection<Google> google = connectionRepository().findPrimaryConnection(Google.class);
        return google != null ? google.getApi() : new GoogleTemplate();
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)   
    public Facebook facebook() {
        Connection<Facebook> facebook = connectionRepository().findPrimaryConnection(Facebook.class);
        return facebook != null ? facebook.getApi() : new FacebookTemplate();
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)   
    public Twitter twitter() {
        Connection<Twitter> twitter = connectionRepository().findPrimaryConnection(Twitter.class);
        return twitter != null ? twitter.getApi() : new TwitterTemplate();
    }

    @Bean
    public ConnectionSignUp autoConnectionSignUp() {
        return new AutoConnectionSignUp(accountService);
    }


}
