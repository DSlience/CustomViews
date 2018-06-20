package com.adealink.customviews.activity;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.adealink.baselib.framework.BaseMvpActivity;
import com.adealink.customviews.R;
import com.adealink.customviews.dagger.component.DaggerMagnifySeekBarComponent;
import com.adealink.customviews.dagger.module.MagnifySeekBarModule;
import com.adealink.customviews.listener.PriceSelectorChangeListener;
import com.adealink.customviews.presenter.MagnifySeekBarPresenter;
import com.adealink.customviews.view.MagnifySeekBarView;
import com.adealink.viewslib.seekbar.RangeSeekBar;

import butterknife.BindView;

/**
 * Created by Xuefu_Du on 2018/6/20.
 */
public class MagnifySeekBarActivity extends BaseMvpActivity<MagnifySeekBarView, MagnifySeekBarPresenter>
        implements MagnifySeekBarView {

    @BindView(R.id.price_seekbar)
    RangeSeekBar mPriceSeekBar;//价格SeekBar
    @BindView(R.id.price_tag_tv)
    TextView mPriceTagTv;//价格指示文字

    @NonNull
    @Override
    public MagnifySeekBarPresenter createPresenter() {
        return new MagnifySeekBarPresenter();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        DaggerMagnifySeekBarComponent.builder()
                .magnifySeekBarModule(new MagnifySeekBarModule())
                .build()
                .inject(this);
    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_magnify_seek_bar);

        float minPrice = 0f;
        float maxPrice = 55f;
        mPriceSeekBar.setSelectedMinValue(minPrice);
        mPriceSeekBar.setSelectedMaxValue(maxPrice);

        PriceSelectorChangeListener mPriceSelectorChangeListener = new PriceSelectorChangeListener(this, mPriceTagTv);
        //初始化价格选择器
        mPriceSeekBar.resetPressedThumb();
        mPriceSeekBar.setOnRangeSeekBarChangeListener(mPriceSelectorChangeListener);
        mPriceSelectorChangeListener.showPriceTag(minPrice, maxPrice);
    }


}
