package pers.sukai.cnshop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.ref.WeakReference;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.adapter.BottomAdapter;
import pers.sukai.cnshop.fragment.CartFragment;
import pers.sukai.cnshop.fragment.CategoryFragment;
import pers.sukai.cnshop.fragment.HomeFragment;
import pers.sukai.cnshop.fragment.HotFragment;
import pers.sukai.cnshop.fragment.UserFragment;
import pers.sukai.cnshop.widget.BottomNavigationViewHelper;
import pers.sukai.cnshop.widget.CnToolbar;


/**
 * Created by sukaidev on 2018/12/4.
 * MainActivity 主活动
 */

public class MainActivity extends AppCompatActivity {

    // ToolBar状态
    public static final int TOOLBAR_SEARCH = 0;
    public static final int TOOLBAR_CART = 1;
    public int Toolbar_status = TOOLBAR_SEARCH;

    @ViewInject(R.id.BottomNavigationView)
    private BottomNavigationView mBv;
    @ViewInject(R.id.ViewPager)
    private ViewPager mVp;
    private MenuItem menuItem;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        BottomNavigationViewHelper.disableShiftMode(mBv);
//        mBv.setItemIconTintList(null);
        //这里可true是一个消费过程，同样可以使用break，外部返回true也可以
        mBv.getMenu().getItem(0).setIcon(R.mipmap.icon_home_press);
        mBv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetToDefaultIcon();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        item.setIcon(R.mipmap.icon_home_press);
                        mVp.setCurrentItem(0);
                        changeToolbar(TOOLBAR_SEARCH);
                        return true;
                    case R.id.navigation_hot:
                        mVp.setCurrentItem(1);
                        item.setIcon(R.mipmap.icon_hot_press);
                        changeToolbar(TOOLBAR_SEARCH);
                        return true;
                    case R.id.navigation_discover:
                        item.setIcon(R.mipmap.icon_discover_press);
                        mVp.setCurrentItem(2);
                        changeToolbar(TOOLBAR_SEARCH);
                        return true;
                    case R.id.navigation_cart:
                        item.setIcon(R.mipmap.icon_cartfill_press);
                        mVp.setCurrentItem(3);
                        changeToolbar(TOOLBAR_CART);
                        return true;
                    case R.id.navigation_user:
                        item.setIcon(R.mipmap.icon_user_press);
                        mVp.setCurrentItem(4);
                        changeToolbar(TOOLBAR_SEARCH);
                        return true;
                }
                return false;
            }
        });


        //数据填充
        setupViewPager(mVp);
        //ViewPager监听
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBv.getMenu().getItem(0).setChecked(false);
                }
                menuItem = mBv.getMenu().getItem(position);
                resetToDefaultIcon();
                switch (position) {
                    case 0:
                        menuItem.setIcon(R.mipmap.icon_home_press);
                        menuItem.setChecked(true);
                        changeToolbar(TOOLBAR_SEARCH);
                        break;
                    case 1:
                        menuItem.setIcon(R.mipmap.icon_hot_press);
                        menuItem.setChecked(true);
                        changeToolbar(TOOLBAR_SEARCH);
                        break;
                    case 2:
                        menuItem.setIcon(R.mipmap.icon_discover_press);
                        menuItem.setChecked(true);
                        changeToolbar(TOOLBAR_SEARCH);
                        break;
                    case 3:
                        menuItem.setIcon(R.mipmap.icon_cartfill_press);
                        menuItem.setChecked(true);
                        changeToolbar(TOOLBAR_CART);
                        break;
                    case 4:
                        menuItem.setIcon(R.mipmap.icon_user_press);
                        menuItem.setChecked(true);
                        changeToolbar(TOOLBAR_SEARCH);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //禁止ViewPager滑动
        //        mVp.setOnTouchListener(new View.OnTouchListener() {
        //                    @Override
        //                    public boolean onTouch(View v, MotionEvent event) {
        //                        return true;
        //                    }
        //                });
    }

    private void resetToDefaultIcon() {
        MenuItem home = mBv.getMenu().findItem(R.id.navigation_home);
        home.setIcon(R.mipmap.icon_home);
        MenuItem hot = mBv.getMenu().findItem(R.id.navigation_hot);
        hot.setIcon(R.mipmap.icon_hot);
        MenuItem discover = mBv.getMenu().findItem(R.id.navigation_discover);
        discover.setIcon(R.mipmap.icon_discover);
        MenuItem cart = mBv.getMenu().findItem(R.id.navigation_cart);
        cart.setIcon(R.mipmap.icon_cart);
        MenuItem user = mBv.getMenu().findItem(R.id.navigation_user);
        user.setIcon(R.mipmap.icon_user);
    }

    private void setupViewPager(ViewPager viewPager) {
        BottomAdapter adapter = new BottomAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new HotFragment());
        adapter.addFragment(new CategoryFragment());
        adapter.addFragment(new CartFragment());
        adapter.addFragment(new UserFragment());
        viewPager.setAdapter(adapter);
    }

    // 改变Toolbar状态
    public void changeToolbar(int status) {

        if (status == TOOLBAR_SEARCH || status == TOOLBAR_CART) {
            if (status == TOOLBAR_SEARCH && Toolbar_status == TOOLBAR_CART) {

                mToolbar.showSearchView();
                mToolbar.hideTitleView();
                mToolbar.getRightButton().setVisibility(View.GONE);
                Toolbar_status = status;

            } else if (status == TOOLBAR_CART && Toolbar_status == TOOLBAR_SEARCH) {
                CartFragment cartFragment = new CartFragment();
                cartFragment.changeToolbar(mToolbar);
                Toolbar_status = TOOLBAR_CART;

            }
        }
    }
}