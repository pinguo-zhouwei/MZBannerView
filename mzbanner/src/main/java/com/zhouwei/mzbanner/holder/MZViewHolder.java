package com.zhouwei.mzbanner.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by zhouwei on 17/5/26.
 */

public interface MZViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
