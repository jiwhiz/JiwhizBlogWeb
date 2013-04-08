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
package com.jiwhiz.blog.web.site;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiwhiz.blog.message.Message;
import com.jiwhiz.blog.message.MessageType;
import com.jiwhiz.blog.web.AbstractPublicPageController;
import com.jiwhiz.blog.web.ContactMessageSender;

/**
 * Controller for Contact page. Handle submitting contact message function.
 * 
 * @author Yuan Ji
 *
 */
@Controller
public class ContactController extends AbstractPublicPageController {
    @Inject
    private ContactMessageSender contactMessageSender;

    public ContactController() {
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactForm(Model uiModel, HttpServletRequest request) {
        counterService.logVisit();
        uiModel.addAttribute("contactForm", new ContactForm());
        return "site/contact";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactSubmit(@ModelAttribute("contactForm") @Valid ContactForm contactForm,
            BindingResult bindingResult, Model uiModel, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message(MessageType.ERROR, "Please fill in all the required fields."));
            uiModel.addAttribute("contactForm", contactForm);
            return "site/contact";
        }

        //send email message to me
        contactMessageSender.send(contactForm);

        uiModel.addAttribute("message", new Message(MessageType.SUCCESS, "Hi "+ contactForm.getName()+
                ", your message was received. I will response as soon as possible. Thank you! -Yuan"));
        uiModel.addAttribute("contactForm", new ContactForm());
        return "site/contact";
    }

}
