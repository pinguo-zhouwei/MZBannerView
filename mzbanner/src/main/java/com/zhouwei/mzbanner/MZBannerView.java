package com.zhouwei.mzbanner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhouwei.mzbanner.transformer.CoverModeTransformer;
import com.zhouwei.mzbanner.transformer.ScaleYTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17/5/26.
 */

public class MZBannerView<T> extends RelativeLayout {
    private static final String TAG = "MZBannerView";
    private CustomViewPager mViewPager;
    private MZPagerAdapter mAdapter;
    private List<T> mDatas;
    private boolean mIsAutoPlay = true;// 是否自动播放
    private int mCurrentItem = 0;//当前位置
    private Handler mHandler = new Handler();
    private int mDelayedTime = 3000;// Banner 切换时间间隔
    private ViewPagerScroller mViewPagerScroller;//控制ViewPager滑动速度的Scroller
    private boolean mIsOpenMZEffect = true;// 开启魅族Banner效果
    private boolean mIsCanLoop = true;// 是否轮播图片
    private LinearLayout mIndicatorContainer;//indicator容器
    private ArrayList<ImageView> mIndicators = new ArrayList<>();
    //mIndicatorRes[0] 为为选中，mIndicatorRes[1]为选中
    private int []mIndicatorRes= new int[]{R.drawable.indicator_normal,R.drawable.indicator_selected};
    private int mIndicatorPaddingLeft = 0;// indicator 距离左边的距离
    private int mIndicatorPaddingRight = 0;//indicator 距离右边的距离
    private int mIndicatorPaddingTop = 0;//indicator 距离上边的距离
    private int mIndicatorPaddingBottom = 0;//indicator 距离下边的距离
    private int mMZModePadding = 0;//在仿魅族模式下，由于前后显示了上下一个页面的部分，因此需要计算这部分padding
    private int mIndicatorAlign = 1;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private BannerPageClickListener mBannerPageClickListener;
    public enum IndicatorAlign{
        LEFT,//做对齐
        CENTER,//居中对齐
        RIGHT //右对齐
    }

    /**
     * 中间Page是否覆盖两边，默认覆盖
     */
    private boolean mIsMiddlePageCover = true;
    public MZBannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public MZBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context,attrs);
        init();
    }

    public MZBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context,attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MZBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttrs(context,attrs);
        init();
    }

    private void readAttrs(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MZBannerView);
        mIsOpenMZEffect = typedArray.getBoolean(R.styleable.MZBannerView_open_mz_mode,true);
        mIsMiddlePageCover = typedArray.getBoolean(R.styleable.MZBannerView_middle_page_cover,true);
        mIsCanLoop = typedArray.getBoolean(R.styleable.MZBannerView_canLoop,true);
        mIndicatorAlign = typedArray.getInt(R.styleable.MZBannerView_indicatorAlign,IndicatorAlign.CENTER.ordinal());
        mIndicatorPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MZBannerView_indicatorPaddingLeft,0);
        mIndicatorPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MZBannerView_indicatorPaddingRight,0);
        mIndicatorPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MZBannerView_indicatorPaddingTop,0);
        mIndicatorPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MZBannerView_indicatorPaddingBottom,0);
        typedArray.recycle();
    }


    private void init(){
      View view = null;
        if(mIsOpenMZEffect){
            view = LayoutInflater.from(getContext()).inflate(R.layout.mz_banner_effect_layout,this,true);
        }else{
            view = LayoutInflater.from(getContext()).inflate(R.layout.mz_banner_normal_layout,this,true);
        }
      mIndicatorContainer = (LinearLayout) view.findViewById(R.id.banner_indicator_container);
      mViewPager = (CustomViewPager) view.findViewById(R.id.mzbanner_vp);
      mViewPager.setOffscreenPageLimit(4);

      mMZModePadding = dpToPx(30);
        // 初始化Scroller
      initViewPagerScroll();

      sureIndicatorPosition();

    }

    /**
     * 是否开启魅族模式
     */
    private void setOpenMZEffect(){
        // 魅族模式
        if(mIsOpenMZEffect){
            if(mIsMiddlePageCover){
                // 中间页面覆盖两边，和魅族APP 的banner 效果一样。
                mViewPager.setPageTransformer(true,new CoverModeTransformer(mViewPager));
            }else{
                // 中间页面不覆盖，页面并排，只是Y轴缩小
                mViewPager.setPageTransformer(false,new ScaleYTransformer());
            }

        }
    }

    /**
     * make sure the indicator
     */
    private void sureIndicatorPosition(){
        if(mIndicatorAlign == IndicatorAlign.LEFT.ordinal()){
            setIndicatorAlign(IndicatorAlign.LEFT);
        }else if(mIndicatorAlign == IndicatorAlign.CENTER.ordinal()){
            setIndicatorAlign(IndicatorAlign.CENTER);
        }else{
            setIndicatorAlign(IndicatorAlign.RIGHT);
        }
    }

    /**
     * 设置ViewPager的滑动速度
     * */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mViewPagerScroller = new ViewPagerScroller(
                 mViewPager.getContext());
            mScroller.set(mViewPager, mViewPagerScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private final Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if(mIsAutoPlay){
                mCurrentItem = mViewPager.getCurrentItem();
                mCurrentItem++;
                if(mCurrentItem == mAdapter.getCount() - 1){
                    mCurrentItem = 0;
                    mViewPager.setCurrentItem(mCurrentItem,false);
                    mHandler.postDelayed(this,mDelayedTime);
                }else{
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.postDelayed(this,mDelayedTime);
                }
            }else{
                mHandler.postDelayed(this,mDelayedTime);
            }
        }
    };



    /**
     * 初始化指示器Indicator
     */
    private void initIndicator(){
        mIndicatorContainer.removeAllViews();
        mIndicators.clear();
        for(int i=0;i<mDatas.size();i++){
            ImageView imageView = new ImageView(getContext());
            if(mIndicatorAlign == IndicatorAlign.LEFT.ordinal()){
                if(i == 0){
                    int paddingLeft = mIsOpenMZEffect ? mIndicatorPaddingLeft+mMZModePadding:mIndicatorPaddingLeft;
                    imageView.setPadding(paddingLeft+6,0,6,0);
                } else{
                    imageView.setPadding(6,0,6,0);
                }

            }else if(mIndicatorAlign == IndicatorAlign.RIGHT.ordinal()){
                if(i == mDatas.size() - 1){
                    int paddingRight = mIsOpenMZEffect ? mMZModePadding + mIndicatorPaddingRight:mIndicatorPaddingRight;
                    imageView.setPadding(6,0,6 + paddingRight,0);
                }else{
                    imageView.setPadding(6,0,6,0);
                }

            }else{
                imageView.setPadding(6,0,6,0);
            }

            if(i == (mCurrentItem % mDatas.size())){
                imageView.setImageResource(mIndicatorRes[1]);
            }else{
                imageView.setImageResource(mIndicatorRes[0]);
            }

            mIndicators.add(imageView);
            mIndicatorContainer.addView(imageView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!mIsCanLoop){
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()){
            // 按住Banner的时候，停止自动轮播
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                int paddingLeft = mViewPager.getLeft();
                float touchX = ev.getRawX();
                // 如果是魅族模式，去除两边的区域
                if(touchX >= paddingLeft && touchX < getScreenWidth(getContext()) - paddingLeft){
                    pause();
                }
                break;
            case MotionEvent.ACTION_UP:
                start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 开始轮播
     * <p>应该确保在调用用了{@link MZBannerView {@link #setPages(List, MZHolderCreator)}} 之后调用这个方法开始轮播</p>
     */
    public void start(){
        // 如果Adapter为null, 说明还没有设置数据，这个时候不应该轮播Banner
        if(mAdapter== null){
            return;
        }
        if(mIsCanLoop){
            pause();
            mIsAutoPlay = true;
            mHandler.postDelayed(mLoopRunnable,mDelayedTime);
        }
    }

    /**
     * 停止轮播
     */
    public void pause(){
        mIsAutoPlay = false;
        mHandler.removeCallbacks(mLoopRunnable);
    }

    /**
     * 设置是否可以轮播
     * @param canLoop
     */
    public void setCanLoop(boolean canLoop){
        mIsCanLoop = canLoop;
        if(!canLoop){
            pause();
        }
    }

    /**
     * 设置BannerView 的切换时间间隔
     * @param delayedTime
     */
    public void setDelayedTime(int delayedTime) {
        mDelayedTime = delayedTime;
    }

    public void addPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     *  添加Page点击事件
     * @param bannerPageClickListener {@link BannerPageClickListener}
     */
    public void setBannerPageClickListener(BannerPageClickListener bannerPageClickListener) {
        mBannerPageClickListener = bannerPageClickListener;
    }

    /**
     * 是否显示Indicator
     * @param visible true 显示Indicator，否则不显示
     */
    public void setIndicatorVisible(boolean visible){
        if(visible){
            mIndicatorContainer.setVisibility(VISIBLE);
        }else{
            mIndicatorContainer.setVisibility(GONE);
        }
    }

    /**
     * set indicator padding
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    public void setIndicatorPadding(int paddingLeft,int paddingTop,int paddingRight,int paddingBottom){
        mIndicatorPaddingLeft = paddingLeft;
        mIndicatorPaddingTop = paddingTop;
        mIndicatorPaddingRight = paddingRight;
        mIndicatorPaddingBottom = paddingBottom;
        sureIndicatorPosition();
    }

    /**
     * 返回ViewPager
     * @return {@link ViewPager}
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 设置indicator 图片资源
     * @param unSelectRes  未选中状态资源图片
     * @param selectRes  选中状态资源图片
     */
    public void setIndicatorRes(@DrawableRes int unSelectRes, @DrawableRes int selectRes){
        mIndicatorRes[0]= unSelectRes;
        mIndicatorRes[1] = selectRes;
    }

    /**
     * 设置数据，这是最重要的一个方法。
     * <p>其他的配置应该在这个方法之前调用</p>
     * @param datas Banner 展示的数据集合
     * @param mzHolderCreator  ViewHolder生成器 {@link MZHolderCreator} And {@link MZViewHolder}
     */
    public void setPages(List<T> datas,MZHolderCreator mzHolderCreator){
        if(datas == null || mzHolderCreator == null){
            return;
        }
        mDatas = datas;
        //如果在播放，就先让播放停止
        pause();

        //增加一个逻辑：由于魅族模式会在一个页面展示前后页面的部分，因此，数据集合的长度至少为3,否则，自动为普通Banner模式
        //不管配置的:open_mz_mode 属性的值是否为true

        if(datas.size() < 3){
            mIsOpenMZEffect = false;
            MarginLayoutParams layoutParams = (MarginLayoutParams) mViewPager.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            mViewPager.setLayoutParams(layoutParams);
            setClipChildren(true);
            mViewPager.setClipChildren(true);
        }
        setOpenMZEffect();
        // 2017.7.20 fix：将Indicator初始化放在Adapter的初始化之前，解决更新数据变化更新时crush.
        //初始化Indicator
        initIndicator();

        mAdapter = new MZPagerAdapter(datas,mzHolderCreator,mIsCanLoop);
        mAdapter.setUpViewViewPager(mViewPager);
        mAdapter.setPageClickListener(mBannerPageClickListener);


        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int realPosition = position % mIndicators.size();
                if(mOnPageChangeListener!=null){
                    mOnPageChangeListener.onPageScrolled(realPosition,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;


                // 切换indicator
                int realSelectPosition = mCurrentItem % mIndicators.size();
                for(int i = 0;i<mDatas.size();i++){
                    if(i == realSelectPosition){
                        mIndicators.get(i).setImageResource(mIndicatorRes[1]);
                    }else{
                        mIndicators.get(i).setImageResource(mIndicatorRes[0]);
                    }
                }
                  // 不能直接将mOnPageChangeListener 设置给ViewPager ,否则拿到的position 是原始的position
                 if(mOnPageChangeListener!=null){
                     mOnPageChangeListener.onPageSelected(realSelectPosition);
                  }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mIsAutoPlay = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsAutoPlay = true;
                        break;

                }
                if(mOnPageChangeListener!=null){
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });


    }

    /**
     * 设置Indicator 的对齐方式
     * @param indicatorAlign {@link IndicatorAlign#CENTER }{@link IndicatorAlign#LEFT }{@link IndicatorAlign#RIGHT }
     */
    public void setIndicatorAlign(IndicatorAlign indicatorAlign) {
        mIndicatorAlign = indicatorAlign.ordinal();
        LayoutParams layoutParams = (LayoutParams) mIndicatorContainer.getLayoutParams();
        if(indicatorAlign == IndicatorAlign.LEFT){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }else if(indicatorAlign == IndicatorAlign.RIGHT){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }else{
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }

        // 2017.8.27 添加：增加设置Indicator 的上下边距。

        layoutParams.setMargins(0,mIndicatorPaddingTop,0,mIndicatorPaddingBottom);
        mIndicatorContainer.setLayoutParams(layoutParams);

    }


    public LinearLayout getIndicatorContainer() {
        return mIndicatorContainer;
    }

    /**
     * 设置ViewPager切换的速度
     * @param duration 切换动画时间
     */
    public void setDuration(int duration){
        mViewPagerScroller.setDuration(duration);
    }

    /**
     * 设置是否使用ViewPager默认是的切换速度
     * @param useDefaultDuration 切换动画时间
     */
    public void setUseDefaultDuration(boolean useDefaultDuration){
        mViewPagerScroller.setUseDefaultDuration(useDefaultDuration);
    }

    /**
     * 获取Banner页面切换动画时间
     * @return
     */
    public int getDuration(){
        return mViewPagerScroller.getScrollDuration();
    }





    public static class  MZPagerAdapter<T> extends PagerAdapter{
        private List<T> mDatas;
        private MZHolderCreator mMZHolderCreator;
        private ViewPager mViewPager;
        private boolean canLoop;
        private BannerPageClickListener mPageClickListener;
        private final int mLooperCountFactor = 500;

        public MZPagerAdapter(List<T> datas, MZHolderCreator MZHolderCreator,boolean canLoop) {
            if(mDatas == null){
                mDatas = new ArrayList<>();
            }
            //mDatas.add(datas.get(datas.size()-1));// 加入最后一个
            for(T t:datas){
                mDatas.add(t);
            }
           // mDatas.add(datas.get(0));//在最后加入最前面一个
            mMZHolderCreator = MZHolderCreator;
            this.canLoop = canLoop;
        }

        public void setPageClickListener(BannerPageClickListener pageClickListener) {
            mPageClickListener = pageClickListener;
        }

        /**
         * 初始化Adapter和设置当前选中的Item
         * @param viewPager
         */
        public void setUpViewViewPager(ViewPager viewPager){
            mViewPager = viewPager;
            mViewPager.setAdapter(this);
            mViewPager.getAdapter().notifyDataSetChanged();
            int currentItem = canLoop ? getStartSelectItem():0;
            //设置当前选中的Item
            mViewPager.setCurrentItem(currentItem);
        }

        private int getStartSelectItem(){
            if(getRealCount() == 0){
                return 0;
            }
            // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
            // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
            int currentItem = getRealCount() * mLooperCountFactor / 2;
            if(currentItem % getRealCount()  ==0 ){
                return currentItem;
            }
            // 直到找到从0开始的位置
            while (currentItem % getRealCount() != 0){
                currentItem++;
            }
            return currentItem;
        }

        public void setDatas(List<T> datas) {
            mDatas = datas;
        }

        @Override
        public int getCount() {
            // 2017.6.10 bug fix
            // 如果getCount 的返回值为Integer.MAX_VALUE 的话，那么在setCurrentItem的时候会ANR(除了在onCreate 调用之外)
            return canLoop ? getRealCount() * mLooperCountFactor : getRealCount();//ViewPager返回int 最大值
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = getView(position,container);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            // 轮播模式才执行
            if(canLoop){
                int position = mViewPager.getCurrentItem();
                 if (position == getCount() - 1) {
                    position = 0;
                    setCurrentItem(position);
                }
            }

        }

        private void setCurrentItem(int position){
            try {
                mViewPager.setCurrentItem(position, false);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }

        /**
         * 获取真实的Count
         * @return
         */
        private int getRealCount(){
            return  mDatas==null ? 0:mDatas.size();
        }

        /**
         *
         * @param position
         * @param container
         * @return
         */
        private View getView(int position,ViewGroup container){

            final int realPosition = position % getRealCount();
            MZViewHolder holder =null;
            // create holder
            holder = mMZHolderCreator.createViewHolder();

            if(holder == null){
                throw new RuntimeException("can not return a null holder");
            }
            // create View
            View view = holder.createView(container.getContext());

            if( mDatas!=null && mDatas.size()>0){
                holder.onBind(container.getContext(),realPosition,mDatas.get(realPosition));
            }

            // 添加点击事件
           view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPageClickListener!=null){
                        mPageClickListener.onPageClick(v,realPosition);
                    }
                }
            });

            return view;
        }


    }

    /**
     *
     ＊由于ViewPager 默认的切换速度有点快，因此用一个Scroller 来控制切换的速度
     * <p>而实际上ViewPager 切换本来就是用的Scroller来做的，因此我们可以通过反射来</p>
     * <p>获取取到ViewPager 的 mScroller 属性，然后替换成我们自己的Scroller</p>
     */
    public static class ViewPagerScroller extends Scroller{
        private int mDuration = 800;// ViewPager默认的最大Duration 为600,我们默认稍微大一点。值越大越慢。
        private boolean mIsUseDefaultDuration = false;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy,mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mIsUseDefaultDuration?duration:mDuration);
        }

        public void setUseDefaultDuration(boolean useDefaultDuration) {
            mIsUseDefaultDuration = useDefaultDuration;
        }

        public boolean isUseDefaultDuration() {
            return mIsUseDefaultDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }


        public int getScrollDuration() {
            return mDuration;
        }
    }

    /**
     * Banner page 点击回调
     */
    public interface BannerPageClickListener{
        void onPageClick(View view, int position);
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
