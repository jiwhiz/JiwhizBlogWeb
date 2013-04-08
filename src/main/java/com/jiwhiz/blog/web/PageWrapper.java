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
package com.jiwhiz.blog.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Wrapper Spring Data {@link Page} for UI pagination.
 * 
 * @author Yuan Ji
 *
 */
public class PageWrapper<T> {
    public static final int MAX_PAGE_ITEM_DISPLAY = 5;
    private Page<T> page;
    private List<PageItem> items;
    private int currentNumber;
    private String url;
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PageWrapper(Page<T> page, String url){
        this.page = page;
        this.url = url;
        items = new ArrayList<PageItem>();
        
        currentNumber = page.getNumber() + 1; //start from 1 to match page.page
        
        int start, size;
        if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY){
            start = 1;
            size = page.getTotalPages();
        } else {
            if (currentNumber <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY/2){
                start = 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else if (currentNumber >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY/2){
                start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else {
                start = currentNumber - MAX_PAGE_ITEM_DISPLAY/2;
                size = MAX_PAGE_ITEM_DISPLAY;
            }
        }
        
        for (int i = 0; i<size; i++){
            items.add(new PageItem(start+i, (start+i)==currentNumber));
        }
    }
    
    public List<PageItem> getItems(){
        return items;
    }
    
    public int getNumber(){
        return currentNumber;
    }
    
    public List<T> getContent(){
        return page.getContent();
    }
    
    public int getSize(){
        return page.getSize();
    }
    
    public int getTotalPages(){
        return page.getTotalPages();
    }
    
    public long getTotalElements(){
        return page.getTotalElements();
    }
    
    public boolean isFirstPage(){
        return page.isFirstPage();
    }
    
    public boolean isLastPage(){
        return page.isLastPage();
    }
    
    public boolean isHasPreviousPage(){
        return page.hasPreviousPage();
    }
    
    public boolean isHasNextPage(){
        return page.hasNextPage();
    }
    
    public class PageItem {
        private int number;
        private boolean current;
        public PageItem(int number, boolean current){
            this.number = number;
            this.current = current;
        }
        
        public int getNumber(){
            return this.number;
        }
        
        public boolean isCurrent(){
            return this.current;
        }
    }
}
