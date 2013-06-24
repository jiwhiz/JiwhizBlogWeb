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
package com.jiwhiz.blog.message;

public enum MessageType {
    
    /**
     * The message is informative in nature, like a note or notice.
     */
    INFO("alert-info"), 

    /**
     * The message indicates that an action initiated by the user was performed successfully.
     */
    SUCCESS("alert-success"), 
    
    /**
     * The message warns the user something is not quite right.
     * Corrective action is generally recommended but not required.
     */
    WARNING("alert-block"), 
    
    /**
     * The message reports an error condition that needs to be addressed by the user.
     */
    ERROR("alert-error");
    
    private final String cssClass;
    
    private MessageType(String cssClass) {
        this.cssClass = cssClass; 
    }
    
    /**
     * The css class for styling messages of this type.
     */
    public String getCssClass() {
        return cssClass;
    }
}
