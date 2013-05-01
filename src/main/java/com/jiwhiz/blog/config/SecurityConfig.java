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
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.AuthenticationRegistry;
import org.springframework.security.config.annotation.method.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.EnableWebSecurity;
import org.springframework.security.config.annotation.web.ExpressionUrlAuthorizations;
import org.springframework.security.config.annotation.web.HttpConfigurator;
import org.springframework.security.config.annotation.web.SpringSecurityFilterChainBuilder.IgnoredRequestRegistry;
import org.springframework.security.config.annotation.web.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import com.jiwhiz.blog.domain.account.MongoPersistentTokenRepositoryImpl;
import com.jiwhiz.blog.domain.account.RememberMeTokenRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;

/**
 * Configuration for Spring Social and Security.
 * 
 * @author Yuan Ji
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private Environment environment;

    @Inject 
    private UserAdminService userAdminService;
    
    @Inject
    private RememberMeTokenRepository rememberMeTokenRepository;
    
    @Inject
    private AuthenticationManager authenticationManager;
    
    @Inject
    private UsersConnectionRepository usersConnectionRepository;
    
    @Inject
    private SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;
    
    @Bean
    public SocialAuthenticationFilter socialAuthenticationFilter() {
        SocialAuthenticationFilter filter = new SocialAuthenticationFilter(authenticationManager, userAdminService,
                usersConnectionRepository, socialAuthenticationServiceLocator);
        filter.setFilterProcessesUrl("/signin");
        filter.setSignupUrl(null); 
        filter.setConnectionAddedRedirectUrl("/myAccount");
        filter.setPostLoginUrl("/myAccount"); //always open account profile page after login
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SocialAuthenticationProvider socialAuthenticationProvider(){
        return new SocialAuthenticationProvider(usersConnectionRepository, userAdminService);
    }
    
    @Bean
    public LoginUrlAuthenticationEntryPoint socialAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/signin");
    }

    @Bean
    public RememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                        environment.getProperty("application.key"), userAdminService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }
    
    @Bean 
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = 
                        new RememberMeAuthenticationProvider(environment.getProperty("application.key"));
        return rememberMeAuthenticationProvider; 
    }

    @Bean 
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoPersistentTokenRepositoryImpl(rememberMeTokenRepository);
    }

    @Override
    protected void ignoredRequests(IgnoredRequestRegistry ignoredRequests) {
        ignoredRequests
            .antMatchers("/resources/**");
    }
    
    @Override
    protected void authorizeUrls(ExpressionUrlAuthorizations interceptUrls) {
        interceptUrls
            .antMatchers("/favicon.ico", "/robots.txt").permitAll()
            .antMatchers("/presentation/**").hasRole("USER")
            .antMatchers("/myAccount/**").hasRole("USER")
            .antMatchers("/myPost/**").hasRole("AUTHOR")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/**").permitAll();
    }


	@Override
	protected void configure(HttpConfigurator http) throws Exception {
        http
        .authenticationEntryPoint(socialAuthenticationEntryPoint())
        .addFilterBefore(socialAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
        .logout()
            .deleteCookies("JSESSIONID")
            .logoutUrl("/signout")
            .logoutSuccessUrl("/")
            .permitAll()
            .and()
        .rememberMe()
        	.rememberMeServices(rememberMeServices());
	}
    
    @Override
    protected void registerAuthentication(AuthenticationRegistry registry) throws Exception{
        registry
        	.add(socialAuthenticationProvider())
        	.add(rememberMeAuthenticationProvider())
        	.add(userAdminService);
    } 
}
