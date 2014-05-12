/* 
 * Copyright 2013-2014 JIWHIZ Consulting Inc.
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

/**
 * @author Yuan Ji
 */
public class AbstractMailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMailSender.class);
    
    private String sendGridUsername;
    private String sendGridPassword;

    private String adminName;
    private String adminEmail;
    private String systemName;
    private String systemEmail;
    private String applicationBaseUrl;

    public String getSendGridUsername() {
        return sendGridUsername;
    }

    public void setSendGridUsername(String sendGridUsername) {
        this.sendGridUsername = sendGridUsername;
    }

    public String getSendGridPassword() {
        return sendGridPassword;
    }

    public void setSendGridPassword(String sendGridPassword) {
        this.sendGridPassword = sendGridPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemEmail() {
        return systemEmail;
    }

    public void setSystemEmail(String systemEmail) {
        this.systemEmail = systemEmail;
    }

    public String getApplicationBaseUrl() {
        return applicationBaseUrl;
    }

    public void setApplicationBaseUrl(String applicationBaseUrl) {
        this.applicationBaseUrl = applicationBaseUrl;
    }

    protected void doSend(String fromEmail, String toEmail, String subject, String message) {
        SendGrid sendgrid = new SendGrid(sendGridUsername, sendGridPassword);
        sendgrid.addTo(toEmail);
        sendgrid.setFrom(fromEmail);
        sendgrid.setSubject(subject);
        sendgrid.setText(message);

        String response = sendgrid.send();
        LOGGER.debug("Sent email, response from SendGrid is "+response);

    }
}
