package com.frame.core.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.core.R;
import com.frame.core.interf.IBaseView;
import com.frame.core.util.MyPreferences;
import com.frame.core.util.StackManager;
import com.frame.core.util.TLog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
 * Created by Administrator on 2015/12/18.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseView {

    public static final String TAG = BaseAppCompatActivity.class.getSimpleName();

    public static final String EXTRA_TITLE = "actionbar_title";

    private static Class<? extends BaseAppCompatActivity> mainClazz;

    private Toolbar mActionBarToolbar;

    // 统一的加载对话框
    protected ProgressDialog mLoadingDialog;

    protected boolean isDestroyed = false;

    private int guideResourceId = 0;

    private boolean isGuideAdded = false;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        StackManager.getStackManager().pushActivity(this);
        setContentView(initPageLayoutID());
        ButterKnife.bind(this);
        initActionBar();
        init();
        initPageView();
        initPageViewListener();
        process(savedInstanceState);
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
        super.onDestroy();
        ButterKnife.unbind(this);
        StackManager.getStackManager().popActivity(this);
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
        mActionBarToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (mActionBarToolbar != null && !TextUtils.isEmpty(title) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        if (hasBackActionbar() && getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    /**
     * 设置actionbar返回键
     *
     * @return
     */
    protected boolean hasBackActionbar() {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 返回首页
     */
    protected void turnBack(Class<? extends BaseAppCompatActivity> clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            navigateUpTo(new Intent(this, clazz));
    }

    protected void onBeforeSetContentLayout() {
        setStatusStyle(R.color.theme_color);
    }

    protected void setStatusStyle(int colorId) {
        //沉淀式状态栏
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
          /*  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
            //  this.mainContent.setFitsSystemWindows(true);
            //  SystemBarTintManager.SystemBarConfig localSystemBarConfig = tintManager.getConfig();
            // this.mainContent.setPadding(0, localSystemBarConfig.getPixelInsetTop(true), 0, localSystemBarConfig.getPixelInsetBottom());
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on) {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            localLayoutParams.flags |= bits;
        } else {
            localLayoutParams.flags &= ~bits;
        }
        localWindow.setAttributes(localLayoutParams);
    }

    protected void addFragmentAndAddBackStack(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.add(containerId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    protected void replaceFragment(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    protected void replaceFragmentAndAddBackStack(@IdRes int containerId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(containerId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

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

    public void launchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    public void lauchActivity(Class<? extends Activity> cls, @Nullable Bundle bundle, int flags) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(flags);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 显示加载对话框
     *
     * @param msg          消息
     * @param isCancelable 是否可被用户关闭
     */
    public void showLoadingDialog(String msg, boolean isCancelable) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            return;
        } else {
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
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !isDestroy()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onSupportNavigateUp();
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
    public boolean isDestroy() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) || isDestroyed;
    }

    /**
     * 设置主页
     *
     * @param clazz
     */
    public static void initMainClass(Class<? extends BaseAppCompatActivity> clazz) {
        mainClazz = clazz;
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

}
