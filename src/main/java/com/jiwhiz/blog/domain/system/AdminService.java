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

import java.util.List;

/**
 * @author Yuan Ji
 * 
 */
public interface AdminService {
    /**
     * Gets AccessRecord list for last 24 hours.
     * @return
     */
    List<AccessRecord> getLastDayAccess();
    
    /**
     * Record access from IP for path.
     * 
     * @param userIpAddress
     * @param path
     */
    void recordAccess(String userIpAddress, String path);
}
