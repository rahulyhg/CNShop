package pers.sukai.cnshop.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import pers.sukai.cnshop.R;


/**
 * Created by sukaidev on 2018/12/5.
 */
public class CnToolbar extends Toolbar {

    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private CustomImageButton mRightImageButton;


    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CnToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);


            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }


            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);

            if (isShowSearchView) {

                showSearchView();
                hideTitleView();

            }
            a.recycle();
        }

    }

    private void initView() {


        if (mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.cntoolbar, null);


            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightImageButton = mView.findViewById(R.id.toolbar_rightButton);


            mSearchView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    mSearchView.setCursorVisible(true);
                    mSearchView.setHint("");
                }
            });

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }


    }


    public void setRightButtonIcon(Drawable icon) {

        if (mRightImageButton != null) {

            mRightImageButton.setImageDrawable(icon);
            mRightImageButton.setVisibility(VISIBLE);
            mRightImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }


    public void setRightButtonOnClickListener(OnClickListener li) {

        mRightImageButton.setOnClickListener(li);
    }


    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }


    }


    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)

            mTextTitle.setVisibility(GONE);

    }

    public View getRightButton() {
        return mRightImageButton;
    }

    public void setRightButtonText(String title) {

        if (mRightImageButton != null) {
            mRightImageButton.setText(title);
        }
    }


//
//    private void ensureRightButtonView() {
//        if (mRightImageButton == null) {
//            mRightImageButton = new ImageButton(getContext(), null,
//                    android.support.v7.appcompat.R.attr.toolbarNavigationButtonStyle);
//            final LayoutParams lp = generateDefaultLayoutParams();
//            lp.gravity = GravityCompat.START | (Gravity.VERTICAL_GRAVITY_MASK);
//            mRightImageButton.setLayoutParams(lp);
//        }
//    }


}
