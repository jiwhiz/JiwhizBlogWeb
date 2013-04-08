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

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiwhiz.blog.domain.system.AccessRecord;
import com.jiwhiz.blog.domain.system.AccessRecordRepository;

/**
 * @author Yuan Ji
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.jiwhiz.blog.domain.TestConfig.class })
@ActiveProfiles("local")
public class AccessRecordRepositoryTest {
    @Inject
    AccessRecordRepository accessRecordRepository;

    @After
    public void shutdown() {
        accessRecordRepository.deleteAll();
    }

    @Test
    public void testFindAccessAfterTime() {
        // create 10 records
        int i = 0;
        while (i < 10) {
            i++;
            AccessRecord record = new AccessRecord(new Date(), "2012/10/Test_Path", "1.0.0.1", "jiwhiz");
            accessRecordRepository.save(record);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                // ignore
            }
        }
        
        Calendar time = Calendar.getInstance();
        time.add(Calendar.DATE, -1);

        List<AccessRecord> result = accessRecordRepository.findAccessAfterTime(time.getTime());
        
        assertEquals(10, result.size());
    }
}
