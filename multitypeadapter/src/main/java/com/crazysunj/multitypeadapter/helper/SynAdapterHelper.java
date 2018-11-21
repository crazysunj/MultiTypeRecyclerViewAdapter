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

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import java.util.List;

/**
 * @author: sunjian
 * created on: 2017/4/1
 * description:
 * 同步适配器，数据量过大可能会卡顿，甚至出现ANR，相对来说比较稳定
 * 卡顿的原因主要是计算差异数据量过大以及大量UI更新
 * 可以采用异步刷新{@link AsynAdapterHelper}
 */
public abstract class SynAdapterHelper<T extends MultiTypeEntity> extends RecyclerViewAdapterHelper<T> {

    public SynAdapterHelper(List<T> data) {
        super(data);
    }

    public SynAdapterHelper(List<T> data, @RefreshMode int mode) {
        super(data, mode);
    }

    @Override
    protected void startRefresh(HandleBase<T> refreshData) {
        handleResult(handleRefresh(refreshData.getNewData(), refreshData.getNewHeader(),
                refreshData.getNewFooter(), refreshData.getLevel(), refreshData.getRefreshType()));
    }
}
