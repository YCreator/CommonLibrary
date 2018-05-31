package com.lib.commonlibrary;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * Created by yzd on 2018/5/23 0023.
 */

public final class ViewAdapter {

    /**
     *
     * @param imageView
     * @param url               图片路径
     * @param placeholderRes    占位图片id
     */
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
           /* ImageEngine.with(imageView.getContext())
                    .url(url)
                    .placeHolder(placeholderRes)
                    .into(imageView);*/
        }
    }

}
