package pers.sukai.cnshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * create by sukaidev on 2018/12/6.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> views;

    private BaseAdapter.OnItemClicklistener onItemClicklistener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClicklistener listener) {
        super(itemView);
        views = new SparseArray<>();

        this.onItemClicklistener = listener;
        itemView.setOnClickListener(this);
    }

    public View getView(int id) {
        return findView(id);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public ImageView getImageView(int id){
        return findView(id);
    }

    private  <T extends View> T  findView(int id) {

        View view = views.get(id);

        if (view == null) {
            view = itemView.findViewById(id);

            views.put(id, view);
        }

        return (T) view;
    }

    @Override
    public void onClick(View v) {

        if (onItemClicklistener!=null){
            onItemClicklistener.onClick(v,getLayoutPosition());
        }

    }
}
