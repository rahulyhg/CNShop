package pers.sukai.cnshop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import pers.sukai.cnshop.R;

/**
 * Created by sukaidev on 2018/12/4.
 *
 */
public class HomeFragment extends Fragment {

    private View view;

    // 广告栏
    private SliderLayout sliderLayout;
    private PagerIndicator indicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view =  inflater.inflate(R.layout.fragment_home,null);
         initSlider();
         return view;
    }

    private void initSlider() {

        sliderLayout = view.findViewById(R.id.home_slider);
        indicator = view.findViewById(R.id.custom_indicator);

        DefaultSliderView sliderView = new DefaultSliderView(getActivity());

    }


    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }
}
