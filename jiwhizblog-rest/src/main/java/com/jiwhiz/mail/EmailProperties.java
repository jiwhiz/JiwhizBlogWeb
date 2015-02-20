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
package com.jiwhiz.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author Yuan Ji
 *
 */
@ConfigurationProperties("application.email")
public class EmailProperties {
	/**
	 * Name of administrator the notification or contact message will send to.
	 */
    private String adminName;
    
    /**
     * Email of administrator the notification or contact message will send to.
     */
    private String adminEmail;
    
    /**
     * Message sender name, like "Jiwhiz Blog".
     */
    private String systemName;
    
    /**
     * Message sender email.
     */
    private String systemEmail;
    
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
    
}
