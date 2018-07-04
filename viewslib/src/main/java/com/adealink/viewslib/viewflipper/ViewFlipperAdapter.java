package com.adealink.viewslib.viewflipper;

import android.view.View;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewFlipper数据Adapter基类
 * Created by Xuefu_Du on 2018/7/3.
 */
public abstract class ViewFlipperAdapter<T> {

    private List<T> mDataList = new ArrayList<>();

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> list) {
        this.mDataList = list;
    }

    public T getItemDataBeam(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    public int getCount() {
        return getDataList().size();
    }

    public View getView(int position, ViewFlipper viewFlipper) {
//        View view = viewFlipper.getChildAt(viewFlipper.getDisplayedChild());
        View view = viewFlipper.getChildAt((viewFlipper.getDisplayedChild() + 1) % 2);
        return updateView(position, view);
    }

    public abstract View updateView(int position, View view);
}
