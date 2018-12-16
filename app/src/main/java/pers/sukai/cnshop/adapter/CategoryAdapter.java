package pers.sukai.cnshop.adapter;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

import pers.sukai.cnshop.R;
import pers.sukai.cnshop.bean.category.Category;

public class CategoryAdapter extends BaseAdapter<Category,BaseViewHolder> {


    public CategoryAdapter(Context context, List<Category> datas ) {
        super(context, datas,R.layout.template_single_textview);
    }

    @Override
    public void bindData(BaseViewHolder holder, Category category) {
        TextView textView = holder.getTextView(R.id.text_view);
        textView.setText(category.getName());
        textView.setHeight(135);
        textView.setGravity(Gravity.CENTER);
    }
}
