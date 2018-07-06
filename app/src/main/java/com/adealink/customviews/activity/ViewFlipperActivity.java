package com.adealink.customviews.activity;

import android.view.View;

import com.adealink.baselib.framework.BaseActivity;
import com.adealink.customviews.R;
import com.adealink.customviews.adapter.DemoViewFlipperAdapter;
import com.adealink.viewslib.viewflipper.MarqueeViewFlipper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Xuefu_Du on 2018/7/4.
 */
public class ViewFlipperActivity extends BaseActivity {

    @BindView(R.id.msg_marquee)
    MarqueeViewFlipper mMsgMarquee;

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_view_flipper);
    }

    public void inflateData(View view) {

        DemoViewFlipperAdapter adapter = new DemoViewFlipperAdapter(this);
        adapter.setDataList(getAdapterDataList());
        mMsgMarquee.setAdapter(adapter);
        mMsgMarquee.start();
    }

    private List<String> getAdapterDataList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("随便写点什么吧--" + i);
        }
        return list;
    }
}
