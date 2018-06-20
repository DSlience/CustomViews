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
import com.adealink.viewslib.seekbar.BaseSeekBar;

import butterknife.BindView;

/**
 * Created by Xuefu_Du on 2018/6/20.
 */
public class MagnifySeekBarActivity extends BaseMvpActivity<MagnifySeekBarView, MagnifySeekBarPresenter>
        implements MagnifySeekBarView {

    @BindView(R.id.magnify_seekbar)
    BaseSeekBar mMagnifySeekBar;//价格SeekBar
    @BindView(R.id.range_seekbar)
    BaseSeekBar mRangeeSeekBar;
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

        initMagnifySeekBar();
        initRangeSeekBar();
    }

    private void initMagnifySeekBar() {
        float minPrice = 0f;
        float maxPrice = 55f;
        mMagnifySeekBar.setSelectedMinValue(minPrice);
        mMagnifySeekBar.setSelectedMaxValue(maxPrice);

        PriceSelectorChangeListener listener = new PriceSelectorChangeListener(this, mPriceTagTv);
        //初始化价格选择器
        mMagnifySeekBar.resetPressedThumb();
        mMagnifySeekBar.setOnRangeSeekBarChangeListener(listener);
        listener.showPriceTag(minPrice, maxPrice);
    }

    private void initRangeSeekBar() {
        float minPrice = 0f;
        float maxPrice = 55f;
        mRangeeSeekBar.setSelectedMinValue(minPrice);
        mRangeeSeekBar.setSelectedMaxValue(maxPrice);

        PriceSelectorChangeListener listener = new PriceSelectorChangeListener(this, mPriceTagTv);
        //初始化价格选择器
        mRangeeSeekBar.resetPressedThumb();
        mRangeeSeekBar.setOnRangeSeekBarChangeListener(listener);
        listener.showPriceTag(minPrice, maxPrice);
    }

}
