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

import android.util.Log;

import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: sunjian
 * created on: 2019/1/4 上午9:34
 * description: 明确未有数据和已有数据的刷新区别，同时采用责任链方式方便使用
 */
public class AdapterHelper {
    public static LevelAdapterHelper with(int level) {
        return new LevelAdapterHelper(level);
    }

    public static ActionAdapterHelper action() {
        return ActionAdapterHelper.create(new ActionAdapterHelper.ActionHandler() {
            @Override
            public void call(RecyclerViewAdapterHelper helper) {
                Log.e("ActionAdapterHelper", "action");
            }
        });
    }

    /**
     * 专门针对level数据
     */
    public static class LevelAdapterHelper {
        private int mLevel;

        private LevelAdapterHelper(int level) {
            mLevel = level;
        }

        public LevelBindAdapterHelper data(List<? extends MultiTypeEntity> data) {
            return new LevelBindAdapterHelper(mLevel)
                    .data(data);
        }

        public LevelBindAdapterHelper data(MultiTypeEntity data) {
            return new LevelBindAdapterHelper(mLevel)
                    .data(data);
        }

        public LevelBindAdapterHelper header(MultiTypeEntity header) {
            return new LevelBindAdapterHelper(mLevel)
                    .header(header);
        }

        public LevelBindAdapterHelper footer(MultiTypeEntity footer) {
            return new LevelBindAdapterHelper(mLevel)
                    .footer(footer);
        }

        /**
         * 详细见{@link RecyclerViewAdapterHelper#notifyModuleEmptyChanged(int)}
         */
        public LevelEmptyAdapterHelper empty() {
            return new LevelEmptyAdapterHelper(mLevel);
        }

        /**
         * 详细见{@link RecyclerViewAdapterHelper#notifyModuleEmptyChanged(MultiTypeEntity, int)}
         */
        public LevelEmptyAdapterHelper empty(MultiTypeEntity empty) {
            return new LevelEmptyAdapterHelper(mLevel, empty);
        }

        /**
         * 详细见{@link RecyclerViewAdapterHelper#notifyModuleErrorChanged(int)}
         */
        public LevelErrorAdapterHelper error() {
            return new LevelErrorAdapterHelper(mLevel);
        }

        /**
         * 详细见{@link RecyclerViewAdapterHelper#notifyModuleErrorChanged(MultiTypeEntity, int)}
         */
        public LevelErrorAdapterHelper error(MultiTypeEntity error) {
            return new LevelErrorAdapterHelper(mLevel, error);
        }

        public LevelLoadingAdapterHelper loading() {
            return new LevelLoadingAdapterHelper(mLevel);
        }

        public static class LevelBindAdapterHelper {
            private int mLevel;
            private List<? extends MultiTypeEntity> mData;
            private MultiTypeEntity mHeader;
            private MultiTypeEntity mFooter;
            private int mRefreshType = RecyclerViewAdapterHelper.REFRESH_NONE;

            private LevelBindAdapterHelper(int level) {
                mLevel = level;
            }

            public LevelBindAdapterHelper data(List<? extends MultiTypeEntity> data) {
                mData = data;
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_DATA;
                return this;
            }

            public LevelBindAdapterHelper data(MultiTypeEntity data) {
                mData = Collections.singletonList(data);
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_DATA;
                return this;
            }

            public LevelBindAdapterHelper header(MultiTypeEntity header) {
                mHeader = header;
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_HEADER;
                return this;
            }

            public LevelBindAdapterHelper footer(MultiTypeEntity footer) {
                mFooter = footer;
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_FOOTER;
                return this;
            }

            @SuppressWarnings("unchecked")
            public void into(RecyclerViewAdapterHelper helper) {
                LevelData levelData = helper.getDataWithLevel(mLevel);
                List data = levelData == null ? null : levelData.getData();
                final int positionStart = helper.getLevelPositionStart(mLevel);
                Object header = levelData == null ? null : levelData.getHeader();
                Object footer = levelData == null ? null : levelData.getFooter();
                final int dataSize = data == null ? 0 : data.size();
                if (intersectant(mData, data)) {
                    final int positionDataStart = positionStart + (header == null ? 0 : 1);
                    // 这里的算法有待调整，效果不是很好
                    List helperData = helper.getData();
                    helperData.removeAll(data);
                    data.clear();
                    helperData.addAll(positionDataStart, mData);
                    data.addAll(mData);
                    RecyclerView.Adapter bindAdapter = helper.getBindAdapter();
                    bindAdapter.notifyItemRangeRemoved(positionDataStart, dataSize);
                    bindAdapter.notifyItemRangeChanged(positionDataStart, helperData.size() - positionDataStart);
                    bindAdapter.notifyItemRangeInserted(positionDataStart, mData.size());
                    if (intersectant(mHeader, header)) {
                        helper.setData(positionStart, mHeader);
                    } else {
                        helper.notifyModuleHeaderChanged(mHeader, mLevel);
                    }
                    if (intersectant(mFooter, footer)) {
                        helper.setData(positionStart + (mHeader == null ? 0 : 1) + mData.size(), mFooter);
                    } else {
                        helper.notifyModuleFooterChanged(mFooter, mLevel);
                    }
                    return;
                }
                if (intersectant(mHeader, header)) {
                    helper.setData(positionStart, mHeader);
                }
                if (intersectant(mFooter, footer)) {
                    helper.setData(positionStart + (header == null ? 0 : 1) + dataSize, mFooter);
                }
                helper.notifyModuleChanged(mData, mHeader, mFooter, mLevel, mRefreshType);
            }
        }

        public static class LevelEmptyAdapterHelper {
            private int mLevel;
            private MultiTypeEntity mEmpty;

            private LevelEmptyAdapterHelper(int level) {
                mLevel = level;
            }

            private LevelEmptyAdapterHelper(int level, MultiTypeEntity empty) {
                mLevel = level;
                mEmpty = empty;
            }

            @SuppressWarnings("unchecked")
            public void into(RecyclerViewAdapterHelper helper) {
                if (mEmpty == null) {
                    helper.notifyModuleEmptyChanged(mLevel);
                    return;
                }
                helper.notifyModuleEmptyChanged(mEmpty, mLevel);
            }
        }

        public static class LevelErrorAdapterHelper {
            private int mLevel;
            private MultiTypeEntity mError;

            private LevelErrorAdapterHelper(int level) {
                mLevel = level;
            }

            private LevelErrorAdapterHelper(int level, MultiTypeEntity error) {
                mLevel = level;
                mError = error;
            }

            @SuppressWarnings("unchecked")
            public void into(RecyclerViewAdapterHelper helper) {
                if (mError == null) {
                    helper.notifyModuleErrorChanged(mLevel);
                    return;
                }
                helper.notifyModuleErrorChanged(mError, mLevel);
            }
        }

        public static class LevelLoadingAdapterHelper {

            private int mLevel;
            private int mRefreshType = RecyclerViewAdapterHelper.REFRESH_NONE;
            private int mCount;

            private LevelLoadingAdapterHelper(int level) {
                mLevel = level;
            }

            public void header() {
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_HEADER;
            }

            public void data(int count) {
                mCount = count;
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_DATA;
            }

            /**
             * mRefreshType:
             * RecyclerViewAdapterHelper.REFRESH_NONE:详细见{@link RecyclerViewAdapterHelper#notifyLoadingHeaderChanged(int)}
             * RecyclerViewAdapterHelper.REFRESH_HEADER:详细见{@link RecyclerViewAdapterHelper#notifyLoadingHeaderChanged(int)}
             * RecyclerViewAdapterHelper.REFRESH_DATA:详细见{@link RecyclerViewAdapterHelper#notifyLoadingDataChanged(int, int)}
             * RecyclerViewAdapterHelper.REFRESH_HEADER_DATA:详细见{@link RecyclerViewAdapterHelper#notifyLoadingDataAndHeaderChanged(int, int)}
             */
            public void into(RecyclerViewAdapterHelper helper) {
                if (mRefreshType == RecyclerViewAdapterHelper.REFRESH_NONE) {
                    helper.notifyLoadingChanged(mLevel);
                    return;
                }
                if (mRefreshType == RecyclerViewAdapterHelper.REFRESH_HEADER) {
                    helper.notifyLoadingHeaderChanged(mLevel);
                    return;
                }
                if (mRefreshType == RecyclerViewAdapterHelper.REFRESH_DATA) {
                    helper.notifyLoadingDataChanged(mLevel, mCount);
                    return;
                }
                helper.notifyLoadingDataAndHeaderChanged(mLevel, mCount);
            }
        }
    }

    /**
     * 常规操作
     * 注意点：执行顺序是从最底下开始的
     */
    public static class ActionAdapterHelper {
        private ActionHandler actionHandler;

        private ActionAdapterHelper(ActionHandler actionHandler) {
            this.actionHandler = actionHandler;
        }

        private static ActionAdapterHelper create(ActionHandler actionHandler) {
            return new ActionAdapterHelper(actionHandler);
        }

        /**
         * 刷新全部数据
         *
         * @param data 传入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper all(final List<? extends MultiTypeEntity> data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "all");
                    helper.notifyDataSetChanged(data);
                    into(helper);
                }
            });
        }

        /**
         * 插入数据
         *
         * @param position 插入位置
         * @param data     插入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(final int position, @NonNull final List<? extends MultiTypeEntity> data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "add-position-list");
                    helper.addData(position, data);
                    into(helper);
                }
            });
        }

        /**
         * 插入数据
         *
         * @param position 插入位置
         * @param data     插入数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(final int position, @NonNull final MultiTypeEntity data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "add-position-data");
                    helper.addData(position, data);
                    into(helper);
                }
            });
        }

        /**
         * 插入数据
         *
         * @param data 插入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(@NonNull final List<? extends MultiTypeEntity> data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "add-list");
                    helper.addData(data);
                    into(helper);
                }
            });
        }

        /**
         * 插入数据
         *
         * @param data 插入数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(@NonNull final MultiTypeEntity data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "add-data");
                    helper.addData(data);
                    into(helper);
                }
            });
        }

        /**
         * 删除数据
         *
         * @param position 删除位置
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remove(final int position) {
            return create(new ActionHandler() {
                @Override
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "remove-position");
                    helper.removeData(position);
                    into(helper);
                }
            });
        }

        /**
         * 删除数据
         *
         * @param position 删除位置
         * @param count    删除总数
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remove(final int position, final int count) {
            return create(new ActionHandler() {
                @Override
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "remove-position-count");
                    helper.removeData(position, count);
                    into(helper);
                }
            });
        }

        /**
         * 修改数据
         *
         * @param position 修改位置
         * @param data     修改数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper set(final int position, @NonNull final MultiTypeEntity data) {
            return create(new ActionHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "set-position-data");
                    helper.setData(position, data);
                    into(helper);
                }
            });
        }

        /**
         * 清除所有数据
         *
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper clear() {
            return create(new ActionHandler() {
                @Override
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "clear");
                    helper.clear();
                    into(helper);
                }
            });
        }

        /**
         * 清除level数组中的数据
         *
         * @param levels 清除的level数组
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper clearModule(@NonNull final int... levels) {
            return create(new ActionHandler() {
                @Override
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "clearModule");
                    helper.clearModule(levels);
                    into(helper);
                }
            });
        }

        /**
         * 只剩下level数组中的数据
         *
         * @param levels 剩下的level数组
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remainModule(@NonNull final int... levels) {
            return create(new ActionHandler() {
                @Override
                public void call(RecyclerViewAdapterHelper helper) {
                    Log.e("ActionAdapterHelper", "remainModule");
                    helper.remainModule(levels);
                    into(helper);
                }
            });
        }


        public void into(RecyclerViewAdapterHelper helper) {
            actionHandler.call(helper);
        }

        private interface ActionHandler {
            /**
             * 回调helper
             *
             * @param helper RecyclerViewAdapterHelper
             */
            void call(RecyclerViewAdapterHelper helper);
        }
    }

    /**
     * 判断两个集合是否有交集，只判断引用相同的情况，排除用户自己实现equals
     *
     * @param l1 集合1
     * @param l2 集合2
     * @return true为有交集
     */
    private static boolean intersectant(List<?> l1, List<?> l2) {
        if (l1 == null || l2 == null) {
            return false;
        }
        Collection<?> contains = l2;
        Collection<?> iterate = l1;
        int l1size = l1.size();
        int l2size = l2.size();
        if (l1size == 0 || l2size == 0) {
            return false;
        }

        if (l1size > l2size) {
            iterate = l2;
            contains = l1;
        }

        for (Object e : iterate) {
            for (Object contain : contains) {
                if (contain == e) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断两个对象的引用是否相同
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return true为相同
     */
    private static boolean intersectant(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1 == o2;
    }
}
