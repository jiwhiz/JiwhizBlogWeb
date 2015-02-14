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

import com.github.sendgrid.SendGrid;
import com.jiwhiz.mail.EmailService;

/**
 * Email Service implementation using Sendgrid in CloudFoundry.
 * 
 * @author Yuan Ji
 *
 */
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private boolean initSuccess;
    private String username;
    private String password;
    
    public EmailServiceImpl() {
        try {
            Vcapenv vcapenv = new Vcapenv();
            this.username = vcapenv.SENDGRID_USERNAME();
            this.password = vcapenv.SENDGRID_PASSWORD();
            this.initSuccess = true;
        } catch (Exception ex) {
            this.initSuccess = false;
            LOGGER.error("Cannot get sendgrid username and password. Please check the service binding. "+ex.getMessage());
        }
    }
    
    public EmailServiceImpl(String username, String password) {
        this.username = username;
        this.password = password;
        this.initSuccess = true;
    }
    
    @Override
    public void sendEmail(String fromEmail, String fromName, String toEmail, String toName,
            String subject, String message, String replyTo) {
        if (!initSuccess) {
            return;
        }
        
        SendGrid sendgrid = new SendGrid(username, password);
        sendgrid.setFrom(fromEmail);
        sendgrid.setFromName(fromName);
        sendgrid.addTo(toEmail);
        sendgrid.addToName(toName);
        sendgrid.setReplyTo(replyTo);
        
        sendgrid.setSubject(subject);
        sendgrid.setText(message);

        try {
            LOGGER.info("Try to send email to {}, message is: {}", toEmail, message);
            String response = sendgrid.send();
            LOGGER.info("Sent email successfully, response from SendGrid is: {}", response);
        } catch (Exception ex) {
            ex.printStackTrace();
           LOGGER.error("Got exception when sending email through sendgrid: " + ex.getMessage());
        }
    }

}
