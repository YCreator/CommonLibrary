package com.frame.core.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.core.R;
import com.frame.core.interf.IBaseView;
import com.frame.core.rx.Lifeful;
import com.frame.core.util.MyPreferences;
import com.frame.core.util.TLog;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * AppCompatActivity基类
 * Activity生命周期
 * onCreate()
 * onStart()
 * onResume()
 * onPause()
 * onStop()
 * onDestroy()
 * Created by yzd on 2015/12/18.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseView, Lifeful {

    public static final String TAG = BaseAppCompatActivity.class.getSimpleName();

    public static final String EXTRA_TITLE = "actionbar_title";

    private Toolbar mActionBarToolbar;

    // 统一的加载对话框
    protected ProgressDialog mLoadingDialog;

    protected boolean isDestroyed = false;

    private int guideResourceId = 0;

    private boolean isGuideAdded = false;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onBeforeSetContentLayout(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(initPageLayoutID());
        ButterKnife.bind(this);
        initActionBar();
        setStatusBar();
        init();
        initPageView();
        initPageViewListener();
        process(savedInstanceState);
    }

    /**
     * 返回主布局id
     */
    @LayoutRes
    protected abstract int initPageLayoutID();

    /**
     * 初始化toolbar
     */
    @SuppressLint("PrivateResource")
    protected void initActionBar() {
        if (getActionBarToolbar() == null) {
            return;
        }
        mActionBarToolbar.setBackgroundColor(getToolbarColor());
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (mActionBarToolbar != null && !TextUtils.isEmpty(title) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        if (hasBackActionbar() && getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            mActionBarToolbar.setNavigationIcon(setBackIcon() == 0 ? R.drawable.ic_arrow_back_white_18dp : setBackIcon());
            mActionBarToolbar.setNavigationOnClickListener(v -> finishPage());
        }
    }

    /**
     * 获取toolbar对象
     * @return
     */
    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    /**
     * 设置状态栏样式
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getToolbarColor());
    }

    /**
     * 初始化
     */
    protected void init() {
    }

    /**
     * 逻辑处理
     */
    protected void process(Bundle savedInstanceState) {
    }

    @Override
    protected void onDestroy() {
        TLog.i("Activity", "onDestroy");
        isDestroyed = true;
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        ButterKnife.unbind(this);
    }

    /**
     * 设置actionbar返回键
     *
     * @return
     */
    protected boolean hasBackActionbar() {
        return true;
    }

    /**
     * 设置返回键图标
     *
     * @return
     */
    @DrawableRes
    protected int setBackIcon() {
        return 0;
    }

    /**
     * 设置toolbar背景颜色
     * @return
     */
    @ColorInt
    protected int getToolbarColor() {
        return this.getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBeforeFinish();
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 结束页面
     */
    public void finishPage() {
        onBeforeFinish();
        finish();
    }

    /**
     * 初始化布局前回调
     */
    protected void onBeforeSetContentLayout(Bundle savedInstanceState) {
    }

    /**
     * 结束页面前回调
     */
    protected void onBeforeFinish() {

    }

    /**
     * 添加fragment并记录fragment状态
     * @param containerId
     * @param fragment
     * @param tag
     */
    protected void addFragmentAndAddBackStack(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.add(containerId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换fragment
     * @param containerId
     * @param fragment
     * @param tag
     */
    protected void replaceFragment(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换fragment并记录fragment状态
     * @param containerId
     * @param fragment
     * @param tag
     */
    protected void replaceFragmentAndAddBackStack(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(containerId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 切换fragment
     * @param id
     * @param from
     * @param to
     * @param tag
     */
    protected void switchFragment(int id, Fragment from, Fragment to, String tag) {
        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
        TLog.i("switchFragment", to.isAdded() + "");
        if (!to.isAdded()) {
            transation.hide(from).add(id, to, tag).show(to).commitAllowingStateLoss();
        } else {
            transation.hide(from).show(to).commitAllowingStateLoss();
        }
    }

    /**
     * 启动activity
     *
     * @param cls
     */
    public void launchActivity(Class<? extends Activity> cls) {
        launchActivity(cls, null);
    }

    /**
     * 带参数启动activity
     * @param cls
     * @param bundle
     */
    public void launchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 带参数并设置flag启动activity
     * @param cls
     * @param bundle
     */
    public void launchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle, int flags) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(flags);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 带有回调结果启动activity
     * @param cls
     * @param requestCode
     * @param bundle
     */
    public void launchActivityForResult(Class<? extends Activity> cls, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void showLoadingDialog() {

    }

    /**
     * 显示加载对话框
     *
     * @param msg          消息
     * @param isCancelable 是否可被用户关闭
     */
    public void showLoadingDialog(String msg, boolean isCancelable) {
        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            mLoadingDialog = new ProgressDialog(this);
            mLoadingDialog.setMessage(msg);
            mLoadingDialog.setIndeterminate(true);
            mLoadingDialog.setCancelable(isCancelable);
            mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mLoadingDialog.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !isDestroyed()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishPage();
        }
        return false;
    }

    @Override
    public void initData() {

    }

    /**
     * 判断当前activity是否被销毁
     *
     * @return
     */
    public boolean isDestroyed() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && super.isDestroyed()) || isDestroyed;
    }

    /**
     * 添加引导
     *
     * @param layoutId
     */
    public void addGuideImage(@IdRes int layoutId) {
        if (!isGuideAdded) {
            if (MyPreferences.activityIsGuided(this, this.getClass().getName())) return;
            View view = getWindow().getDecorView().findViewById(layoutId);
            if (view == null) return;
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof FrameLayout) {
                final FrameLayout frameLayout = (FrameLayout) viewParent;
                if (guideResourceId != 0) {
                    final ImageView guideImage = new ImageView(this);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    guideImage.setLayoutParams(params);
                    guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    guideImage.setImageResource(guideResourceId);
                    guideImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            frameLayout.removeView(guideImage);
                            MyPreferences.setIsGuided(getApplicationContext(), BaseAppCompatActivity.this.getClass().getName());//设为已引导
                        }
                    });
                    frameLayout.addView(guideImage);
                    isGuideAdded = true;
                }
            }
        }
    }

    /**
     * 设置引导资源图片
     *
     * @param resId
     */
    protected void setGuideResourceId(@DrawableRes int resId) {
        this.guideResourceId = resId;
    }

    public Handler getHandler() {
        synchronized (this) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
        }
        return handler;
    }

    /**
     * 设置遮罩层灰度
     * @param bgAlpha
     */
    public void backgroundAlpha(Float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

    @Override
    public boolean isAlive() {
        return activityIsAlive();
    }

    public boolean activityIsAlive() {
        if (getBaseContext() == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(this.isDestroyed() || this.isFinishing());
        } else {
            return !this.isFinishing();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TLog.i("Activity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        TLog.i("Activity", "onPause");
    }

}
