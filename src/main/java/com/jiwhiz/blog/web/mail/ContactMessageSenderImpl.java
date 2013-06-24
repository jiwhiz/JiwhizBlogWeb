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
package com.jiwhiz.blog.web.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comfirm.alphamail.services.client.AlphaMailService;
import com.comfirm.alphamail.services.client.entities.EmailContact;
import com.jiwhiz.blog.web.ContactMessageSender;
import com.jiwhiz.blog.web.site.ContactForm;

/**
 * Implementation for ContactMessageSender. Using Alpha Mail.
 * 
 * @author Yuan Ji
 * 
 */
public class ContactMessageSenderImpl extends AbstractMailSender implements ContactMessageSender {
    final static Logger logger = LoggerFactory.getLogger(ContactMessageSenderImpl.class);

    public ContactMessageSenderImpl(AlphaMailService mailService) {
        super(mailService);
    }

    @Override
    public void send(ContactForm contact) {
        doSend(new EmailContact(contact.getName(), contact.getEmail()), 
               new EmailContact(getAdminName(), getAdminEmail()), 
               contact);
    }

}
