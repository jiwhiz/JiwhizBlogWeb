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
package com.jiwhiz.blog.domain.post;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Yuan Ji
 *
 */
public class BlogPostTest {
    @Test
    public void testParseTags(){
        BlogPost post = new BlogPost();
        assertEquals("", post.getFormatTagString());
        
        post.parseAndSetTags(" Spring Framework , MongoDB   , jiwhiz ");
        assertEquals(3, post.getTags().size());
        assertEquals("Spring Framework", post.getTags().get(0));
        assertEquals("MongoDB", post.getTags().get(1));
        assertEquals("jiwhiz", post.getTags().get(2));
    }
    
    @Test
    public void testPublish() {
        BlogPost post = new BlogPost();
        assertFalse(post.isPublished());
        assertTrue(post.getPublishedYear() == 0);
        assertTrue(post.getPublishedMonth() == 0);
        
        post.publish("path");
        assertTrue(post.isPublished());
        assertTrue(post.getPublishedYear() > 0);
        assertTrue(post.getPublishedMonth() > 0);
    }
    
    @Test
    public void testGetContentFirstParagraph() {
        String paraText = "This is a test.";
        BlogPost post = new BlogPost();
        
        post.updateContent("<p>"+paraText+"</p>");
        assertEquals(paraText, post.getContentFirstParagraph());
        
        post.updateContent("<p>"+paraText);
        assertEquals(paraText, post.getContentFirstParagraph());
        
        post.updateContent(paraText);
        assertEquals(paraText, post.getContentFirstParagraph());
        
    }
}
