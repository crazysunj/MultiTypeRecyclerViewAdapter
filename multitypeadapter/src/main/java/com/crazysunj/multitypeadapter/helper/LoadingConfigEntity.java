/*
  Copyright 2017 Sun Jian
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

/**
 * @author: sunjian
 * created on: 2017/6/9
 * description: loading的全局刷新配置实体
 * @see LoadingConfig
 */
class LoadingConfigEntity {

    int count;
    boolean isHaveHeader;

    LoadingConfigEntity(boolean isHaveHeader) {
        this(0, isHaveHeader);
    }

    LoadingConfigEntity(int count) {
        this(count, false);
    }

    LoadingConfigEntity(int count, boolean isHaveHeader) {
        this.count = count;
        this.isHaveHeader = isHaveHeader;
    }
}
