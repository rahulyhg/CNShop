package pers.sukai.cnshop.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.refreshview.CustomRefreshView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import pers.sukai.cnshop.Contains;
import pers.sukai.cnshop.R;
import pers.sukai.cnshop.adapter.BaseAdapter;
import pers.sukai.cnshop.adapter.CategoryAdapter;
import pers.sukai.cnshop.adapter.WaresAdapter;
import pers.sukai.cnshop.bean.category.Category;
import pers.sukai.cnshop.bean.home.Banner;
import pers.sukai.cnshop.bean.hot.Page;
import pers.sukai.cnshop.bean.hot.Ware;
import pers.sukai.cnshop.http.BaseCallBack;
import pers.sukai.cnshop.http.OkHttpHelper;

/**
 * Created by sukaidev on 2018/12/4.
 */

public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recycle_view_discover)
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    @ViewInject(R.id.home_slider)
    private SliderLayout slider;

    @ViewInject(R.id.custom_indicator)
    private PagerIndicator indicator;

    @ViewInject(R.id.custom_refresh)
    private CustomRefreshView refreshLayout;
    private WaresAdapter waresAdapter;

    private int currentPage = 1;
    private int pageSize = 10;
    private int categoryId = 1;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null, false);

        x.view().inject(this,view);

        requestBannerResource();
        requestCategory();
        initRefresh();

        return view;
    }

    private void initCategoryRecyclerView(List<Category> categories) {

        categoryAdapter = new CategoryAdapter(getContext(), categories);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        categoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position <= 9) {
                    Category category = categoryAdapter.getItem(position);
                    categoryId = category.getId();
                    currentPage = 1;
                    state = STATE_NORMAL;
                    requestWaresData(categoryId);
                }
            }
        });
    }

    private void initSlider(List<Banner> banners) {
        if (banners != null && banners.size() > 0) {
            for (Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(getContext());
                sliderView
                        .description(banner.getTitle())
                        .image(banner.getUrl())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                            }
                        });
                slider.addSlider(sliderView);
            }
            slider.setCustomIndicator(indicator);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
            slider.setDuration(3000);
            slider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

            });

        }
    }

    private void initRefreshRecycleView(List<Ware> wares) {

        switch (state) {
            case STATE_NORMAL:
                waresAdapter = new WaresAdapter(getContext(), wares);
                refreshLayout.setAdapter(waresAdapter);
                refreshLayout.getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2));
                waresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            case STATE_REFRESH:
                waresAdapter.clearData();
                waresAdapter.addData(wares);
                waresAdapter.notifyDataSetChanged();
                break;
            case STATE_MORE:
                waresAdapter.addData(waresAdapter.getData().size(), wares);
                waresAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initRefresh() {
        refreshLayout.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
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
                        requestWaresData(categoryId);
                        refreshLayout.complete();
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
                        requestWaresData(categoryId);
                        refreshLayout.complete();
                    }
                });
            }
        }).start();
    }

    private void requestCategory() {
        String url = Contains.API.CATEGORY_URL;

        okHttpHelper.get(url, new BaseCallBack<List<Category>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, List<Category> categories) {

                initCategoryRecyclerView(categories);
                if (categories != null && categories.size() > 0) {
                    requestWaresData(categories.get(0).getId());
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


    private void requestBannerResource() {
        String url = Contains.API.BANNER_URL + "?pageId=1";

        okHttpHelper.get(url, new BaseCallBack<List<Banner>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> categoryBanners) {
                initSlider(categoryBanners);
            }


            @Override
            public void onError(Response response, int Code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });
    }

    private void requestWaresData(int categoryId) {

        String url = Contains.API.CATEGORY_WARE_URL + "?currentPage=" + currentPage + "&pageSize=" + pageSize + "&categoryId=" + categoryId;


        okHttpHelper.get(url, new BaseCallBack<Page<Ware>>() {

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, Page<Ware> warePage) {
                if (currentPage <= warePage.getTotalPage()) {
                    currentPage = warePage.getCurrentPage();
                    initRefreshRecycleView(warePage.getList());
                } else refreshLayout.onNoMore();
            }

            @Override
            public void onError(Response response, int Code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });
    }

    @Override
    public void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }

}