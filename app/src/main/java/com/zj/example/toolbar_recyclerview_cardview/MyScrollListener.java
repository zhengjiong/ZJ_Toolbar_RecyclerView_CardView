package com.zj.example.toolbar_recyclerview_cardview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 *
 * create by zhengjiong
 * Date: 2015-04-05
 * Time: 12:27
 */
public abstract class MyScrollListener extends RecyclerView.OnScrollListener{
    private static final String TAG = "MyScrollListener";

    private int mScrollDistance = 0;
    private int mMaxScrollDistance = 30;
    private boolean isVisible = true;

    public MyScrollListener() {
        super();
    }

    /**
     * dx, dy参数分别是横向和纵向的滚动距离，准确是的是上一次滾動和下一次滚动事件之间的偏移量，而不是总的滚动距离
     *
     * dy>0手指向上滚，dy<0手指向下滚
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.i(TAG, "onScrolled dx=" + dx + " ,dy=" + dy);

        /**
         * 计算出滚动的总距离（dy相加），
         * 只在Toolbar隐藏且下滚或者Toolbar未隐藏且上滚的時候才計算,因为我们只需要處理這兩種情況
         */
        if (isVisible && dy > 0 || !isVisible && dy < 0) {
            mScrollDistance += dy;
        }

        /**
         * 在隐藏Toolbar的时候会在列表的顶部留下一段空白区域（也就是header区域,随着滚动空白区域会消失），
         * 判斷一下,當划过header之后才隐藏Toolbar
         */
        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int currentVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();//當前屏幕上第一個item的position
        if (currentVisiblePosition == 0) {//當前屏幕第一個Item為header
            /**
             * 當前屏幕第一個View如果是Header的話,如果Toolbar隱藏了就馬上顯示出來,比如說:
             * 當手指上劃劃到剛好當前屏幕第一個View是Item0的時候,這個時候Toolbar剛好隱藏,這個時候如果手指下滑,當前第一個View變成Header
             * 這個時候,Toobar如果不顯示出來,就會顯示一個空白的Header在頂部,所以需要顯示Toolbar
             */
            if (!isVisible) {
                isVisible = true;
                show();
            }
        } else {
            if (isVisible && mScrollDistance > mMaxScrollDistance) {
                isVisible = false;
                mScrollDistance = 0;

                hide();
            }else if (!isVisible && (-mScrollDistance > mMaxScrollDistance)) {
                isVisible = true;
                mScrollDistance = 0;
                show();
            }
        }

    }

    public abstract void show();

    public abstract void hide();
}
