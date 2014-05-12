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
package com.jiwhiz.rest.site;

import java.util.Comparator;

/**
 * @author Yuan Ji
 */
public class TagCloud {
    private String tag;
    private int count;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TagCloud(String tag, int count) {
        this.tag = tag;
        this.count = count;
    }

    public static Comparator<TagCloud> TagCloudNameComparator = new Comparator<TagCloud>() {

        public int compare(TagCloud tagCloud1, TagCloud tagCloud2) {
            // ascending order
            return tagCloud1.tag.compareTo(tagCloud2.tag);
        }

    };
    
    public static Comparator<TagCloud> TagCloudCountComparator = new Comparator<TagCloud>() {

        public int compare(TagCloud tagCloud1, TagCloud tagCloud2) {
            // descending order
            return tagCloud2.count - tagCloud1.count;
        }

    };

}
