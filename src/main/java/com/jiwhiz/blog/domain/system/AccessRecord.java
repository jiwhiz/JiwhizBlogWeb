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
package com.jiwhiz.blog.domain.system;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jiwhiz.blog.domain.BaseEntity;

/**
 * @author Yuan Ji
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "AccessRecord")
public class AccessRecord extends BaseEntity {
    //@Indexed
    private Date accessTime;
    private String accessPath;
    private String userIpAddress;
    private String username;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getAccessTime() {
        return accessTime;
    }
    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }
    public String getAccessPath() {
        return accessPath;
    }
    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }
    public String getUserIpAddress() {
        return userIpAddress;
    }
    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }
    
    public AccessRecord(){
        
    }
    
    public AccessRecord(Date accessTime, String accessPath, String userIpAddress, String username){
        this.accessTime = accessTime;
        this.accessPath = accessPath;
        this.userIpAddress = userIpAddress;
        this.username = username;
                
    }
    
    public String getAccessDateTimeString() {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return formatter.format(accessTime);
    }

}
