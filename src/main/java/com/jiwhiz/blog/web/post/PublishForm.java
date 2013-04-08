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
package com.jiwhiz.blog.web.post;

import java.util.Calendar;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;

import com.jiwhiz.blog.domain.post.BlogPost;

/**
 * @author Yuan Ji
 *
 */
public class PublishForm {
    private String id;
    
    @NotEmpty
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PublishForm(){
        
    }
    
    public PublishForm(BlogPost blog){
        this.id = blog.getId();
        if (StringUtils.hasText(blog.getPublishedPath())){
            path = blog.getPublishedPath();
        } else {
            path = buildPath(blog.getTitle());
        }
    }

    private String buildPath(String title) {
        return title.replace(' ', '_');
    }
    
    public String getYearMonthPath(){
        return "/"+Calendar.getInstance().get(Calendar.YEAR)+"/"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"/";
    }
}
