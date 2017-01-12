package com.cheikh.lazywaimai.widget.section;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.util.StringUtil;

public class SectionView extends LinearLayout {

    private String headerText;
    private String footerText;
    private LayoutInflater inflater;

    public SectionView(Context context) {
        this(context, null);
    }

    public SectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.SectionView);
        headerText = typeArr.getString(R.styleable.SectionView_headerText);
        footerText = typeArr.getString(R.styleable.SectionView_footerText);
        typeArr.recycle();

        inflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.section_space);
        setPadding(0, padding, 0, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (StringUtil.isNotEmpty(headerText)) {
            TextView headerTxt = (TextView) inflater.inflate(R.layout.view_section_list_footer, null);
            headerTxt.setText(headerText);
            addView(headerTxt);
        }
        if (StringUtil.isEmpty(footerText)) {
            TextView footerTxt = (TextView) inflater.inflate(R.layout.view_section_list_footer, null);
            footerTxt.setText(footerText);
            addView(footerTxt);
        }
    }
}