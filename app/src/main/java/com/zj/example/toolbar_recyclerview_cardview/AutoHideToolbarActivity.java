package com.zj.example.toolbar_recyclerview_cardview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zhengjiong
 * Date: 2015-04-04
 * Time: 14:56
 */
public class AutoHideToolbarActivity extends ActionBarActivity{
    private static final String TAG = "AutoHideToolbarActivity";
    private Toolbar mToolbar;
    private ImageView mFabButton;
    private RecyclerView mRecyclerView;

    private List<String> mItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_hide_toolbar_layout);

        mFabButton = (ImageView) findViewById(R.id.img_fab_btn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        initData();
        initToolbar();
        initRecyclerView();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mItems.add(String.valueOf("item" + i));
        }
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter());

        mRecyclerView.setOnScrollListener(new MyScrollListener(){

            @Override
            public void show() {
                mToolbar.animate().setDuration(300).translationY(0).setInterpolator(new DecelerateInterpolator(2));

                mFabButton.animate().setDuration(300).translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }

            @Override
            public void hide() {
                mToolbar.animate().setDuration(300).translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFabButton.getLayoutParams();
                int bottomMargin = layoutParams.bottomMargin;
                mFabButton.animate().setDuration(300).translationY(bottomMargin + mFabButton.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("AutoHideToolbarActivity");
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }

        public void setTextView(String value){
            textView.setText(value);
        }
    }

    class MyHeaderHolder extends RecyclerView.ViewHolder{
        public MyHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        static final int TYPE_ITEM = 1;
        static final int TYPE_HEADER = 2;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {

            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_list_layout, parentView, false);
                return new MyViewHolder(view);
            } else {
                View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.header_list_layout, parentView, false);
                return new MyHeaderHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (!isPositionHeader(position)) {
                MyViewHolder holder = (MyViewHolder) viewHolder;
                String itemText = mItems.get(position - 1); // header
                holder.setTextView(itemText);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }

        }

        @Override
        public int getItemCount() {
            return getBasicItemCount() + 1; // header
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        public int getBasicItemCount() {
            return mItems == null ? 0 : mItems.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

