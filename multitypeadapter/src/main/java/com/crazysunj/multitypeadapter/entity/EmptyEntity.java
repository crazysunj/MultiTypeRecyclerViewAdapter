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
package com.crazysunj.multitypeadapter.entity;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * 空实体类
 * 可根据业务自定义空实体类
 * <p>
 * Created by sunjian on 2017/5/15.
 */
public class EmptyEntity implements MultiHeaderEntity {

    protected long id;
    protected int type;

    public EmptyEntity(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public final int getType() {
        return type;
    }

    @Override
    public final int getItemType() {
        return type - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
    }

    @Override
    public final long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
