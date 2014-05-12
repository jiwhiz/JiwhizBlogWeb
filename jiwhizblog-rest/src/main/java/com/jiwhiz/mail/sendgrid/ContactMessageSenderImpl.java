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

import com.jiwhiz.mail.ContactForm;
import com.jiwhiz.mail.ContactMessageSender;

/**
 * @author Yuan Ji
 */
public class ContactMessageSenderImpl extends AbstractMailSender implements ContactMessageSender {

    @Override
    public void send(ContactForm contact) {
        String subject = "Contact message";
        String message = contact.getName()+" tried to contact you on your website: "+contact.getMessage();
        doSend(contact.getEmail(), getAdminEmail(), subject, message);
     }

}
