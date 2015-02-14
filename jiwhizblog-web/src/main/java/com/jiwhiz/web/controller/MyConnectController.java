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
package com.jiwhiz.web.controller;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;

/**
 * Override Spring Social {@link ConnectController} to redirect to account overview page
 * after user connected or not connected to provider.
 * 
 * @author Yuan Ji
 *
 */
@Controller
public class MyConnectController extends ConnectController{
    
    private static final String ACCOUNT_PAGE = "/#/myAccount";

    @Inject
    public MyConnectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }

    protected String connectView(String providerId) {
        return "redirect:" + ACCOUNT_PAGE;
    }

    protected String connectedView(String providerId) {
        return "redirect:" + ACCOUNT_PAGE;
    }
    
}
