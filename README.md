# MTRVA

[![](https://travis-ci.org/crazysunj/MultiTypeRecyclerViewAdapter.svg?branch=master)](https://travis-ci.org/crazysunj/MultiTypeRecyclerViewAdapter)
[![License](https://img.shields.io/badge/license-Apache%202-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![](https://img.shields.io/github/release/crazysunj/MultiTypeRecyclerViewAdapter.svg)](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter/releases)

![](https://github.com/crazysunj/crazysunj.github.io/blob/master/img/mtrva_logo.png)

一个专注于RecyclerView优雅刷新(接管资源和数据源来代理Adapter进行数据处理)的轻量级MVP模式库，让你更关注UI逻辑，对Adapter进行组件化封装（大型UI框架也可）效果更佳

## 架构

![](https://github.com/crazysunj/crazysunj.github.io/blob/master/img/mtrva_architecture.png)

## 特点

* 一行代码刷新(附动画)单个level(一个level可对应多个type)
* 支持常规增删改查操作
* 支持RecyclerView组件化
* 支持异步，高频率，链式刷新，可扩展(如配合RxJava)
* 单个level支持Loading(加载)，Empty(空)，Error(错误)页面切换
* 单个level支持header，footer
* 单个level支持展开和闭合(附动画)
* 支持刷新生命周期回调
* 异步差量计算数据，只刷新改动部分，对比算法可自定义
* 支持动态注册资源(根据服务端返回数据解析注册，不再静态写死)

## 传送门

博客：[http://crazysunj.com/](http://crazysunj.com/ "http://crazysunj.com/")

谷歌邮箱：twsunj@gmail.com

QQ邮箱：387953660@qq.com

[版本记录](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter/releases)

[使用说明书](http://crazysunj.com/2017/08/14/MTRVA%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E4%B9%A6/)

由于我们专注于数据处理使得复杂布局非常简单，但我们与UI是完全解耦，开发者可以定制各种UI(使用各种Adapter)，每一种level相当于一个简单的Activity页面，想知道具体的用法，可以阅读文档；想知道具体效果，可以参考一下Demo。

**注：本库已从jcenter迁移mavenCentral，请重新依赖，具体参考使用说明书**

**欢迎大家的star(fork)**

**如果大家有什么问题或者觉得哪里需要优化改进或者有新奇想法可以发我邮箱或者直接加我QQ387953660，大家一起来探讨**

## License

> ```
> Copyright 2017 Sun Jian
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```




