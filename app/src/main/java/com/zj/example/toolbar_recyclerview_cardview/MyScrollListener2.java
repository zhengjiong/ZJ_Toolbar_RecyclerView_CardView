package com.zj.example.toolbar_recyclerview_cardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;

/**
 * create by zhengjiong
 * Date: 2015-04-06
 * Time: 17:07
 */
public abstract class MyScrollListener2 extends RecyclerView.OnScrollListener{
    private static final String TAG = "MyScrollListener2";

    private static final float HIDE_THRESHOLD = 20;
    private static final float SHOW_THRESHODE = 60;

    private boolean isVisible;

    //每次滑動相對於上次的距離
    private int mToolbarOffset;
    private int mToolbarHeight;

    //滾動的距離
    private int mTotalScrolledDistance;

    public MyScrollListener2(Context context) {
        mToolbarHeight = getToolbarHeight(context);
    }

    /**
     * 当向上滚动的时候offset这个值将增加（但是我们并不希望这个值大于Toolbar的高度），
     * 而向下滚动的时候这个值将减小（同样，我们也不希望减小到小于0），
     * 你很快会知道为什么我们要作此限制的原因。虽然上面的代码已经有了限制，
     * 但是在很短的时间内（比如fling的时候），还是有可能超过这个区间，
     * 因此需要调用clipToolbarOffset()方法来做二次限制，确保mToolbarOffset在0到Toolbar高度的范围内，
     * 否则会出现抖动闪烁的情况。
     * @param recyclerView
     * @param dx dx, dy参数分别是横向和纵向的滚动距离，准确是的是上一次滾動和下一次滚动事件之间的偏移量，而不是总的滚动距离
     * @param dy dy>0意味着上滚，dy<0意味着下滚
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        clipToolbarOffset();
        //Log.i(TAG, "mToolbarOffset=" + mToolbarOffset);
        move(mToolbarOffset);
        if ((mToolbarOffset < mToolbarHeight && dy > 0)//手指上滑,並且offset小於Toolbar高度
                || (mToolbarOffset > 0 && dy < 0)) {//手指下滑,並且offset大於0

            mToolbarOffset += dy;
        }
        if (mTotalScrolledDistance < 0) {
            mTotalScrolledDistance = 0;
        } else {
            mTotalScrolledDistance += dy;
        }
    }

    /**
     * 如果我们放开了手指并且列表停止滚动（这是就是RecyclerView.SCROLL_STATE_IDLE状态），
     * 我们需要检查当前Toolbar是否可见，如果是可见的，
     * 就在mToolbarOffset大于HIDE_THRESHOLD的时候隐藏它，
     * 而在mToolbarOffset大于SHOW_THRESHOLD的时候显示它。
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {//當滑動停止的時候
            //Log.i(TAG, "onScrollStateChanged ---> SCROLL_STATE_IDLE");
            /**
             * 如果我们列表在顶部，我们向上滑动一点点，Toolbar隐藏的同时
             * 會顯示出paddingTop的空白,所以這裡需要判斷滾動距離是否已經大於Toolbar的高度,
             * 沒有超過Toolbar的高度的話,就顯示
             *
             * demo1是用headerview來判斷的(position=0):"如果當前屏幕上顯示的第一個View是HeaderView就不隱藏"
             */
            if (mTotalScrolledDistance < mToolbarHeight) {
                setVisible();
            } else {
                if (isVisible) {//當前是Toolbar是顯示的狀態
                    if (mToolbarOffset > HIDE_THRESHOLD) {//大於隱藏設置的閥值
                        setInVisible();
                    } else {//如果沒有大於隱藏的閥值,則執行顯示相關代碼
                        setVisible();
                    }

                } else {//當前Toolbar是隱藏的狀態
                    /**
                     * 滑動距離通過(mToolbarHeight - mToolbarOffset)來計算
                     * 因為當前是隱藏狀態的時候,mToolbarOffset是等於mToolbarHeight的,
                     * 如果手指向下滑動,需要用Toolbar總的高度減去現在的偏移量,才是剛剛手指滑動的距離
                     * 168-130=38
                     */
                    if ((mToolbarHeight - mToolbarOffset) > SHOW_THRESHODE) {//手指滑動距離大於顯示設置的閥值
                        setVisible();
                    } else {
                        setInVisible();
                    }
                }
            }
        }
    }

    private void setVisible() {
        /**
         * 如果mToolbarOffset = 0(不可能會<0),則代表Toolbar已經處於完全顯示的狀態,就不需要再執行顯示動畫
         * 比如說在move的過程中就處於完全顯示狀態,手指放開的時候就不需要再執行顯示動畫
         */
        if (mToolbarOffset > 0) {
            //顯示
            show();
            mToolbarOffset = 0;
        }
        isVisible = true;
    }

    private void setInVisible() {
        /**
         * 如果mToolbarOffset = mToolbarHeight(不可能會>0)就代表Toolbar已經完全隱藏了
         * 比如說在move的過程中就已經完全隱藏了,手指放開的時候就不需要再執行隱藏動畫
         */
        if (mToolbarOffset < mToolbarHeight) {
            //隱藏
            hide();
            mToolbarOffset = mToolbarHeight;
        }
        isVisible = false;
    }

    /**
     * 防止因手速太快,dy大於mToolbarHeight,或者mToolbarOffset<0
     */
    private void clipToolbarOffset() {
        if (mToolbarOffset > mToolbarHeight) {
            mToolbarOffset = mToolbarHeight;
        }else if (mToolbarOffset < 0) {
            mToolbarOffset = 0;
        }
    }

    public abstract void move(int toolbarOffset);
    public abstract  void hide();
    public abstract  void show();

    public static int getToolbarHeight(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int value = (int) typedArray.getDimension(0, 0);
        typedArray.recycle();
        return value;
    }

    public static int getTabHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tab_height);
    }
}
