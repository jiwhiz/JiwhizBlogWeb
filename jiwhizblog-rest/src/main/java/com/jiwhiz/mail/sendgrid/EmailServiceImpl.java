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
package com.jiwhiz.mail.sendgrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import com.github.sendgrid.SendGrid;
import com.jiwhiz.mail.EmailMessage;
import com.jiwhiz.mail.EmailService;

/**
 * Email Service implementation using Sendgrid in CloudFoundry.
 * 
 * @author Yuan Ji
 *
 */
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private String username;
    private String password;
    
    public EmailServiceImpl() {
        try {
            Vcapenv vcapenv = new Vcapenv();
            this.username = vcapenv.SENDGRID_USERNAME();
            this.password = vcapenv.SENDGRID_PASSWORD();
        } catch (Exception ex) {
            LOGGER.error("Cannot get sendgrid username and password. Please check the service binding. "+ex.getMessage());
        }
    }
    
    public EmailServiceImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Sends email asynchronously through Sendgrid. 
     */
	@Override
	@Async
	public void sendEmail(final EmailMessage message) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return;
		}
		
		SendGrid sendgrid = new SendGrid(username, password);
        sendgrid.setFrom(message.getFromEmail());
        sendgrid.setFromName(message.getFromName());
        sendgrid.addTo(message.getToEmail());
        sendgrid.addToName(message.getToName());
        sendgrid.setReplyTo(message.getReplyTo());
        sendgrid.setSubject(message.getSubject());
        sendgrid.setText(message.getBody());

        try {
            LOGGER.info("Try to send email to {}, message is: {}", message.getToEmail(), message.getBody());
            String response = sendgrid.send();
            LOGGER.info("Sent email successfully, response from SendGrid is: {}", response);
        } catch (Exception ex) {
            LOGGER.debug("Exception:", ex);
            LOGGER.error("Got exception when sending email through sendgrid: {}", ex.getMessage());
        }
		
	}
}
