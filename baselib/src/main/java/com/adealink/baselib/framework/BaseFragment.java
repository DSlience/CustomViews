package com.adealink.baselib.framework;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Xuefu_Du on 2018/6/21.
 */
public abstract class BaseFragment extends BaseMvpFragment<MvpView, MvpPresenter<MvpView>> {

    @Override
    public MvpPresenter<MvpView> createPresenter() {
        return new MvpPresenter<MvpView>() {
            @Override
            public void attachView(MvpView view) {

            }

            @Override
            public void detachView(boolean retainInstance) {

            }
        };
    }

}
