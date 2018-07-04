package com.adealink.viewslib.viewflipper;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.adealink.viewslib.R;

/**
 * 制造走马灯效果；使得View产生复用；
 * Created by Xuefu_Du on 2018/7/3.
 */
public class MarqueeViewFlipper extends ViewFlipper {

    @AnimRes
    private int mInAnimResId = R.anim.bottom_in;
    @AnimRes
    private int mOutAnimResId = R.anim.top_out;

    private int mInterval = 2500;
    private int mAnimDuration = 500;
    private boolean mIsAnimStart;
    private int mPosition;
    private ViewFlipperAdapter mAdapter;

    public MarqueeViewFlipper(Context context) {
        this(context, null);
    }

    public MarqueeViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView, defStyleAttr, 0);
        mInterval = typedArray.getInteger(R.styleable.MarqueeView_marqueeInterval, mInterval);
        mAnimDuration = typedArray.getInteger(R.styleable.MarqueeView_marqueeAnimDuration, mAnimDuration);
        typedArray.recycle();

        setFlipInterval(mInterval);
        setInAndOutAnimation(mInAnimResId, mOutAnimResId);
        if (getInAnimation() != null) {
            getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (mIsAnimStart) {
                        animation.cancel();
                    }
                    mIsAnimStart = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPosition++;
                    if (mPosition >= mAdapter.getCount()) {
                        mPosition = 0;
                    }
                    addItemView(mPosition);
                    mIsAnimStart = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public void setAdapter(ViewFlipperAdapter adapter) {
        mAdapter = adapter;
    }

    public void start() {
        removeAllViews();
        clearAnimation();

        mPosition = 0;
        addItemView(mPosition);
    }

    private void addItemView(int position) {
        View view = mAdapter.getView(position, this);
        if (view.getParent() == null) {
            addView(view);
        }
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResId 离开动画的resID
     */
    private void setInAndOutAnimation(@AnimRes int inAnimResId, @AnimRes int outAnimResId) {
        Animation inAnim = AnimationUtils.loadAnimation(getContext(), inAnimResId);
        inAnim.setDuration(mAnimDuration);
        setInAnimation(inAnim);

        Animation outAnim = AnimationUtils.loadAnimation(getContext(), outAnimResId);
        outAnim.setDuration(mAnimDuration);
        setOutAnimation(outAnim);
    }


}
