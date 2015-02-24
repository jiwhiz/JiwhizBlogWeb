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
package com.jiwhiz.web.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import com.jiwhiz.domain.account.RememberMeTokenRepository;
import com.jiwhiz.domain.account.UserAccountService;
import com.jiwhiz.domain.account.UserRoleType;
import com.jiwhiz.domain.account.impl.MongoPersistentTokenRepositoryImpl;

/**
 * Configuration for Spring Security.
 * 
 * @author Yuan Ji
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private Environment environment;

    @Inject 
    private UserAccountService userAccountService;
    
    @Inject
    private RememberMeTokenRepository rememberMeTokenRepository;
    
    @Inject
    private UsersConnectionRepository usersConnectionRepository;
    
    @Inject
    private SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;
    
    @Inject
    private UserIdSource userIdSource;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/bower_components/**")
            .antMatchers("/fonts/**")
            .antMatchers("/images/**")
            .antMatchers("/scripts/**")
            .antMatchers("/styles/**")
            .antMatchers("/views/**")
            .antMatchers("/index.html")
            .antMatchers("/")
            ;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // disable CSRF now. TODO figure out how to config CSRF header in AngularJS
            .authorizeRequests()
                .antMatchers("/api/admin/**").hasAuthority(UserRoleType.ROLE_ADMIN.name())
                .antMatchers("/api/author/**").hasAuthority(UserRoleType.ROLE_AUTHOR.name())
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/currentUser").permitAll()
                .antMatchers("/signin/**").permitAll()
                .antMatchers("/connect/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .addFilterBefore(socialAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/signout")
                .logoutSuccessUrl("/")
                .and()
            .rememberMe()
                .rememberMeServices(rememberMeServices());
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder
        	.authenticationProvider(socialAuthenticationProvider())
        	.authenticationProvider(rememberMeAuthenticationProvider())
        	.userDetailsService(userAccountService);
    } 
    
    /**
     * Must expose AuthenticationManager as bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SocialAuthenticationFilter socialAuthenticationFilter() throws Exception{
        SocialAuthenticationFilter filter = new SocialAuthenticationFilter(
        		authenticationManager(), userIdSource,
                usersConnectionRepository, socialAuthenticationServiceLocator);
        filter.setFilterProcessesUrl("/signin");  //TODO fix the deprecated call.
        filter.setSignupUrl(null); 
        filter.setConnectionAddedRedirectUrl("/#/myAccount");
        filter.setPostLoginUrl("/#/myAccount"); //always open account profile page after login
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SocialAuthenticationProvider socialAuthenticationProvider(){
        return new SocialAuthenticationProvider(usersConnectionRepository, userAccountService);
    }

    @Bean
    public RememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                        environment.getProperty("application.key"), userAccountService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }
    
    @Bean 
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoPersistentTokenRepositoryImpl(rememberMeTokenRepository);
    }

    @Bean 
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = 
                        new RememberMeAuthenticationProvider(environment.getProperty("application.key"));
        return rememberMeAuthenticationProvider; 
    }

}
