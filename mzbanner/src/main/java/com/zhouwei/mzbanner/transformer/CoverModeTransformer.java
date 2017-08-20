package com.zhouwei.mzbanner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhouwei on 17/8/20.
 */

public class CoverModeTransformer implements ViewPager.PageTransformer {

    private float reduceX = 0.0f;
    private float itemWidth = 0;
    private float offsetPosition = 0f;
    private int mCoverWidth;
    private float mScaleMax = 1.0f;
    private float mScaleMin = 0.9f;
    private ViewPager mViewPager;
    public CoverModeTransformer(ViewPager pager){
        mViewPager = pager;
    }

    @Override
    public void transformPage(View view, float position) {
        if (offsetPosition == 0f) {
            float paddingLeft = mViewPager.getPaddingLeft();
            float paddingRight = mViewPager.getPaddingRight();
            float width = mViewPager.getMeasuredWidth();
            offsetPosition = paddingLeft / (width - paddingLeft - paddingRight);
        }
        float currentPos = position - offsetPosition;
        if (itemWidth == 0) {
            itemWidth = view.getWidth();
            //由于左右边的缩小而减小的x的大小的一半
            reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
        }
        if (currentPos <= -1.0f) {
            view.setTranslationX(reduceX + mCoverWidth);
            view.setScaleX(mScaleMin);
            view.setScaleY(mScaleMin);
        } else if (currentPos <= 1.0) {
            float scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
            float translationX = currentPos * -reduceX;
            if (currentPos <= -0.5) {//两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                view.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
            } else if (currentPos <= 0.0f) {
                view.setTranslationX(translationX);
            } else if (currentPos >= 0.5) {//两个view中间的临界，这时两个view在同一层
                view.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
            } else {
                view.setTranslationX(translationX);
            }
            view.setScaleX(scale + mScaleMin);
            view.setScaleY(scale + mScaleMin);
        } else {
            view.setScaleX(mScaleMin);
            view.setScaleY(mScaleMin);
            view.setTranslationX(-reduceX - mCoverWidth);
        }

    }
}

