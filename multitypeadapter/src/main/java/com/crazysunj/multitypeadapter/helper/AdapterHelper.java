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
        return ActionAdapterHelper.create(new ActionAdapterHelper.CallHandler() {
            @Override
            public void call(ActionAdapterHelper.ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                actionHandler.action(helper);
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
                final int dataSize = helper.getDataSize(mLevel);
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

            public LevelLoadingAdapterHelper header() {
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_HEADER;
                return this;
            }

            public LevelLoadingAdapterHelper data(int count) {
                mCount = count;
                mRefreshType |= RecyclerViewAdapterHelper.REFRESH_DATA;
                return this;
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
     */
    public static class ActionAdapterHelper {
        private CallHandler callHandler;

        private ActionAdapterHelper(CallHandler callHandler) {
            this.callHandler = callHandler;
        }

        private static ActionAdapterHelper create(CallHandler callHandler) {
            return new ActionAdapterHelper(callHandler);
        }

        /**
         * 刷新全部数据
         * 详细见{@link RecyclerViewAdapterHelper#notifyDataSetChanged(List)}
         *
         * @param data 传入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper all(final List<? extends MultiTypeEntity> data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.notifyDataSetChanged(data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 刷新全部数据
         * 详细见{@link RecyclerViewAdapterHelper#notifyDataSetChanged(MultiTypeEntity)}
         *
         * @param data 传入数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper all(final MultiTypeEntity data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.notifyDataSetChanged(data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 插入数据
         * 详细见{@link RecyclerViewAdapterHelper#addData(int, List)}
         *
         * @param position 插入位置
         * @param data     插入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(final int position, @NonNull final List<? extends MultiTypeEntity> data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.addData(position, data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 插入数据
         * 详细见{@link RecyclerViewAdapterHelper#addData(int, MultiTypeEntity)}
         *
         * @param position 插入位置
         * @param data     插入数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(final int position, @NonNull final MultiTypeEntity data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.addData(position, data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 插入数据
         * 详细见{@link RecyclerViewAdapterHelper#addData(List)}
         *
         * @param data 插入集合
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(@NonNull final List<? extends MultiTypeEntity> data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.addData(data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 插入数据
         * 详细见{@link RecyclerViewAdapterHelper#addData(MultiTypeEntity)}
         *
         * @param data 插入数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper add(@NonNull final MultiTypeEntity data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.addData(data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 删除数据
         * 详细见{@link RecyclerViewAdapterHelper#removeData(int)}
         *
         * @param position 删除位置
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remove(final int position) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.removeData(position);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 删除数据
         * 详细见{@link RecyclerViewAdapterHelper#removeData(int, int)}
         *
         * @param position 删除位置
         * @param count    删除总数
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remove(final int position, final int count) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.removeData(position, count);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 修改数据
         * 详细见{@link RecyclerViewAdapterHelper#setData(int, MultiTypeEntity)}
         *
         * @param position 修改位置
         * @param data     修改数据
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper set(final int position, @NonNull final MultiTypeEntity data) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.setData(position, data);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 清除所有数据
         * 详细见{@link RecyclerViewAdapterHelper#clear()}
         *
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper clear() {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.clear();
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 清除level数组中的数据
         * 详细见{@link RecyclerViewAdapterHelper#clearModule(int...)}
         *
         * @param levels 清除的level数组
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper clearModule(@NonNull final int... levels) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.clearModule(levels);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }

        /**
         * 只剩下level数组中的数据
         * 详细见{@link RecyclerViewAdapterHelper#remainModule(int...)}
         *
         * @param levels 剩下的level数组
         * @return ActionAdapterHelper
         */
        public ActionAdapterHelper remainModule(@NonNull final int... levels) {
            return create(new CallHandler() {
                @Override
                public void call(final ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
                    into(new ActionHandler() {
                        @Override
                        public void action(RecyclerViewAdapterHelper helper) {
                            helper.remainModule(levels);
                            actionHandler.action(helper);
                        }
                    }, helper);
                }
            });
        }


        public void into(RecyclerViewAdapterHelper helper) {
            callHandler.call(new ActionHandler() {
                @Override
                public void action(RecyclerViewAdapterHelper helper) {
                }
            }, helper);
        }

        private void into(ActionHandler actionHandler, RecyclerViewAdapterHelper helper) {
            callHandler.call(actionHandler, helper);
        }

        private interface ActionHandler {
            /**
             * 处理helper
             *
             * @param helper RecyclerViewAdapterHelper
             */
            void action(RecyclerViewAdapterHelper helper);
        }

        private interface CallHandler {
            /**
             * 回调helper
             *
             * @param actionHandler ActionHandler
             * @param helper        RecyclerViewAdapterHelper
             */
            void call(ActionHandler actionHandler, RecyclerViewAdapterHelper helper);
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
