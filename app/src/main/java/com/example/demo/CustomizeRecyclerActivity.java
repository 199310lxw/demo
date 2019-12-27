package com.example.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demo.adapters.RefreshLoadMoreAdapter;
import com.example.demo.views.CustomizeRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomizeRecyclerActivity extends AppCompatActivity {
    private  String TAG="Customize";
    private List<String> mStrings;
    private final int TYPE_REFRESH = 1;
    private final int TYPE_LOAD = 2;
    private final int TYPE_NO_LOAD_MORE = 3;

    private View mHeaderView;
    private View mFooterView;

    private int mLoadCount=0;

    private CustomizeRecyclerView mRecyclerView;

    private RefreshLoadMoreAdapter mAdapter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPE_REFRESH:
                    mRecyclerView.refreshEnd();//刷新完成后需调用refreshEnd，更新HeaderView显示状态
                    mAdapter.setDatas(mStrings);
                    //由于自定义RecycleView中使用了自定义的adapter所以数据改变时需调用自定义adapter的notifyDataSetChanged
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                    LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                    tvHeader.setVisibility(View.VISIBLE);
                    llRefreshing.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_LOAD:
                    mAdapter.setDatas(mStrings);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                    LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                    tvLoad.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_NO_LOAD_MORE:
                    mRecyclerView.removeOnLoadMoreListener();//当没有更多数据时可移除加载更多监听
                    tvLoad = mFooterView.findViewById(R.id.tv_load);
                    llLoading = mFooterView.findViewById(R.id.ll_loading);
                    tvLoad.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.INVISIBLE);
                    tvLoad.setText("没有更多数据了");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_customize);
        initView();
    }

    private void initView() {
        initData();
        mRecyclerView=findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LayoutInflater layoutInflater_header = LayoutInflater.from(this);
        mHeaderView=layoutInflater_header.inflate(R.layout.item_header, mRecyclerView, false);
        mRecyclerView.setHeaderView(mHeaderView);//设置HeaderView，需在setLayoutManager后设置否则会报错

        LayoutInflater layoutInflater_footer = LayoutInflater.from(this);
        mFooterView=layoutInflater_footer.inflate(R.layout.item_footer, mRecyclerView, false);
        mRecyclerView.setFooterView(mFooterView);//设置FooterView

        mAdapter =new RefreshLoadMoreAdapter(this,mStrings);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new CustomizeRecyclerView.OnRefreshListener() {//设置下拉刷新监听
            @Override
            public void onStartRefresh() {
                TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                tvHeader.setVisibility(View.INVISIBLE);
                llRefreshing.setVisibility(View.VISIBLE);
                refresh();//监听到开始刷新时调用刷新函数
            }
        });

        mRecyclerView.setOnPullDownListener(new CustomizeRecyclerView.OnPullDownListener() {//设置下拉过程的监听
            @Override
            public void onPullDownProgress(float progress) {//获取下拉的占比，一些特殊要求可能会用到，比如下面实现了下拉过程中字体逐渐变大
                if (progress > 1) {
                    progress = 1;
                }
                TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                tvHeader.setTextSize(15 + 10 * progress);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new CustomizeRecyclerView.OnLoadMoreListener() {//设置上拉加载更多的监听
            @Override
            public void onLoadMoreStart() {
                TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                tvLoad.setVisibility(View.INVISIBLE);
                llLoading.setVisibility(View.VISIBLE);
                loadMore();//监听到开始加载时调用加载更多函数
            }
        });

    }
    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                mStrings.clear();
                for (int i = 0; i < 20; i++) {
                    mStrings.add("栏目 " + i);
                }
                mHandler.sendEmptyMessage(TYPE_REFRESH);
            }
        }).start();
    }
    private void loadMore() {
        mLoadCount++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                if (mLoadCount == 3) {//模拟没有更多数据
                    mHandler.sendEmptyMessage(TYPE_NO_LOAD_MORE);
                } else {
                    for (int i = 0; i < 20; i++) {
                        mStrings.add("新栏目 " + i);
                    }
                    Log.d(TAG, "run: loadMore");
                    mHandler.sendEmptyMessage(TYPE_LOAD);
                }
            }
        }).start();
    }

    private void initData() {
        mStrings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {//初始数据
            mStrings.add("条目 " + i);
        }
    }
}
