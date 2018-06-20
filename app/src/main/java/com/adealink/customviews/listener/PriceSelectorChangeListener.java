package com.adealink.customviews.listener;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.adealink.baselib.utils.LogUtil;
import com.adealink.customviews.R;
import com.adealink.viewslib.seekbar.BaseSeekBar;

/**
 * 条件选车-价格区间选择的监听器
 * Created by Xuefu_Du on 2016/12/21.
 */
public class PriceSelectorChangeListener implements BaseSeekBar.OnRangeSeekBarChangeListener<Float> {

    private TextView mPriceTagTv;//显示价格的TextView
    private Context mContext;

    public PriceSelectorChangeListener(Context context, TextView priceTagTv) {
        mContext = context;
        this.mPriceTagTv = priceTagTv;
    }

    /**
     * 显示价格区间的文字形式--供外部调用
     */
    public void showPriceTag(float minValue, float maxValue) {
        showPriceTag(turnFloatToInt(minValue), turnFloatToInt(maxValue));
    }

    @Override
    public void onRangeSeekBarValuesChanged(BaseSeekBar<?> seekBar, Float minValue, Float maxValue, int changeType) {
        //处理显示UI--调整最大最小值的间距
        handlePriceInterval((BaseSeekBar<Float>) seekBar, minValue, maxValue);

        //接下来，从seekBar中获取值，以保证值为最新
        float minPrice = (Float) seekBar.getSelectedMinValue();
        int min = turnFloatToInt(minPrice);
        float maxPrice = (Float) seekBar.getSelectedMaxValue();
        int max = turnFloatToInt(maxPrice);
        //价格选择完成--to筛选
        if (changeType == BaseSeekBar.BAR_CHANGE_WHEN_ACTION_UP) {
            postEventBus(min, max);
        }
        showPriceTag(min, max);
    }

    /**
     * 四舍五入地处理srcNUmber
     *
     * @param srcNumber
     * @return
     */
    private int turnFloatToInt(float srcNumber) {
        int ret = (int) Math.floor(srcNumber);
        if (srcNumber > ret + 0.5) {
            ret = (int) Math.ceil(srcNumber);
        }
        return ret;
    }

    /**
     * 显示价格区间的文字形式
     *
     * @param minPrice
     * @param maxPrice
     */
    private void showPriceTag(int minPrice, int maxPrice) {
        if (minPrice >= 50) {
            String priceStr = mContext.getResources().getString(R.string.price_html_four, "50");
            mPriceTagTv.setText(Html.fromHtml(priceStr));
            return;
        }
        if (maxPrice > 50 && minPrice <= 0) {
            mPriceTagTv.setText(R.string.total);
            return;
        } else {
            if (maxPrice > 50) {
                String priceStr = mContext.getResources().getString(R.string.price_html_four, String.valueOf(minPrice));
                mPriceTagTv.setText(Html.fromHtml(priceStr));
                return;
            }
        }
        String priceStr = mContext.getResources().getString(R.string.price_html_five, (minPrice + " - " + maxPrice));
        mPriceTagTv.setText(Html.fromHtml(priceStr));
    }

    //发送EventBus--选中了选车条件
    private void postEventBus(int minPrice, int maxPrice) {
        LogUtil.d("d_slience", "minPrice:" + minPrice + "--maxPrice:" + maxPrice);
    }

    //处理价格区间过小的情况
    private void handlePriceInterval(BaseSeekBar<Float> seekBar, Float minValue, Float maxValue) {
        float minPrice = minValue;
        float maxPrice = maxValue;
        if (Math.abs(maxPrice - minPrice) < 5) {
            if (seekBar.whichThumbPressed() == 1 && minPrice >= 0) {//min被按下
                seekBar.setSelectedMaxValue(minPrice + 5);
            } else if (seekBar.whichThumbPressed() == 2 && maxPrice <= 50) {//max被按下
                seekBar.setSelectedMinValue(maxPrice - 5);
            } else if (minPrice >= 5) {
                seekBar.setSelectedMaxValue(minPrice + 5);
            }
        }
        if (maxPrice < 5) {
            seekBar.setSelectedMaxValue(5f);
        }
        if (minPrice > 50) {
            seekBar.setSelectedMinValue(50f);
        }
    }
}
