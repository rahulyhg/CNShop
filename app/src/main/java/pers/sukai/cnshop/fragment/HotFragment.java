package pers.sukai.cnshop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refreshview.CustomRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import pers.sukai.cnshop.Contains;
import pers.sukai.cnshop.R;
import pers.sukai.cnshop.adapter.BaseAdapter;
import pers.sukai.cnshop.adapter.HotPageAdapter;
import pers.sukai.cnshop.bean.hot.Page;
import pers.sukai.cnshop.bean.hot.Ware;
import pers.sukai.cnshop.http.BaseCallBack;
import pers.sukai.cnshop.http.OkHttpHelper;

/**
 * Created by sukaidev on 2018/12/4.
 */

public class HotFragment extends Fragment {

    private CustomRefreshView refreshView;
    private HotPageAdapter adapter;

    // 数据
    private List<Ware> wares = new ArrayList<>();

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    // 初始化当前页和每页数量
    private int currentPage = 1;
    private int pageSize = 10;

    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container);
        refreshView = view.findViewById(R.id.refresh_layout);

        initRefresh();
        requestResources();

        return view;
    }

    private void initRefresh() {
        refreshView.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void initRecycleView() {
        switch (state) {
            case STATE_NORMAL:
                adapter = new HotPageAdapter(getContext(), wares, R.layout.template_hot_wares);
                refreshView.setAdapter(adapter);
                adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
                break;
            case STATE_REFRESH:
                adapter.clearData();
                adapter.addData(wares);
                break;
            case STATE_MORE:
                adapter.addData(adapter.getData().size(), wares);
                break;
        }

    }

    private void requestResources() {

        String url = Contains.API.WARE_URL + "?currentPage=" + currentPage + "&pageSize=" + pageSize;

        httpHelper.get(url, new BaseCallBack<Page<Ware>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, Page<Ware> warePage) {
                if (currentPage <= (warePage.getTotalPage())) {
                    wares = warePage.getList();
                    currentPage = warePage.getCurrentPage();
                    initRecycleView();
                } else {
                    refreshView.onNoMore();
                }
            }

            @Override
            public void onError(Response response, int Code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });
    }


    private void refreshData() {
        currentPage = 1;
        state = STATE_REFRESH;

        //非主线程中调用requestResources()方法创建Handler要手动开启Looper并绑定否则报错
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟耗时
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //
                //这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
                //所以需要切换到主线程再进行UI更新操作
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestResources();
                        refreshView.complete();
                    }
                });

            }
        }).start();
    }

    private void loadMoreData() {
        state = STATE_MORE;
        currentPage++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟耗时
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
                //所以需要切换到主线程再进行UI更新操作
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestResources();
                        refreshView.complete();
                    }
                });
            }
        }).start();
    }
}
