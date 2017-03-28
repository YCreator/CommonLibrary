分享库主要集成了友盟分享，其中包含了分享到qq，微信，新浪，支付宝，钉钉等平台。

主要类：ShareHelper.java，单例模式

项目引用说明：引入sharelib依赖，获取ShareHelper单例，在application中初始化配置，需要分享到哪个平台，就添加配置哪个平台
申请的appId或key,（如：要分享到qq，则条用setQQ(id,secret)方法），最后再执行init方法，将从友盟申请的key设置进去。最后
在需要分享的地方直接调用share方法。代码如下：

在application的onCreate方法中：
    例如： ShareHelper.getInstance()
                    .setSina("2635404820", "ec743121ef7d06c707e0a01f69e615cc")
                    .setQQ("1104741678", "3sFFig9X22svZsXo")
                    .setDebug(true)     //调试模式
                    .init(this, "587c8ddc734be4160b001c6d");

在需要调用分享的activity的点击事件中：
    例如： UMImage image = new UMImage(this, R.drawable.umeng_socialize_qq);
          image.setThumb(new UMImage(this, R.mipmap.ic_launcher));
          ShareEntity entity = new ShareEntity("test", "https://www.baidu.com", "hello world", image);
          ShareHelper.getInstance().share(this, entity, SHARE_MEDIA.SINA);

还需要重载activity中的onActivityResult方法：
    例如： @Override
          protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                 super.onActivityResult(requestCode, resultCode, data);
                 UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

          }

在项目AndrroidManifest.xml中的配置：
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />

        <uses-permission android:name="android.permission.GET_TASKS" />

        <activity
                    android:name="com.umeng.qq.tencent.AuthActivity"
                    android:launchMode="singleTask"
                    android:noHistory="true" >
                    <intent-filter>
                        <action android:name="android.intent.action.VIEW" />

                        <category android:name="android.intent.category.DEFAULT" />
                        <category android:name="android.intent.category.BROWSABLE" />

                        <data android:scheme="tencent1104741678(这是qq的appId)" />
                    </intent-filter>
                </activity>