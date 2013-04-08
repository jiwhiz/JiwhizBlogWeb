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

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author Yuan Ji (jiwhiz@gmail.com)
 *
 */
public class CounterServiceImpl implements CounterService {
    public static final String USER_ID_SEQUENCE_NAME = "user_id";
    public static final String VISIT_SEQUENCE_NAME = "visit_num";
    public static final String BLOG_VISIT_SEQUENCE_NAME_PREFIX = "blog_visit_num_";
    public static final String SLIDE_VISIT_SEQUENCE_NAME_PREFIX = "slide_visit_num_";

    private final MongoTemplate mongoTemplate;
    
    @Inject
    public CounterServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    
    /* (non-Javadoc)
     * @see com.jiwhiz.blog.domain.counter.CounterService#getNextUserIdSequence()
     */
    @Override
    public long getNextUserIdSequence() {
        return increaseCounter(USER_ID_SEQUENCE_NAME);
    }

    /* (non-Javadoc)
     * @see com.jiwhiz.blog.domain.counter.CounterService#logVisit()
     */
    @Override
    public long logVisit() {
        return increaseCounter(VISIT_SEQUENCE_NAME);
    }
    
    /* (non-Javadoc)
     * @see com.jiwhiz.blog.domain.counter.CounterService#getVisitCount()
     */
    @Override
    public long getVisitCount() {
        return getCount(VISIT_SEQUENCE_NAME);
    }

    @Override
    public long logBlogPostVisit(String blogPostId) {
        return increaseCounter(BLOG_VISIT_SEQUENCE_NAME_PREFIX+blogPostId);
    }

    @Override
    public long logSlidePostVisit(String slidePostId) {
        return increaseCounter(SLIDE_VISIT_SEQUENCE_NAME_PREFIX+slidePostId);
    }

    @Override
    public long getBlogPostVisitCount(String blogPostId) {
        return getCount(BLOG_VISIT_SEQUENCE_NAME_PREFIX+blogPostId);
    }

    @Override
    public long getSlidePostVisitCount(String slidePostId) {
        return getCount(SLIDE_VISIT_SEQUENCE_NAME_PREFIX+slidePostId);
    }
    
    private long increaseCounter(String counterName){
        Query query = new Query(Criteria.where("name").is(counterName));
        Update update = new Update().inc("sequence", 1);
        Counter counter = mongoTemplate.findAndModify(query, update, Counter.class); // return old Counter object
        if (counter == null){
            counter = new Counter();
            counter.setName(counterName);
            counter.setSequence(1);
            mongoTemplate.save(counter);
        }
        return counter.getSequence();
    }

    private long getCount(String counterName){
        Query query = new Query(Criteria.where("name").is(counterName));
        Counter counter = mongoTemplate.findOne(query, Counter.class); // return old Counter object
        if (counter != null){
            return counter.getSequence();
        } else {
            return 0l;
        }
    }


}
