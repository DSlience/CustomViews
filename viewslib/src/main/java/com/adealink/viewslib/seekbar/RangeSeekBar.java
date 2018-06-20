package com.adealink.viewslib.seekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.adealink.baselib.utils.PixelUtil;
import com.adealink.viewslib.R;

/**
 * 选择器控件；
 * Created by Xuefu_Du on 2016/6/13.
 */
@SuppressLint("AppCompatCustomView")
public class RangeSeekBar<T extends Number> extends BaseSeekBar<T> {

    private final int OVERLAY_CIRCLE_RADIUS_IN_DP = 15;//显示选中刻度的圆形的半径
    private final int OVERLAY_TEXT_IN_DP = 14;//选中刻度文字的大小

    public RangeSeekBar(Context context) {
        super(context);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * Ensures correct size of the widget.
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 200;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        //按钮顶端距离刻度文字顶端的距离
        int calibrationH = THUMB_TO_CALIBRATION_IN_DP + CALIBRATION_HEIGHT_IN_DP + CALIBRATION_TO_TEXT_IN_DP + CALIBRATION_TEXT_IN_DP;
        //按钮顶端距离圆形Overlay顶端的距离
        int overlayH = THUMB_TO_CALIBRATION_IN_DP + 2 * OVERLAY_CIRCLE_RADIUS_IN_DP;
        //+1其实就是上取整
        int height = 1 + (int) Math.max(THUMB_HEIGHT + PixelUtil.dpToPx(getContext(), calibrationH), THUMB_HEIGHT + PixelUtil.dpToPx(getContext(), overlayH));
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //选中的刻度的显示
        drawOverlayPart(canvas);
    }

    //绘制显示刻度的圆形区域
    private void drawOverlayPart(Canvas canvas) {
        if (pressedThumb == null) {
            return;
        }

        int selectedValue = turnFloatToInt(getSelectedMinValue().floatValue());
        double magnifierPosX = normalizedMinValue;
        if (Thumb.MAX.equals(pressedThumb)) {
            selectedValue = turnFloatToInt(getSelectedMaxValue().floatValue());
            magnifierPosX = normalizedMaxValue;
        }

        //绘制圆形
        paint.setColor(getResources().getColor(R.color.color_c1));
        float radius = PixelUtil.dpToPx(mContext, OVERLAY_CIRCLE_RADIUS_IN_DP);
        float thumbToCalibration = PixelUtil.dpToPx(mContext, THUMB_TO_CALIBRATION_IN_DP);//按钮顶到刻度底
        float circleMidY = getHeight() - THUMB_HEIGHT - thumbToCalibration - radius;
        canvas.drawCircle(normalizedToScreen(magnifierPosX), circleMidY, radius, paint);

        //绘制显示文字
        paint.setColor(getResources().getColor(R.color.color_g7));
        paint.setFakeBoldText(true);
        float textSize = PixelUtil.dpToPx(mContext, OVERLAY_TEXT_IN_DP);//刻度文字大小
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = circleMidY + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText("" + selectedValue, normalizedToScreen(magnifierPosX), baseline, paint);
    }

}
