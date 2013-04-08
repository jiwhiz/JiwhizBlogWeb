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
package com.jiwhiz.mail;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.AlphaMailServiceException;
import com.comfirm.alphamail.services.client.entities.EmailContact;
import com.comfirm.alphamail.services.client.entities.EmailMessagePayload;
import com.comfirm.alphamail.services.client.entities.ServiceIdentityResponse;

/**
 * Abstract super class for all email sender classes. Using Alpha Mail as
 * underline email service provider.
 * 
 * 
 * @author Yuan Ji
 * 
 */
public class AbstractMailSender {
    final static Logger logger = LoggerFactory.getLogger(AbstractMailSender.class);

    private final AlphaMailService mailService;
    private int projectId;
    private String senderName;
    private String senderEmail;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Inject
    public AbstractMailSender(AlphaMailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Call Alpha Mail service API to send payload object.
     * @param receiver
     * @param payload
     * @return response from Alpha Mail service
     * @throws AlphaMailServiceException
     */
    protected ServiceIdentityResponse doSend(EmailContact receiver, Object payload) throws AlphaMailServiceException {
        EmailMessagePayload message = new EmailMessagePayload().setProjectId(getProjectId()).setReceiverId(0)
                .setSender(new EmailContact(getSenderName(), getSenderEmail())).setReceiver(receiver)
                .setBodyObject(payload);
        ServiceIdentityResponse response = mailService.queue(message);
        logger.debug("send email content ='"+payload.toString());
        logger.info(String.format("Send email to %s, result is %s ", receiver.getEmail(), response.getResult()));
        return response;
    }
}
