# ZhihuUserProfile

按照知乎个人主页的交互逻辑实现了一个`ViewGroup`,继承自`RelativeLayout`。写这个的主要原因是去年用nexus6的时候就感觉这个页面左右滑动的时候（在头部可见的情况下）卡卡的，前几天买了一加3T,居然还是有点卡卡的，看了下GPU条形图惊呆了。分析了一下布局，能猜出个大概目前是怎么实现的，卡有多种多样的原因，但是由其他页面的流畅度来看，基本能确定的是卡是View本身导致的。于是就按照我能看到的交互实现了一个。

知乎的GPU条形图：  
![](http://omy50xsvp.bkt.clouddn.com/17-4-3/32200561-file_1491234271066_1316a.jpg?imageView/2/w/300/q/90)

我的实现：  
![](http://omy50xsvp.bkt.clouddn.com/17-4-3/88240720-file_1491234218641_bcb5.jpg?imageView/2/w/300/q/90)

> 本来是想用`Behavior`做的，顺便深入研究下，没想到跟去年一样，官方给的`NestedScroll`遇上业务需求各种坑，于是还是手写了一个`ViewGroup`，缺点就是扩展性略差。
