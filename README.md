# MultiTypeRecyclerViewAdapter
一个专注于RecyclerView优雅刷新的库，可配合大多数Adapter

## 特点

* 简单快捷，可配合大多数Adapter
* 一行代码刷新单个type，支持一般的set，add，remove，clear等刷新
* 支持异步，高频率刷新，可扩展(如配合RxJava)
* 单个type支持Loading(可支持shimmer效果)，empty，error页面切换
* 单个type支持粘性头，header，footer
* 单个type支持展开和合拢(可设置合拢最小值)
* 支持注解生成类，减少工作量
* 支持刷新生命周期回调

## 效果
![](https://github.com/crazysunj/crazysunj.github.io/blob/master/img/adapterHelper9.gif)

## 注意点
### Type 取值范围

* level [0,+∞)
* data类型 [0,1000)
* header类型 [-1000,0)
* loading-data类型 [-2000,-1000)
* loading-header类型 [-3000,-2000)
* error类型 [-4000,-3000)
* empty类型 [-5000,-4000)
* footer类型 [-6000,-5000)

### 差值常量

```
//header类型差值
public static final int HEADER_TYPE_DIFFER = 1000;
//loading-data类型差值
public static final int LOADING_DATA_TYPE_DIFFER = 2000;
//loading-header类型差值
public static final int LOADING_HEADER_TYPE_DIFFER = 3000;
//error类型差值
public static final int ERROR_TYPE_DIFFER = 4000;
//empty类型差值
public static final int EMPTY_TYPE_DIFFER = 5000;
//footer类型差值
public static final int FOOTER_TYPE_DIFFER = 6000;
```

### 其他

```
protected int getPreDataCount();
```
获取数据前条目数量，保持数据与Adapter一致，比如Adapter的position=0添加的是headerView，如果你的Adapter有这样的条件，请重写该方法或者自己处理数据的时候注意，否则数据混乱，甚至崩溃都是有可能的。

调用刷新方法的时候请注意刷新模式，有些方法只支持相应刷新模式，需要注意的都加有check语句。

关于entity的id为long类型是考虑刷新效率，你大可采用多种属性的UUID的hashCode或者就是普通hashCode作为主键（参考demo，注意缓存）。倘若还支持不了你的数据(出现哈希冲突，并不是没刷新，可打日志调试)，就自定义DiffCallback(可参数demo)。

具体可**参考Demo**，建议把helper封装在Adapter中。

## gradle依赖

```
compile 'com.crazysunj:multitypeadapter:1.6.0'
```
如果想用注解生成可依赖

```
apt 'com.crazysunj:multitypeadapter-compiler:1.6.0'
```
记得在项目gradle中添加

```
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
```
主Module中添加

```
apply plugin: 'com.neenbedankt.android-apt'
```

该库还依赖了recyclerview，想依赖自己的recyclerview版本，请如下依赖。

```
compile('com.crazysunj:multitypeadapter:1.6.0') {
    exclude group: 'com.android.support', module: 'recyclerview-v7'
}
```
## 感谢

[shimmer-android](https://github.com/facebook/shimmer-android)

## 传送门

博客:[http://crazysunj.com/](http://crazysunj.com/)

谷歌邮箱:twsunj@gmail.com

QQ邮箱:387953660@qq.com

[版本记录](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter/releases)

**欢迎大家的star(fork)和反馈(可发issues或者我的邮箱）。**

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




