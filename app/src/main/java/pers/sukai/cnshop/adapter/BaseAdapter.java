package pers.sukai.cnshop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * create by sukaidev on 2018/12/6.
 * RecyclerViewAdapter的简单封装.
 */
public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    // 数据源
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected  Context mContext;
    //  创建ViewHolder所用的布局ID
    protected  int mLayoutResId;

    public interface OnItemClickListener{
        void onClick(View view,int position);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BaseAdapter(Context context , List<T> datas, int layoutResId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutResId = layoutResId;

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(mLayoutResId,null,false);

        return new BaseViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        T t = getItem(position);

        // 将绑定逻辑交给用户处理
        bindData(holder,t);
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    public T getItem(int position) {
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    public void clearData() {
        mDatas.clear();
        notifyItemRangeChanged(0, mDatas.size());
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public List<T> getDatas(){

        return  mDatas;
    }

    public void addData(int position, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }

    public List<T> getData() {
        return mDatas;
    }

    // 对ViewHolder内的控件进行操作交给用户
    public abstract void bindData(BaseViewHolder holder,T t);
}
