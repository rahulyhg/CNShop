package pers.sukai.cnshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.bean.cart.ShoppingCart;
import pers.sukai.cnshop.bean.hot.Ware;
import pers.sukai.cnshop.utils.CartProvider;

public class HotPageAdapter extends BaseAdapter<Ware,BaseViewHolder> {

    CartProvider cartProvider;
    Context mContext;

    public HotPageAdapter(Context context, List<Ware> wares, int layoutResId) {
        super(context, wares, layoutResId);
        this.mContext = context;
        cartProvider = new CartProvider(context);
    }

    @Override
    public void bindData(BaseViewHolder holder, final Ware ware) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.ware_drawee_view);
        draweeView.setImageURI(Uri.parse(ware.getImgUrl()));
        holder.getTextView(R.id.ware_title).setText(ware.getTitle());
        holder.getTextView(R.id.ware_price).setText("¥"+ ware.getPrice());
        holder.getTextView(R.id.ware_sale).setText("已售:" + ware.getSale());

        Button button = holder.getButton(R.id.btn_purchase);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartProvider.put(convertData(ware));

                Toast.makeText(mContext,"已添加到购物车",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private ShoppingCart convertData(Ware item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setTitle(item.getTitle());
        cart.setImgUrl(item.getImgUrl());
//        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
