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
 * description: 错误实体类的适配器
 */
@FunctionalInterface
public interface ErrorEntityAdapter<T> {

    /**
     * 如果不想自己创建关于错误type的实体类，可以实现这个接口，然后调用{@link com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper#notifyModuleErrorChanged(int)}
     *
     * @param type  int 范围[-4000,-3000)
     * @param level int
     * @return T
     */
    T createErrorEntity(int type, int level);
}
