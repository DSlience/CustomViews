package com.adealink.customviews.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.adealink.customviews.R;
import com.adealink.viewslib.viewflipper.ViewFlipperAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Xuefu_Du on 2018/7/4.
 */
public class DemoViewFlipperAdapter extends ViewFlipperAdapter<String> {

    private FragmentActivity mActivity;

    public DemoViewFlipperAdapter(FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public View updateView(int position, View view ) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_demo_view_flipper, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (holder != null) {
            holder.msgTv.setText(getItemDataBeam(position));
        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.msg_layout)
        LinearLayout msgLayout;
        @BindView(R.id.msg_tv)
        TextView msgTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
