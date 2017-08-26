package com.xiaoluogo.goodtochat.pager;

import android.view.View;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.base.BaseFragment;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.waveview.WaveView;

/**
 * 我的相关设置页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MySettingFragment extends BaseFragment {

    private WaveView wave_view;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.setting_fragment,null);
        L.e("我的相关设置页面====初始化视图完成");
        wave_view = (WaveView) view.findViewById(R.id.wave_view);
        wave_view.setAnimation();
        UserBean userBean = UserBean.getCurrentUser(UserBean.class);
        wave_view.setWaveBitmap(userBean.getHeaderImage());
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        L.e("我的相关设置页面====初始化数据完成");
    }
}
