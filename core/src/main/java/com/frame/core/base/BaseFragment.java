package com.frame.core.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.core.interf.IBaseView;
import com.frame.core.util.TLog;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Fragment基类
 * Fragment的生命周期如下（附加对应Activity的生命周期状态）
 * Fragment ---------------- Activity
 * ----------------------------------
 * onInflate()              | before Create
 * ----------------------------------
 * onAttach()               |
 * onCreate()               |
 * onCreateView()           | Created
 * onViewCreated()          |
 * onActivityCreated()      |
 * ----------------------------------
 * onStart()                | Started
 * ----------------------------------
 * onResume()               | Resumed
 * ----------------------------------
 * onPause()                | Paused
 * ----------------------------------
 * onStop()                 | Stopped
 * ----------------------------------
 * onDestroyView()          |
 * onDestroy()              | Destroyed
 * onDetach()               |
 * ----------------------------------
 * Created by Administrator on 2015/12/2.
 */
public abstract class BaseFragment extends Fragment implements IBaseView {

    public static final String TAG = BaseFragment.class.getSimpleName();

    protected boolean isActivityCreated = false; // 页面控件是否已初始化
    public boolean isFirstVisible = true; // 是否第一次可见

    protected View mFragmentView;

    protected int position;

    protected BaseAppCompatActivity mContext;

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        TLog.i(TAG,"setUserVisibleHint====>"+isVisibleToUser+"====>"+position);
        if (isActivityCreated) {
            if (isVisibleToUser) {
                if (isFirstVisible) {
                    isFirstVisible = false;
                    onPageFirstVisible();
                }
                onPageStart();
            } else {
                onPageEnd();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        TLog.i(TAG,"onCreate====>"+position);
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        mContext = (BaseAppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TLog.i(TAG,"onCreateView====>"+position+"====>"+isActivityCreated+"====>"+isFirstVisible);
        return inflater.inflate(initPageLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TLog.i(TAG,"onViewCreated====>"+position);
        mFragmentView = view;
        ButterKnife.bind(this, mFragmentView);
        initPageView();
        initPageViewListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        TLog.i(TAG,"onActivityCreated====>"+position);
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onPageFirstVisible();
            }
        }
        process(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        TLog.i(TAG,"onDestroyView====>"+position);
        super.onDestroyView();
        ButterKnife.unbind(this);
        mFragmentView = null;
    }

    @Override
    public void onDetach() {
        TLog.i(TAG,"onDetach====>"+position);
        super.onDetach();
        //noinspection TryWithIdenticalCatches
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @LayoutRes
    protected abstract int initPageLayoutId();

    @Override
    public boolean getUserVisibleHint() {
        TLog.i(TAG,"getUserVisibleHint====>"+super.getUserVisibleHint()+"====>"+position);
        return super.getUserVisibleHint();
    }

    /**
     * 当页面首次可见时调用。调用时页面控件已经完成初始化
     * 用于ViewPager下的页面懒加载，在一个生命周期内只会调用一次
     */
    protected void onPageFirstVisible() {
        TLog.i(TAG,"onPageFirstVisible====>"+position);
    }

    /**
     * 逻辑处理
     */
    protected void process(Bundle savedInstanceState) {
        TLog.i(TAG,"process====>"+position);
    }

    protected void onPageStart() {
        TLog.i(TAG,"onPageStart====>"+super.getUserVisibleHint()+"====>"+position);
        lazyLoad();
    }

    protected void onPageEnd() {
        TLog.i(TAG,"onPageEnd====>"+super.getUserVisibleHint()+"====>"+position);
    }

    /**
     * 懒加载数据
     */
    protected void lazyLoad() {

    }

    /**
     * 跳转到新的页面
     *
     * @param cls
     */
    protected void launchActivity(Class<? extends Activity> cls) {
        launchActivity(cls, null);
    }

    protected void launchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(this.getActivity(), cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void launchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle, Integer flag) {
        Intent intent = new Intent(this.getActivity(), cls);
        if (flag != null) {
            intent.setFlags(flag);
        }
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void initData() {

    }

    public BaseAppCompatActivity getBaseActivity() {
        return mContext != null ? mContext : (BaseAppCompatActivity) this.getContext();
    }
}
