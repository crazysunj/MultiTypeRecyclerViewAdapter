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
package com.crazysunj.multitypeadapter.helper;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * 生成shimmer的实体类
 * Created by sunjian on 2017/4/20.
 */

final class ShimmerEntity implements MultiHeaderEntity {

    private long id;
    private long headerId = StickyHeaderDecoration.NO_HEADER_ID;
    private int type;

    ShimmerEntity() {
    }

    ShimmerEntity(long id, int type) {

        this.id = id;
        this.type = type;
    }

    public void setId(long id) {

        this.id = id;
    }

    public void setHeaderId(long headerId) {

        this.headerId = headerId;
    }

    public void setType(int type) {

        this.type = type;
    }

    @Override
    public long getId() {

        return id;
    }

    @Override
    public long getHeaderId() {

        return headerId;
    }

    @Override
    public int getItemType() {

        return type;
    }
}
