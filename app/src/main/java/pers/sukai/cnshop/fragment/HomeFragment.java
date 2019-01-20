package pers.sukai.cnshop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import pers.sukai.cnshop.Contains;
import pers.sukai.cnshop.R;
import pers.sukai.cnshop.adapter.HomePageAdapter;
import pers.sukai.cnshop.bean.home.Banner;
import pers.sukai.cnshop.bean.home.Campaign;
import pers.sukai.cnshop.bean.home.HomeCampaign;
import pers.sukai.cnshop.http.BaseCallBack;
import pers.sukai.cnshop.http.OkHttpHelper;

/**
 * Created by sukaidev on 2018/12/4.
 */
public class HomeFragment extends Fragment {

    private View view;

    @ViewInject(R.id.recycle_view)
    RecyclerView recyclerView;
    HomePageAdapter adapter;

    private List<Banner> banners = new ArrayList<>();
    private List<HomeCampaign> campaigns = new ArrayList<>();
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);

        x.view().inject(this,view);

        requestResources();

        return view;
    }

    private void requestResources() {

        String bannerUrl = Contains.API.BANNER_URL+ "?pageId=1";
        String campaignUrl = Contains.API.CAMPAIGN_URL;

        httpHelper.get(bannerUrl, new BaseCallBack<List<Banner>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Banner:onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Response response, List<Banner> bannerList) {
                banners = bannerList;

            }


            @Override
            public void onError(Response response, int Code, Exception e) {

                Toast.makeText(getContext(), "Banner:onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) {

            }
        });

        httpHelper.get(campaignUrl, new BaseCallBack<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Campaign:onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                campaigns = homeCampaigns;
                initRecycleView(view);
            }

            @Override
            public void onError(Response response, int Code, Exception e) {
                Toast.makeText(getContext(), "Campaign:onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) {

            }
        });

    }

    private void initRecycleView(View view) {


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomePageAdapter(banners, campaigns, getContext());
        adapter.setOnCampaignClickListener(new HomePageAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(), campaign.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
