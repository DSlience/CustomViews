package com.adealink.baselib.framework;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的(ListView)基类Adapter，实现基本方法中的通用操作，避免在具体的Adapter中重复实现基本方法
 * Created by Xuefu_Du on 2018/6/24.
 */
public abstract class BaseLVAdapter<T> extends BaseAdapter {

    public List<T> mDatalist = new ArrayList<>();

    public List<T> getDataList() {
        return mDatalist;
    }

    public void updateList(List<T> list) {
        mDatalist.clear();
        if (list != null) {
            mDatalist.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        if (list != null) {
            mDatalist.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clearList() {
        mDatalist.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatalist.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatalist != null && mDatalist.size() > position) {
            return mDatalist.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}