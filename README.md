# MultiTypeRecyclerViewAdapter
MultiTypeRecyclerViewAdapter for recyclerview


* 自动添加模块头
* 支持粘性头
* 自动刷新单个模块
* 一行代码搞定，大大加快开发效率

先给大家欣赏几张优美的动态图，网差的朋友赶紧把demo下载下来，运行运行，看看效果。

![](http://crazysunj.com/img/GIF_20170330_165026.gif)

![](http://crazysunj.com/img/GIF_20170330_170450.gif)

![](http://crazysunj.com/img/GIF_20170330_171326.gif)

![](http://crazysunj.com/img/GIF_20170330_172021.gif)

库中依赖

```
compile fileTree(include: ['*.jar'], dir: 'libs')
compile 'com.android.support:recyclerview-v7:24.2.0'
compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.1'
compile 'com.android.support:appcompat-v7:24.2.0'
```

你可以自己设置相应的版本依赖，关于recyclerview的版本最好在24.2.0及以下，不然在异步刷新的时候可能会引起GapWork这货的异常问题，不用这家伙总没问题了把。



Gradle依赖

```
maven { url "https://jitpack.io" }
compile 'com.crazysunj:multitypeadapter:1.1.0'
```

这里要说一下的是上面的maven地址，这是因为我们的库依赖于BaseRecyclerViewAdapterHelper。

介绍文章地址:
[打造炫酷的多类型RecyclerView的Adapter](http://crazysunj.com/2017/04/01/%E6%89%93%E9%80%A0%E7%82%AB%E9%85%B7%E7%9A%84%E5%A4%9A%E7%B1%BB%E5%9E%8BRecyclerView%E7%9A%84Adapter/#more)

本人博客地址:
[http://crazysunj.com/](http://crazysunj.com/)

感谢:

[https://github.com/CymChad/BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

[https://github.com/edubarr/header-decor](https://github.com/edubarr/header-decor)






