package com.adealink.customviews.dagger.component;

import com.adealink.customviews.activity.MagnifySeekBarActivity;
import com.adealink.customviews.dagger.module.MagnifySeekBarModule;

import dagger.Component;

/**
 * Created by Xuefu_Du on 2018/5/23.
 */
@Component(modules = MagnifySeekBarModule.class)
public interface MagnifySeekBarComponent {

    void inject(MagnifySeekBarActivity activity);
}
