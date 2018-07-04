package com.adealink.baselib.framework;

import android.support.v7.widget.RecyclerView;

import com.adealink.baselib.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的Adapter使用的基类；提供一些基础方法而避免重复实现；
 * Created by Xuefu_Du on 2017/10/27.
 */
public abstract class RecyclerBaseAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> mDataList = new ArrayList<>();
    private String mSourcePage;//埋点-源页面

    public String getSourcePageNotNull() {
        if (mSourcePage == null) {
            return "";
        }
        return mSourcePage;
    }

    public String getSourcePage() {
        return mSourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.mSourcePage = sourcePage;
    }

    public boolean isDataListEmpty() {
        return mDataList.size() <= 0;
    }

    /**
     * @param data 更新List
     */
    public void updateList(List<M> data) {
        mDataList.clear();
        if (!CommonUtil.isListBlank(data)) {
            mDataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * @param data 在集合尾部添加更多数据集合
     */
    public void addMoreList(List<M> data) {
        if (!CommonUtil.isListBlank(data)) {
            mDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addList(M data) {
        if (data != null) {
            mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * @return 获取数据集合
     */
    public List<M> getDataList() {
        return mDataList;
    }

    /**
     * @param position
     * @return 获取指定索引位置的数据模型
     */
    public M getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
