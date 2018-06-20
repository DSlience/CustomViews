package com.adealink.viewslib.seekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.adealink.baselib.utils.PixelUtil;
import com.adealink.viewslib.R;

/**
 * 放大镜效果的选择器控件
 * Created by Xuefu_Du on 2018/6/20.
 */
@SuppressLint("AppCompatCustomView")
public class MagnifySeekBar<T extends Number> extends BaseSeekBar<T> {

    private final Bitmap mMagnifierBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.magnifier_bg);

    public MagnifySeekBar(Context context) {
        super(context);
    }

    public MagnifySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagnifySeekBar(Context context, AttributeSet attrs, int defStyle) {
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

        int height = (int) Math.max(2 * THUMB_HEIGHT + PixelUtil.dpToPx(getContext(), CALIBRATION_HEIGHT_IN_DP), THUMB_HEIGHT + mMagnifierBmp.getHeight());
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
        //放大镜效果
        drawMagnifierPart(canvas);
    }

    //被放大的图形
    private void drawMagnifierPart(Canvas canvas) {
        if (pressedThumb == null) {
            return;
        }

        double magnifierPosX = normalizedMinValue;
        if (Thumb.MAX.equals(pressedThumb)) {
            magnifierPosX = normalizedMaxValue;
        }

        //放大镜圆
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        float radius = mMagnifierBmp.getWidth() / 2 * 138 / 152;//PixelUtil.dpToPx(mContext, RADIUS_IN_DP);//放大镜圆的半径
        Path path = new Path();
        float circleMidY = getHeight() - THUMB_HEIGHT - mMagnifierBmp.getHeight() / 2;
        canvas.drawBitmap(mMagnifierBmp, normalizedToScreen(magnifierPosX) - mMagnifierBmp.getWidth() / 2, circleMidY - mMagnifierBmp.getHeight() / 2, mPaint);
        path.addCircle(normalizedToScreen(magnifierPosX), circleMidY, radius, Path.Direction.CW);
        canvas.clipPath(path);
        //矩阵变换画布
        float longLine = PixelUtil.dpToPx(mContext, THUMB_TO_CALIBRATION_IN_DP);//长刻度长度
        float magnifierMidX = (normalizedToScreen(magnifierPosX)) * ZOOM_FACTOR;
        float magnifierMidY = (getHeight() - THUMB_HEIGHT - longLine) * ZOOM_FACTOR;
        canvas.translate(-(magnifierMidX - normalizedToScreen(magnifierPosX)), -(magnifierMidY - circleMidY));
        canvas.scale(ZOOM_FACTOR, ZOOM_FACTOR);

        drawCustomView(canvas);
    }

}
