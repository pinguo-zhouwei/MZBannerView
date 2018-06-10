### MZBannerView

[![](https://img.shields.io/badge/release-v2.0.2-blue.svg)](https://github.com/pinguo-zhouwei/MZBannerView/releases)
[![](https://img.shields.io/badge/MZBannerView-v2.0.2-brightgreen.svg)](https://github.com/pinguo-zhouwei/MZBannerView)
[![](https://img.shields.io/badge/API-16%2B-brightgreen.svg)](https://github.com/pinguo-zhouwei/MZBannerView)
[![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/pinguo-zhouwei/MZBannerView)
[![](https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg)]()
[![](https://img.shields.io/badge/%E7%AE%80%E4%B9%A6-%E4%BE%9D%E7%84%B6%E8%8C%83%E7%89%B9%E7%A8%80%E8%A5%BF-red.svg)](https://www.jianshu.com/u/35167a70aa39)
[![](https://img.shields.io/badge/%E5%85%AC%E4%BC%97%E5%8F%B7-Android%E6%8A%80%E6%9C%AF%E6%9D%82%E8%B4%A7%E9%93%BA-blue.svg)](https://mp.weixin.qq.com/mp/homepage?__biz=MzIxMTg5NjQyMA==&hid=1&sn=6914e8d560a0d524f937af629488b6b5&scene=1&devicetype=iMac+MacBookPro12%2C1+OSX+OSX+10.11.5+build(15F34)&version=12020810&lang=zh_CN&nettype=WIFI&ascene=0&session_us=gh_d32aef65c1b3&fontScale=100&pass_ticket=Wty8DxZurpU2xxUkFPtXL5oqMrigpoVVh1agsDKXukmezsMUUJaWb%2FdIlcrhpQOq&wx_header=1&uin=OTU2MjA1MzIy&key=ebe51358ecae289391e767e9274e1a2bf8576a9e4fd5bc2b31956a627346413f84f752a9dd302e2a157b6fb84991a4142881a8143d21cdd25b805b8ba327e3ba78443337a68c9a1b32095d9adc30ec4b)

现在的APP Banner大多数千篇一律，前几天看到魅族手机上所有魅族自家APP上的Banner效果不错，于是就想着来仿着做一个类似的效果。因此就有了这个库。但是为了使用方便，这个库不仅仅只有仿魅族效果的BannerView 来使用，还可以当作普通的BannerView 来使用，还可以当作一个ViewPager 来使用。使用很方便，具体使用方法和API 请看后面的示例。


![meizuappbanner](image/meizuappbanner.gif) －－－ ![魅族bannerView](image/魅族Banner_fial.gif)
左图为魅族APP上的Banner效果，右图是高仿效果。



**MZBannerView 有以下功能：**

1 . 仿魅族BannerView 效果。

2 . 当普通Banner 使用

3 . 当普通ViewPager 使用。

4 . 当普通ViewPager使用(有魅族Banner效果)

5 . 仿爱奇艺Banner效果。

### Demo APK 

gif图片有点模糊，可以扫描下方二维码下载APK体验

![demo二维码.png](image/demo二维码.png)

### 相关博客

[ViewPager系列之 仿魅族应用的广告BannerView](http://www.jianshu.com/p/653680cfe877)

### 更新日志

**v1.1.1 :** 增加按住Banner 停止轮播，松开开始自动轮播的功能

**v1.1.0 :** fix 在从网上获取数据后，banner 显示 造成 ANR 的bug(如果在onCreate()中设置资源显示则没问题)

**v1.1.2 :** fix 更改数据之后，调用setPages重新刷新数据会crush的bug

**v2.0.0 :**  
              
1,add: 添加仿魅族Banner效果，中间Page覆盖两边。
              
2,fix 部分bug: 添加OnPageChangeListener 回调 pisition 不对的bug.

**v2.0.1**

1 , 部分代码优化
2，添加设置Indicator的api,代码中和xml中都可以设置：
代码中：
```java
 mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
 mMZBanner.setIndicatorPadding(10,0,0,150);

```
xml中：
```java
 app:indicatorAlign="center"
 app:indicatorPaddingLeft="10dp"
 app:indicatorPaddingBottom="50dp"
 app:indicatorPaddingRight="10dp"
 app:indicatorPaddingTop="50dp"
```
**v2.0.2**

1,修复部分bug
2,代码优化，增加在代码中可以设置是否自动轮播


>重要： 代码中所有的配置项都应该在`setPages()` 之前调用，不然有可能会无效


### Dependency
Add it in your root build.gradle at the end of repositories:

```java
allprojects {
     repositories {
          ...
          maven { url 'https://jitpack.io' }
     }
}
```
Step 2. Add the dependency
```
dependencies {
         compile 'com.github.pinguo-zhouwei:MZBannerView:v2.0.2'
}
```

### 自定义属性

| 属性名      | 属性意义   |  取值  |
| --------   | -----:   | :----: |
| open_mz_mode       | 是否开启魅族模式     |   true 为魅族Banner效果，false 则普通Banner效果   |
| canLoop        | 是否轮播     |   true 轮播，false 则为普通ViewPager   |
| indicatorPaddingLeft        | 设置指示器距离左侧的距离      |   单位为 dp 的值     |
| indicatorPaddingRight        | 设置指示器距离右侧的距离     |     单位为 dp 的值  |
| indicatorAlign        | 设置指示器的位置      |   有三个取值：left 左边，center 剧中显示，right 右侧显示   |
| middle_page_cover        | 设置中间Page是否覆盖（真正的魅族Banner效果）     |   true 覆盖，false 无覆盖效果 |


### 使用方法

1 . xml 布局文件
```java
 <com.zhouwei.mzbanner.MZBannerView
       android:id="@+id/banner"
       android:layout_width="match_parent"
       android:layout_height="200dp"
       android:layout_marginTop="10dp"
       app:open_mz_mode="true"
       app:canLoop="true"
       app:indicatorAlign="center"
       app:indicatorPaddingLeft="10dp"
       />
```
2 . activity中代码：
```java
        mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
     
       // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

 public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }
```
3 .如果是当Banner使用，注意在onResume 中调用start()方法，在onPause中调用 pause() 方法。如果当普通ViewPager使用，则不需要。
```java
 @Override
    public void onPause() {
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }
```

**通过`open_mz_mode `、`middle_page_cover`和`canLoop `这3个属性来控制MZBannerView 是用作Banner还是普通ViewPager,能控制多种Banner展示效果：**

1 . 魅族Banner 效果，中间Page覆盖两边。

```java
 app:open_mz_mode="true"
 app:canLoop="true"
 app:middle_page_cover="true"
```

![Page 覆盖效果](http://upload-images.jianshu.io/upload_images/3513995-39f495bf8a915ad0.gif?imageMogr2/auto-orient/strip)

2 普通banner 使用。

```java
app:open_mz_mode="false"
app:canLoop="true"
```
![普通Banner效果](http://upload-images.jianshu.io/upload_images/3513995-39f495bf8a915ad0.gif?imageMogr2/auto-orient/strip)

上图中的底部BannerView 示例。

3 仿魅族Banner 效果，中间Page不覆盖。

```java
 app:open_mz_mode="true"
 app:canLoop="true"
 app:middle_page_cover="false"
```

![Page不覆盖效果](http://upload-images.jianshu.io/upload_images/3513995-9aa19c594f932982.gif?imageMogr2/auto-orient/strip)

4 仿爱奇艺Banner效果，Page 之间有间隔。

```java
 <com.zhouwei.mzbanner.MZBannerView
       android:id="@+id/banner_normal"
       android:layout_width="match_parent"
       android:layout_height="150dp"
       android:layout_marginTop="10dp"
       app:open_mz_mode="true"
       app:middle_page_cover="false"
       app:canLoop="true"
       app:indicatorAlign="center"
       />
```

除了上面的代码外，还需要在Page 的item 布局里面设置左右Margin:

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <ImageView
        android:id="@+id/banner_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="centerCrop"
        android:layout_marginLeft="4dp"
        android:layout_marginRight="4dp"
        />

```
效果如下：

![仿爱奇艺Banner效果](http://upload-images.jianshu.io/upload_images/3513995-f753f5449512c06c.gif?imageMogr2/auto-orient/strip)

5 有魅族Banner 效果的普通ViewPager 使用


```java
 app:open_mz_mode="true"
 app:canLoop="false"
```

![魅族Banner效果的普通ViewPager](http://upload-images.jianshu.io/upload_images/3513995-29d27def0753dd86.gif?imageMogr2/auto-orient/strip)

6 普通ViewPager 使用

```java
 app:canLoop="false"
 app:open_mz_mode="false"
```

 ![普通ViewPager](http://upload-images.jianshu.io/upload_images/3513995-6cb035771cda9870.gif?imageMogr2/auto-orient/strip)

上面都是用Banner 展示的本地数据，但是项目中我们一般都是从网络获取Banner 数据，具体参考：`RemoteTestFragment.java`

![从网络获取数据](image/network_data.gif)




### 其他对外API
```java
    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    //开始轮播
     start()
    //停止轮播
     pause()

    //设置BannerView 的切换时间间隔
     setDelayedTime(int delayedTime)
    // 设置页面改变监听器
    addPageChangeLisnter(ViewPager.OnPageChangeListener onPageChangeListener)

    //添加Page点击事件
     setBannerPageClickListener(BannerPageClickListener bannerPageClickListener)
    //设置是否显示Indicator
    setIndicatorVisible(boolean visible)
    // 获取ViewPager
    ViewPager getViewPager()
    // 设置 Indicator资源
    setIndicatorRes(int unSelectRes,int selectRes)
    //设置页面数据
    setPages(List<T> datas,MZHolderCreator mzHolderCreator)
    //设置指示器显示位置
    setIndicatorAlign(IndicatorAlign indicatorAlign)
    //设置ViewPager（Banner）切换速度
    setDuration(int duration)
```
因为是对ViewPager的包装，所有要设置某些ViewPager的属性，可以通过getViewPager 获取到ViewPager再设置对应属性

### 效果图：

1, BannerView 轮播效果图：

![魅族bannerView](image/魅族Banner_fial.gif) 


### Thanks
 感谢[Android-ConvenientBanner](https://github.com/saiwu-bigkoo/Android-ConvenientBanner),Android-ConvenientBanner 是一个不错的Banner库，我也参考了其中的部分代码
 有兴趣的可以去看一下这个库。
 感谢[ScaleViewPager](https://github.com/liuyuejinqiu/ScaleViewPager) 提供中间Page覆盖效果的思路。
 
### 联系方式
 简书:[http://www.jianshu.com/u/35167a70aa39](http://www.jianshu.com/u/35167a70aa39)
 
 掘金：[https://juejin.im/user/56949a9960b2e058a42be0ba](https://juejin.im/user/56949a9960b2e058a42be0ba)
 
 公众号：**Android技术杂货铺**
 
 欢迎关注我的公众号，第一时间获取我的博客更新提醒，以及更多有价值的原创Android干货文章、职场经验、面试技巧等等。
 长按下方二维码即可关注。

 ![gzh.jpg](image/gzh.jpg)
 

### License
  
  ```
     Copyright (C) 2017 zhouwei
  
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
  
         http://www.apache.org/licenses/LICENSE-2.0
  
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
  ```