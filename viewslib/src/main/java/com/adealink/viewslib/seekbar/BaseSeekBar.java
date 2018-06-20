package com.adealink.viewslib.seekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.adealink.baselib.utils.PixelUtil;
import com.adealink.viewslib.R;

import java.math.BigDecimal;

/**
 * SeekBar选择器基类
 * Created by Xuefu_Du on 2018/6/20.
 */
@SuppressLint("AppCompatCustomView")
public abstract class BaseSeekBar<T extends Number> extends ImageView {

    protected final Integer BAR_CHANGE_WHEN_ACTION_MOVE = 1;
    public static final Integer BAR_CHANGE_WHEN_ACTION_UP = 2;

    protected final Integer DEFAULT_MINIMUM = 0;
    protected final Integer DEFAULT_MAXIMUM = 100;
    protected final int THUMB_TO_CALIBRATION_IN_DP = 32;//按钮顶端距离刻度线底端的距离
    protected final int CALIBRATION_TO_TEXT_IN_DP = 6;//刻度线顶端距离刻度文字底端的距离
    protected final int CALIBRATION_WIDTH_IN_DP = 1;//刻度线的宽度
    protected final int CALIBRATION_HEIGHT_IN_DP = 6;//刻度线的高度
    protected final int CALIBRATION_TEXT_IN_DP = 11;//刻度文字大小
    protected final int LINE_HEIGHT_IN_DP = 4;//进度条线的高度
    protected final float ZOOM_FACTOR = 1.6f;//放大倍数

    protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Bitmap mThumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_carc_screen_handle);
    private final float HALF_THUMB_WIDTH = 0.5f * mThumbBmp.getWidth();
    protected final float THUMB_HEIGHT = mThumbBmp.getHeight();
    private T absoluteMinValue, absoluteMaxValue;
    private NumberType numberType;
    private double absoluteMinValuePrim, absoluteMaxValuePrim;
    protected double normalizedMinValue = 0d;
    protected double normalizedMaxValue = 1d;
    protected Thumb pressedThumb = null;
    private OnRangeSeekBarChangeListener<T> listener;
    private float mGapPaddingLeft;//进度条距离控件左侧的间隔（目前左右间隔相同）
    /**
     * An invalid pointer id.
     */
    public static final int INVALID_POINTER_ID = 255;

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    public static final int ACTION_POINTER_UP = 0x6, ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;

    private float mDownMotionX;

    private int mActivePointerId = INVALID_POINTER_ID;

    private int mScaledTouchSlop;

    private boolean mIsDragging;

    private RectF mRect;

    protected Context mContext;

    public BaseSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public BaseSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }

        int type = tv.type;
        if (type == TypedValue.TYPE_FLOAT) {
            return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
        } else {
            return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs == null) {
            setRangeToDefaultValues();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);
            setRangeValues(
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM),
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM));
            a.recycle();
        }

        setValuePrimAndNumberType();

        // make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
        setFocusable(true);
        setFocusableInTouchMode(true);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setRangeValues(T minValue, T maxValue) {
        this.absoluteMinValue = minValue;
        this.absoluteMaxValue = maxValue;
        setValuePrimAndNumberType();
    }

    @SuppressWarnings("unchecked")
    // only used to set default values when initialised from XML without any values specified
    private void setRangeToDefaultValues() {
        this.absoluteMinValue = (T) DEFAULT_MINIMUM;
        this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
        setValuePrimAndNumberType();
    }

    private void setValuePrimAndNumberType() {
        absoluteMinValuePrim = absoluteMinValue.doubleValue();
        absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        numberType = NumberType.fromNumber(absoluteMinValue);
    }

    public void resetSelectedValues() {
        setSelectedMinValue(absoluteMinValue);
        setSelectedMaxValue(absoluteMaxValue);
    }

    /**
     * Returns the absolute minimum value of the range that has been set at construction time.
     *
     * @return The absolute minimum value of the range.
     */
    public T getAbsoluteMinValue() {
        return absoluteMinValue;
    }

    /**
     * Returns the absolute maximum value of the range that has been set at construction time.
     *
     * @return The absolute maximum value of the range.
     */
    public T getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    /**
     * Returns the currently selected min value.
     *
     * @return The currently selected min value.
     */
    public T getSelectedMinValue() {
        return normalizedToValue(normalizedMinValue);
    }

    /**
     * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
     */
    public void setSelectedMinValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMinValue(0d);
        } else {
            setNormalizedMinValue(valueToNormalized(value));
        }
    }

    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    public T getSelectedMaxValue() {
        return normalizedToValue(normalizedMaxValue);
    }

    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */
    public void setSelectedMaxValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMaxValue(1d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }

    /**
     * Registers given listener callback to notify about changed selected values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Handles thumb selection and movement. Notifies listener callback on certain events.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled()) {
            return false;
        }

        int pointerIndex;

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                // Remember where the motion event started
                mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                mDownMotionX = event.getX(pointerIndex);

                pressedThumb = evalPressedThumb(mDownMotionX);

                // Only handle thumb presses.
                if (pressedThumb == null) {
                    return super.onTouchEvent(event);
                }

                setPressed(true);
                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();

                break;
            case MotionEvent.ACTION_MOVE:

                if (pressedThumb != null) {
                    if (mIsDragging) {
                        trackTouchEvent(event);
                    } else {
                        // Scroll to follow the motion event
                        pointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(pointerIndex);

                        if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }
                    }
                }
                handleSeekBarValuesChanged(BAR_CHANGE_WHEN_ACTION_MOVE);
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }

                pressedThumb = null;
                invalidate();
                handleSeekBarValuesChanged(BAR_CHANGE_WHEN_ACTION_UP);
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = event.getPointerCount() - 1;
                mDownMotionX = event.getX(index);
                mActivePointerId = event.getPointerId(index);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    //进度条数值发生变化--做出相应的响应与处理
    private void handleSeekBarValuesChanged(int changeType) {
        if (listener != null) {
            listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue(), changeType);
        }
    }

    private final void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose
            // a new active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mDownMotionX = ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private final void trackTouchEvent(MotionEvent event) {
        final int pointerIndex = event.findPointerIndex(mActivePointerId);
        final float x = event.getX(pointerIndex);

        if (Thumb.MIN.equals(pressedThumb)) {
            setNormalizedMinValue(screenToNormalized(x));
        } else if (Thumb.MAX.equals(pressedThumb)) {
            setNormalizedMaxValue(screenToNormalized(x));
        }
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCustomView(canvas);

        // draw minimum thumb
        drawThumb(normalizedToScreen(normalizedMinValue), canvas);
        // draw maximum thumb
        drawThumb(normalizedToScreen(normalizedMaxValue), canvas);
    }

    //draw刻度、进度条等
    protected void drawCustomView(Canvas canvas) {
        //set mPaint
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        // draw calibration line
        mGapPaddingLeft = HALF_THUMB_WIDTH + getPaddingLeft();
        mPaint.setColor(getResources().getColor(R.color.color_g4plus));//刻度线颜色
        mPaint.setStrokeWidth(PixelUtil.dpToPx(mContext, CALIBRATION_WIDTH_IN_DP));
        float gap = (getWidth() - 2 * mGapPaddingLeft) / 55;
        float thumbToCalibration = PixelUtil.dpToPx(mContext, THUMB_TO_CALIBRATION_IN_DP);//按钮顶到刻度底
        float calibrationHeight = PixelUtil.dpToPx(mContext, CALIBRATION_HEIGHT_IN_DP);//刻度线高度
        for (int i = 0; i < 51; i++) {
            if (i != 0 && i % 5 == 0) {
                float startX = gap * i + mGapPaddingLeft;
                float startY = getHeight() - THUMB_HEIGHT - thumbToCalibration;
                canvas.drawLine(startX, startY, startX, startY - calibrationHeight, mPaint);
            }
        }
        // draw calibration text
        float textSize = PixelUtil.dpToPx(mContext, CALIBRATION_TEXT_IN_DP);//刻度文字大小
        float textGap = 5 * gap;
        float textZeroStartX = mGapPaddingLeft + 5 * gap - textSize / 2;
        float calibrationToText = PixelUtil.dpToPx(mContext, CALIBRATION_TO_TEXT_IN_DP);//刻度顶到文字底
        float textBaselineY = getHeight() - THUMB_HEIGHT - thumbToCalibration - calibrationToText - calibrationHeight;
        mPaint.setTextSize(textSize);
        mPaint.setColor(getResources().getColor(R.color.color_g1));//刻度文字颜色
        mPaint.setFakeBoldText(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < 10; i++) {
            int textValue = 5 * (i + 1);
            canvas.drawText("" + textValue, textGap * i + textZeroStartX + textSize / 2, textBaselineY, mPaint);
        }
        // draw seek bar background line
        float progressBarHeight = PixelUtil.dpToPx(mContext, LINE_HEIGHT_IN_DP);//进度条高度
        mPaint.setColor(getResources().getColor(R.color.color_g5));//进度条背景色
        float rectRight = getWidth() - mGapPaddingLeft;
        if (mRect == null) {
            float rectTop = getHeight() - THUMB_HEIGHT - thumbToCalibration / 2 - progressBarHeight / 2;//进度条top边
            mRect = new RectF(mGapPaddingLeft, rectTop, rectRight, rectTop + progressBarHeight);
        }
        mRect.left = mGapPaddingLeft;
        mRect.right = rectRight;
        int roundCorner = getResources().getDimensionPixelSize(R.dimen.dimen_50dp);//进度条圆角
        canvas.drawRoundRect(mRect, roundCorner, roundCorner, mPaint);

        // draw seek bar active range line
        mRect.left = normalizedToScreen(normalizedMinValue);
        mRect.right = normalizedToScreen(normalizedMaxValue);
        mPaint.setColor(getResources().getColor(R.color.color_c1));//进度条颜色
        canvas.drawRoundRect(mRect, roundCorner, roundCorner, mPaint);
    }

    /**
     * 四舍五入地处理srcNUmber
     *
     * @param srcNumber
     * @return
     */
    protected int turnFloatToInt(float srcNumber) {
        int ret = (int) Math.floor(srcNumber);
        if (srcNumber > ret + 0.5) {
            ret = (int) Math.ceil(srcNumber);
        }
        return ret;
    }

    /**
     * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", normalizedMinValue);
        bundle.putDouble("MAX", normalizedMaxValue);
        return bundle;
    }

    /**
     * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        normalizedMinValue = bundle.getDouble("MIN");
        normalizedMaxValue = bundle.getDouble("MAX");
    }

    /**
     * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
     *
     * @param screenCoord The x-coordinate in screen space where to draw the image.
     * @param canvas      The canvas to draw upon.
     */
    private void drawThumb(float screenCoord, Canvas canvas) {
        Bitmap buttonToDraw = mThumbBmp;
        canvas.drawBitmap(buttonToDraw, screenCoord - HALF_THUMB_WIDTH, getHeight() - THUMB_HEIGHT, mPaint);
    }

    /**
     * Decides which (if any) thumb is touched by the given x-coordinate.
     *
     * @param touchX The x-coordinate of a touch event in screen space.
     * @return The pressed thumb or null if none has been touched.
     */
    private Thumb evalPressedThumb(float touchX) {
        Thumb result = null;
        boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            // if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
            result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
        } else if (minThumbPressed) {
            result = Thumb.MIN;
        } else if (maxThumbPressed) {
            result = Thumb.MAX;
        }
        return result;
    }

    /**
     * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
     *
     * @param touchX               The x-coordinate in screen space to check.
     * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
     * @return true if x-coordinate is in thumb range, false otherwise.
     */
    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= HALF_THUMB_WIDTH;
    }

    //判断哪个游标被按下了 0-无  1-Min  2-Max
    public int whichThumbPressed() {
        int result = 0;
        if (pressedThumb == null) {
            return result;
        }
        if (Thumb.MAX.equals(pressedThumb)) {
            result = 2;
        } else {
            result = 1;
        }
        return result;
    }

    public void resetPressedThumb() {
        pressedThumb = null;
    }

    /**
     * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized min value to set.
     */
    private void setNormalizedMinValue(double value) {
        normalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, normalizedMaxValue)));
        invalidate();
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    private void setNormalizedMaxValue(double value) {
        normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, normalizedMinValue)));
        invalidate();
    }

    /**
     * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
     *
     * @param normalized
     * @return
     */
    @SuppressWarnings("unchecked")
    private T normalizedToValue(double normalized) {
        double v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim);
        return (T) numberType.toNumber(Math.round(v * 100) / 100d);
    }

    /**
     * Converts the given Number value to a normalized double.
     *
     * @param value The Number value to normalize.
     * @return The normalized double.
     */
    private double valueToNormalized(T value) {
        if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
            // prevent division by zero, simply return 0.
            return 0d;
        }
        return (value.doubleValue() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim);
    }

    /**
     * Converts a normalized value into screen space.
     *
     * @param normalizedCoord The normalized value to convert.
     * @return The converted value in screen space.
     */
    protected float normalizedToScreen(double normalizedCoord) {
        return (float) (mGapPaddingLeft + normalizedCoord * (getWidth() - 2 * mGapPaddingLeft));
    }

    /**
     * Converts screen space x-coordinates into normalized values.
     *
     * @param screenCoord The x-coordinate in screen space to convert.
     * @return The normalized value.
     */
    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= 2 * mGapPaddingLeft) {
            // prevent division by zero, simply return 0.
            return 0d;
        } else {
            double result = (screenCoord - mGapPaddingLeft) / (width - 2 * mGapPaddingLeft);
            return Math.min(1d, Math.max(0d, result));
        }
    }

    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    public interface OnRangeSeekBarChangeListener<T> {

        //changeType 1-ACTION_MOVE  2-ACTION_UP
        public void onRangeSeekBarValuesChanged(BaseSeekBar<?> bar, T minValue, T maxValue, int changeType);
    }

    /**
     * Thumb constants (min and max).
     */
    public enum Thumb {
        MIN, MAX
    }

    ;

    /**
     * Utility enumeration used to convert between Numbers and doubles.
     *
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    private enum NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
            throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (this) {
                case LONG:
                    return Long.valueOf((long) value);
                case DOUBLE:
                    return value;
                case INTEGER:
                    return Integer.valueOf((int) value);
                case FLOAT:
                    return Float.valueOf((float) value);
                case SHORT:
                    return Short.valueOf((short) value);
                case BYTE:
                    return Byte.valueOf((byte) value);
                case BIG_DECIMAL:
                    return BigDecimal.valueOf(value);
            }
            throw new InstantiationError("can't convert " + this + " to a Number object");
        }
    }

}
