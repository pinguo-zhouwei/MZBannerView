### MZBannerView

现在的APP Banner大多数千篇一律，前几天看到魅族手机上所有魅族自家APP上的Banner效果不错，于是就想着来仿着做一个类似的效果。因此就有了这个库。但是为了使用方便，这个库不仅仅只有仿魅族效果的BannerView 来使用，还可以当作普通的BannerView 来使用，还可以当作一个ViewPager 来使用。使用很方便，具体使用方法和API 请看后面的示例。


![meizuappbanner](image/meizuappbanner.gif) －－－ ![魅族bannerView](image/MZBannerView.gif)
左图为魅族APP上的Banner效果，右图是高仿效果。



**MZBannerView 有以下功能：**

1 . 仿魅族BannerView 效果。

2 . 当普通Banner 使用

3 . 当普通ViewPager 使用。

4 . 当普通ViewPager使用(有魅族Banner效果)

### Demo APK 

gif图片有点模糊，可以扫描下方二维码下载APK体验

![demo二维码.png](image/demo二维码.png)


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
         compile 'com.github.pinguo-zhouwei:MZBannerView:v1.0.1'
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

**通过`open_mz_mode `和`canLoop `这两个属性来控制MZBannerView 是用作Banner还是普通ViewPager,有4种组合方式**

1，仿魅族BannerView(默认的模式)
 ```java
 app:open_mz_mode="true"
 app:canLoop="true"
``` 
2, 普通BannerView 
 ```java
 app:open_mz_mode="false"
 app:canLoop="true"
``` 
3 ,普通ViewPager (有魅族Banner的切换动画)
 ```java
 app:open_mz_mode="true"
 app:canLoop="false"
``` 
4, 普通ViewPager
 ```java
 app:open_mz_mode="false"
 app:canLoop="false"
``` 
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

![魅族bannerView](image/魅族bannerView.gif) 

2 ，普通ViewPager效果图:

![MZBanner普通ViewPager效果.gif](image/MZBanner普通ViewPager效果.gif)

### Thanks
 感谢[Android-ConvenientBanner](https://github.com/saiwu-bigkoo/Android-ConvenientBanner),Android-ConvenientBanner 是一个不错的Banner库，我也参考了其中的部分代码
 有兴趣的可以去看一下这个库。

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