package pers.sukai.cnshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.activity.MainActivity;
import pers.sukai.cnshop.adapter.CartAdapter;
import pers.sukai.cnshop.bean.cart.ShoppingCart;
import pers.sukai.cnshop.utils.CartProvider;
import pers.sukai.cnshop.widget.CnToolbar;

/**
 * Created by sukaidev on 2018/12/4.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CnToolbar mToolbar;


    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, null);

        x.view().inject(this, view);

        cartProvider = new CartProvider(getContext());
        mBtnOrder.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
        showData();
        return view;
    }

    private void showData() {


        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    public void refData() {

        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof MainActivity) {

            MainActivity activity = (MainActivity) context;

            mToolbar = activity.findViewById(R.id.toolbar);

            if (activity.Toolbar_status == MainActivity.TOOLBAR_SEARCH) {
                changeToolbar();
                activity.Toolbar_status = MainActivity.TOOLBAR_CART;
            }
        }
    }


    private void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.menu_bottom_cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    public void changeToolbar(CnToolbar toolbar) {

        toolbar.hideSearchView();
        toolbar.showTitleView();
        toolbar.setTitle(R.string.menu_bottom_cart);
        toolbar.getRightButton().setVisibility(View.VISIBLE);
        toolbar.setRightButtonText("编辑");
        toolbar.getRightButton().setTag(ACTION_EDIT);

    }

    private void showDelControl() {
        mToolbar.setRightButtonText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    private void hideDelControl() {

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toolbar_rightButton:
                int action = (int) v.getTag();
                if (ACTION_EDIT == action) {

                    showDelControl();
                } else if (ACTION_CAMPLATE == action) {

                    hideDelControl();
                }
                break;
            case R.id.btn_order:
                Toast.makeText(getContext(),"结算",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_del:

                Toast.makeText(getContext(),"删除",Toast.LENGTH_LONG).show();
                mAdapter.delCart();
                break;
            default:
                break;
        }

    }

}