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
package com.crazysunj.multityperecyclerviewadapter.helper;

/**
 * @author sunjian
 * @description 默认实现返回StickyHeaderDecoration.NO_HEADER_ID
 * android现在还不能很好兼容java8特性，无奈之举，不然使用default method
 * @date 2017/5/13
 */
public abstract class DefaultMultiHeaderEntity implements MultiHeaderEntity {

    @Override
    public long getHeaderId() {
        return -1;
    }
}
