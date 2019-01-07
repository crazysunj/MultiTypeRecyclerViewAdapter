# MTRVA

[![](https://travis-ci.org/crazysunj/MultiTypeRecyclerViewAdapter.svg?branch=master)](https://travis-ci.org/crazysunj/MultiTypeRecyclerViewAdapter)
[![License](https://img.shields.io/badge/license-Apache%202-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![](https://img.shields.io/github/release/crazysunj/MultiTypeRecyclerViewAdapter.svg)](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter/releases)

![](https://github.com/crazysunj/crazysunj.github.io/blob/master/img/mtrva_logo.png)

一个专注于RecyclerView优雅刷新(接管资源和数据源来代理进行数据处理)、高灵活、低耦合、健壮性以及高效性的轻量级MVP模式库，支持大多数Adapter

## 架构

![](https://github.com/crazysunj/crazysunj.github.io/blob/master/img/mtrva_architecture.png)

## 特点

* 使用简单快捷，支持大多数Adapter(高灵活、低耦合)
* 一行代码刷新(附动画)单个level(可对应多个type)
* 支持增删改查操作(健壮性)
* 支持异步，高频率，链式刷新，可扩展(如配合RxJava，高效性)
* 单个level支持Loading(加载)，Empty(空)，Error(错误)页面切换
* 单个level支持header，footer
* 单个level支持展开和闭合
* 支持加载全局Loading(加载)页面
* 支持注解生成类，减少工作量
* 支持刷新生命周期回调
* 兼容低版本及AndroidX版本RecyclerView
* 进阶用法，比如在多种页面之间自由切换

## 传送门

博客：[http://crazysunj.com/](http://crazysunj.com/ "http://crazysunj.com/")

谷歌邮箱：twsunj@gmail.com

QQ邮箱：387953660@qq.com

[版本记录](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter/releases)

[使用说明书](http://crazysunj.com/2017/08/14/MTRVA%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E4%B9%A6/)

由于我们专注于数据处理使得复杂布局非常简单，但我们与UI是完全解耦，开发者可以定制各种UI(使用各种Adapter)，每一种level相当于一个简单的Activity页面，想知道具体的用法，可以阅读文档；想知道具体效果，可以参考一下Demo。

下载地址：[使用Demo](https://www.pgyer.com/LAZn "https://www.pgyer.com/LAZn") [首页Demo](https://www.pgyer.com/sOVg "https://www.pgyer.com/sOVg") [项目Demo](https://www.pgyer.com/EbHS "https://www.pgyer.com/EbHS")

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




