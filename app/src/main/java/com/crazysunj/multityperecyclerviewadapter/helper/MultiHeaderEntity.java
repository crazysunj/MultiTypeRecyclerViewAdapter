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

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * 多类型实体类接口
 * id标志不同数据项(包括头,唯一标识)
 * headerId标志粘性头部的类型(拥有相同headerId的数据项公用同一个粘性头部)
 * itemType标志数据的类型
 * 该类已经过时，希望大家自己实现，讲道理，这跟我们的主题并不符合
 * Created by sunjian on 2017/3/28.
 */

public interface MultiHeaderEntity extends MultiTypeEntity {

    long getHeaderId();
}
