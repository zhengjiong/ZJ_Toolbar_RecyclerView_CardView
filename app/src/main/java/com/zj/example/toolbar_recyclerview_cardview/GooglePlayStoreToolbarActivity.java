package com.zj.example.toolbar_recyclerview_cardview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0319/2618.html
 *
 * create by zhengjiong
 * Date: 2015-04-06
 * Time: 11:31
 */
public class GooglePlayStoreToolbarActivity extends ActionBarActivity{

    private static final String TAG = "GooglePlayStoreToolbarActivity";
    private int mToolbarHeight;

    private View mHeaderView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<String> mItems = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.BlueTheme);//設置主題,必須放在super之前
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googleplaystore_toolbar_layout);

        mHeaderView = findViewById(R.id.header);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mToolbarHeight = MyScrollListener2.getToolbarHeight(this);

        initData();
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        int padding = mToolbarHeight + MyScrollListener2.getTabHeight(this);
        mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), padding, mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        mRecyclerView.setClipToPadding(false);

        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter());
        mRecyclerView.setOnScrollListener(new MyScrollListener2(this) {

            @Override
            public void move(int toolbarOffset) {
                mHeaderView.setTranslationY(-toolbarOffset);
            }

            @Override
            public void hide() {
                mHeaderView.animate()
                        .translationY(-mToolbarHeight)
                        .setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            public void show() {
                mHeaderView.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("GooglePlayStoreToobar Demo");
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    private void initData() {
        for (int i = 0; i <= 30; i++) {
            mItems.add(String.valueOf("item " + i));
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public MyViewHolder(View parentView, TextView textView) {
            super(parentView);

            mTextView = textView;
        }

        public static MyViewHolder newInstance(View parentView) {
            TextView textView = (TextView) parentView.findViewById(R.id.textview);
            return new MyViewHolder(parentView, textView);
        }

        public void setItemValue(String value) {
            mTextView.setText(value);
        }
    }

    class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(GooglePlayStoreToolbarActivity.this).inflate(R.layout.item_list_layout, viewGroup, false);
            return MyViewHolder.newInstance(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String value = mItems.get(i);
            ((MyViewHolder) viewHolder).setItemValue(value);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
