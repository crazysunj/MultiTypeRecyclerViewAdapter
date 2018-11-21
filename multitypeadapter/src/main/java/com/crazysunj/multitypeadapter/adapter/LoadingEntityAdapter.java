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
package com.crazysunj.multitypeadapter.adapter;

/**
 * @author: sunjian
 * created on: 2017/6/29
 * description: 加载实体类的适配器
 */
public interface LoadingEntityAdapter<T> {

    /**
     * 创建loading的data部分
     *
     * @param type  int
     * @param level int
     * @return T
     */
    T createLoadingEntity(int type, int level);

    /**
     * 创建loading的header部分
     *
     * @param type  int
     * @param level int
     * @return T
     */
    T createLoadingHeaderEntity(int type, int level);

    /**
     * loadingEntity是复用的，可以在回调这做一些操作
     *
     * @param loadingEntity 复用entity
     * @param position      所在索引，头的索引为-1
     */
    void bindLoadingEntity(T loadingEntity, int position);
}
