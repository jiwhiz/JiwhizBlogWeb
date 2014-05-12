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
package com.jiwhiz.domain.system.impl;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.jiwhiz.domain.system.Counter;
import com.jiwhiz.domain.system.CounterService;

/**
 * @author Yuan Ji
 *
 */
public class CounterServiceImpl implements CounterService {
    public static final String USER_ID_SEQUENCE_NAME = "user_id";

    private final MongoTemplate mongoTemplate;
    
    @Inject
    public CounterServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public long getNextUserIdSequence() {
        return increaseCounter(USER_ID_SEQUENCE_NAME);
    }

    private long increaseCounter(String counterName){
        Query query = new Query(Criteria.where("name").is(counterName));
        Update update = new Update().inc("sequence", 1);
        Counter counter = mongoTemplate.findAndModify(query, update, Counter.class); // return old Counter object
        if (counter == null){
            counter = new Counter();
            counter.setName(counterName);
            counter.setSequence(2); //should increase by one.
            mongoTemplate.save(counter);
            return 1;
        }
        return counter.getSequence();
    }

}
