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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jiwhiz.blog.domain.account.UserAccount;
import com.jiwhiz.blog.domain.account.UserAccountRepository;
import com.jiwhiz.blog.domain.account.UserAdminService;
import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for SlidePostService.
 * 
 * @author Yuan Ji
 *
 */
public class SlidePostServiceImpl extends AbstractPostServiceImpl implements SlidePostService{
    final static Logger logger = LoggerFactory.getLogger(SlidePostServiceImpl.class);
    
    private final SlidePostRepository slidePostRepository;

    @Inject
    public SlidePostServiceImpl(UserAccountRepository accountRepository, SlidePostRepository slidePostRepository, 
            UserAdminService userAdminService, CounterService counterService) {
        super(accountRepository, userAdminService, counterService);
        this.slidePostRepository = slidePostRepository;
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.post.SlidePostService#getSlideByPublishedPath(java.lang.String)
     */
    @Override
    public SlidePost getSlideByPublishedPath(String path) {
        return this.slidePostRepository.findByPublishedPath(path);
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.post.SlidePostService#getSlidePostsForCurrentUser(org.springframework.data.domain.Pageable)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public Page<SlidePost> getSlidePostsForCurrentUser(Pageable pageable){
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), 
                new Sort(Sort.Direction.DESC, "createdTime"));
        UserAccount account = userAdminService.getCurrentUser();
        Page<SlidePost> slideList = slidePostRepository.findByAuthorId(
                    account.getUserId(), pageable);
        
        for (SlidePost slide : slideList){
            slide.setVisits(counterService.getSlidePostVisitCount(slide.getId()));
        }
        return slideList;
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.post.SlidePostService#getSlideById(java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')") //TODO check user is the author of the slide 
    public SlidePost getSlideById(String id) {
        return this.slidePostRepository.findOne(id);
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.post.SlidePostService#createSlide(com.jiwhiz.blog.domain.post.StyleType, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public SlidePost createSlide(StyleType type, String title, String content, String path) {
        UserAccount account = userAdminService.getCurrentUser();
        SlidePost slidePost = new SlidePost(account.getUserId(), type, title, content, path);
        slidePostRepository.save(slidePost);
        return slidePost;
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.post.SlidePostService#updateSlide(com.jiwhiz.blog.domain.post.SlidePost)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_AUTHOR')") //TODO check user is the author of the slide 
    public SlidePost updateSlide(SlidePost slidePost) {
        return slidePostRepository.save(slidePost);
    }

}
