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
package com.crazysunj.multitypeadapter.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunjian
 * created on: 2017/3/27
 * description: 存储单个level数据实体
 * 没有读懂源码之前，尽量不要在这里修改数据
 */
public final class LevelData<T> {

    private List<T> data;
    private T header;
    private T footer;

    public LevelData(List<T> data, T header, T footer) {
        this.data = data == null ? null : new ArrayList<>(data);
        this.header = header;
        this.footer = footer;
    }

    public void setData(List<T> data) {
        this.data = data == null ? null : new ArrayList<>(data);
    }

    public List<T> getData() {
        return data;
    }

    public T getHeader() {
        return header;
    }

    public void removeHeader() {
        header = null;
    }

    public void setHeader(T newHeader) {
        header = newHeader;
    }

    public T getFooter() {
        return footer;
    }

    public void removeFooter() {
        footer = null;
    }

    public void setFooter(T newFooter) {
        footer = newFooter;
    }
}
