package pers.sukai.cnshop.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.bean.hot.Ware;

public class WaresAdapter extends BaseAdapter<Ware,BaseViewHolder> {

    public WaresAdapter(Context context, List<Ware> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, Ware ware) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(ware.getImgUrl());
        holder.getTextView(R.id.ware_title).setText(ware.getTitle());
        holder.getTextView(R.id.ware_price).setText(""+ware.getPrice());

    }
}
