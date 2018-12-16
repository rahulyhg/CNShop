package pers.sukai.cnshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.bean.hot.Ware;

public class HotPageAdapter extends BaseAdapter<Ware,BaseViewHolder> {

//    CartProvider cartProvider;
    Context mContext;

    public HotPageAdapter(Context context, List<Ware> wares, int layoutResId) {
        super(context, wares, layoutResId);
        this.mContext = context;
    }

    @Override
    public void bindData(BaseViewHolder holder, Ware ware) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.ware_drawee_view);
        draweeView.setImageURI(Uri.parse(ware.getImgUrl()));
        holder.getTextView(R.id.ware_title).setText(ware.getTitle());
        holder.getTextView(R.id.ware_price).setText("¥"+ ware.getPrice());
        holder.getTextView(R.id.ware_sale).setText("已售:" + ware.getSale());

        holder.getButton(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                cartProvider.put(convertData(ware));
//
//                Toast.makeText(mContext,"已添加到购物车",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
