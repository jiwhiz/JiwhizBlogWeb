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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiwhiz.blog.domain.account.AccountUtils;

/**
 * @author Yuan Ji
 * 
 */
public class AdminServiceImpl implements AdminService {
    final static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    
    private final AccessRecordRepository accessRecordRepository;
    
    @Inject
    public AdminServiceImpl(AccessRecordRepository accessRecordRepository){
        this.accessRecordRepository = accessRecordRepository;
    }
    
    /* (non-Javadoc)
     * @see com.jiwhiz.blog.domain.admin.AdminService#getLastDayAccess()
     */
    @Override
    public List<AccessRecord> getLastDayAccess() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        return accessRecordRepository.findAccessAfterTime(yesterday.getTime());
    }

    @Override
    public void recordAccess(String userIpAddress, String path) {
        AccessRecord record = new AccessRecord(new Date(), path, userIpAddress, AccountUtils.getLoginUserId());
        accessRecordRepository.save(record);
    }


}
