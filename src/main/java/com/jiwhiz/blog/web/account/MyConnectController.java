/**
 * Copyright © 2012 Jiwhiz.com. All rights reserved.
 */
package com.jiwhiz.blog.web.account;

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
    
    private static final String ACCOUNT_PAGE = "/myAccount";

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
