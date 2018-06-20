package com.adealink.baselib.framework;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * MVP框架基类Activity
 * Created by Xuefu_Du on 2018/5/9.
 */
public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    private Unbinder mUnbinder;

    /**
     * 初始化布局及数据
     */
    protected abstract void setUpViewAndData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        setUpViewAndData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    //--------依赖注入--------Start
    protected void injectDependencies() {
    }
    //--------依赖注入--------End

}
