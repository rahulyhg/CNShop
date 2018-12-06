package pers.sukai.cnshop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.bean.home.Banner;
import pers.sukai.cnshop.bean.home.Campaign;
import pers.sukai.cnshop.bean.home.HomeCampaign;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_SLIDER = 0;
    private static final int VIEW_TYPE_LEFT = 1;
    private static final int VIEW_TYPE_RIGHT = 2;

    private List<Banner> banners;
    private List<HomeCampaign> campaigns;
    private Context mContext;
    private LayoutInflater mInflater;

    private OnCampaignClickListener mListener;
    private View sliderView;
    private boolean hasInit = false;

    public HomePageAdapter(List<Banner> listBanner, List<HomeCampaign> listCampaign, Context mContext) {
        this.banners = listBanner;
        this.campaigns = listCampaign;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public HomePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SLIDER) {
            sliderView = mInflater.inflate(R.layout.home_slider, parent, false);
            return new ViewHolder(sliderView);
        } else if (viewType == VIEW_TYPE_RIGHT) {
            return new ViewHolder(mInflater.inflate(R.layout.home_cardview1, parent, false));
        } else {
            return new ViewHolder(mInflater.inflate(R.layout.home_cardview2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == 0) {
            if (banners != null && !hasInit) {
                for (Banner banner : banners) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.
                            description(banner.getTitle())
                            .image(banner.getUrl())
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                }
                            });
                    holder.sliderLayout.addSlider(sliderView);
                }
                hasInit = true;
            }
            holder.sliderLayout.setCustomIndicator(holder.indicator);
            holder.sliderLayout.setCustomAnimation(new DescriptionAnimation());
            holder.sliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
            holder.sliderLayout.setDuration(3000);
            holder.sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
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

        } else {
            HomeCampaign homeCampaign = campaigns.get(position - 1);
            holder.textTitle.setText(homeCampaign.getTitle());
            Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageBig);
            Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageTop);
            Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageBottom);
        }

    }


    @Override
    public int getItemCount() {

        return campaigns.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_SLIDER;
        } else {
            if (position % 2 == 0) {
                return VIEW_TYPE_RIGHT;
            } else return VIEW_TYPE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SliderLayout sliderLayout;
        PagerIndicator indicator;

        TextView textTitle;
        ImageView imageBig;
        ImageView imageTop;
        ImageView imageBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            //将slider布局和card布局分开获取控件实例,防止设置监听空指针异常
            if (itemView == sliderView) {
                sliderLayout = itemView.findViewById(R.id.home_slider_ad);
                indicator = itemView.findViewById(R.id.custom_indicator);
            } else {
                textTitle = itemView.findViewById(R.id.text_title);
                imageBig = itemView.findViewById(R.id.imgview_big);
                imageTop = itemView.findViewById(R.id.imgview_top);
                imageBottom = itemView.findViewById(R.id.imgview_bottom);

                imageBig.setOnClickListener(this);
                imageTop.setOnClickListener(this);
                imageBottom.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {

            HomeCampaign campaign = campaigns.get(getLayoutPosition() - 1);

            if (mListener != null) {
                switch (v.getId()) {
                    case R.id.imgview_big:
                        mListener.onClick(v, campaign.getCpOne());
                        break;
                    case R.id.imgview_top:
                        mListener.onClick(v, campaign.getCpTwo());
                        break;
                    case R.id.imgview_bottom:
                        mListener.onClick(v, campaign.getCpThree());
                        break;
                }
            }
        }
    }

    public interface OnCampaignClickListener {
        void onClick(View view, Campaign campaign);
    }

    public void setOnCampaignClickListener(OnCampaignClickListener listener){
        this.mListener = listener;
    }
}
