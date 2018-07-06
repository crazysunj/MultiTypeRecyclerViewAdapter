/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.multitypeadapter.util;

/**
 * author: sunjian
 * created on: 2018/7/6 下午2:54
 * description: 辅助开发者自定义ID
 */
public class IDUtil {
    private static final long MIN_ID = Long.MIN_VALUE;
    private static final long MAX_ID = Long.MAX_VALUE;
    private static long sCurrentId = MIN_ID;

    private IDUtil() {
    }

    /**
     * 辅助id
     * 有特殊要求的同学可以自己设计
     *
     * @return 返回不重复Id
     */
    public static long getId() {
        if (MAX_ID <= sCurrentId) {
            throw new RuntimeException("it's the biggest");
        }
        return sCurrentId++;
    }
}
