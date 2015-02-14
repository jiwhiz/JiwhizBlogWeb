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

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * Util class to get sendgrid username/password from VCAP_SEVICES environment variables on CloudFoundry.
 * <p>
 * Copied from <a href="https://github.com/motdotla/vcapenv">https://github.com/motdotla/vcapenv</a>
 * 
 * @author yuan
 *
 */
public class Vcapenv {
    public JsonNode original_node;
    public JsonNode current_node;
    public ObjectMapper mapper;

    public Vcapenv() {
        this.original_node = null;
        this.current_node = null;
        this.mapper = new ObjectMapper();
        this.setNode();
    }

    public Vcapenv get(String key) {
        this.current_node = (JsonNode) this.current_node.get(key);
        return this;
    }

    public Vcapenv get(Integer index) {
        this.current_node = (JsonNode) this.current_node.get(index);
        return this;
    }

    public String toString() {
        return this.current_node.toString().replace("\"", "");
    }

    public String SENDGRID_USERNAME() {
        this.resetNode();
        return this.get("sendgrid").get(0).get("credentials").get("username").toString();
    }

    public String SENDGRID_PASSWORD() {
        this.resetNode();
        return this.get("sendgrid").get(0).get("credentials").get("password").toString();
    }

    public String resetNode() {
        this.current_node = this.original_node;
        return "reset";
    }

    public String setNode() {
        String vcap_services = System.getenv("VCAP_SERVICES");

        try {
            this.original_node = this.mapper.readValue(vcap_services, JsonNode.class);
            this.current_node = this.mapper.readValue(vcap_services, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vcap_services;
    }
}