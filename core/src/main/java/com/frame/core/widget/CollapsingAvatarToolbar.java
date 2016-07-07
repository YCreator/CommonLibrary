package com.frame.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frame.core.R;
import com.frame.core.util.PixelUtil;
import com.frame.core.util.TLog;


/**
 *
 * Created by Administrator on 2016/1/20.
 */
public class CollapsingAvatarToolbar extends FrameLayout implements AppBarLayout.OnOffsetChangedListener {

    private float collapsedMargin,
            expandedMargin,
            collapsedImageSize,
            expandedImageSize,
            collapsedTextSize,
            expandedTextSize;

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    View avatarView;
    private TextView titleView;

    private float collapsedHeight;
    private float expandedHeight;
    private float maxOffset;

    public static final int CENTER = 0;
    public static final  int LEFT = -1;
    public static final int RIGHT = 1;
    private int gravity, titleGravity;

    float colTextMargin,
          exTextMargin,
          textTopmargin,
          colMargin,
          avatarTopMargin,
          exMargin;

    boolean valuesCalculatedAlready = false;

    private CollapseChangedListener collapseChangedListener;

    public CollapsingAvatarToolbar(Context context) {
        this(context, null);
    }

    public CollapsingAvatarToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CollapsingAvatarToolbar, 0,0);
        try {
            gravity = a.getInteger(R.styleable.CollapsingAvatarToolbar_avatar_gravity, -1);
            titleGravity = a.getInteger(R.styleable.CollapsingAvatarToolbar_title_gravity, -1);

            collapsedMargin = a.getDimension(R.styleable.CollapsingAvatarToolbar_collapsedMargin, -1);
            expandedMargin = a.getDimension(R.styleable.CollapsingAvatarToolbar_expandedMargin, -1);

            collapsedImageSize = a.getDimension(R.styleable.CollapsingAvatarToolbar_collapsedImageSize, -1);
            expandedImageSize = a.getDimension(R.styleable.CollapsingAvatarToolbar_expandedImageSize, -1);

            collapsedTextSize = a.getDimension(R.styleable.CollapsingAvatarToolbar_collapsedTextSize, -1);
            expandedTextSize = a.getDimension(R.styleable.CollapsingAvatarToolbar_expandedTextSize, -1);

            textTopmargin = a.getDimension(R.styleable.CollapsingAvatarToolbar_titleTopMargin, 0);
            colMargin = a.getDimension(R.styleable.CollapsingAvatarToolbar_titleColMargin, 0);
            exMargin = a.getDimension(R.styleable.CollapsingAvatarToolbar_titleExMargin, 0);
        } finally {
            a.recycle();
        }

        if (collapsedMargin < 0) {
            collapsedMargin = PixelUtil.dip2px(context,48);
        }

        if (collapsedImageSize < 0) {
            collapsedImageSize = PixelUtil.dip2px(context,32);
        }

        if (expandedImageSize < 0) {
            expandedImageSize = PixelUtil.dip2px(context,72);
        }

        switch (gravity) {
            case CENTER:
                expandedMargin = (PixelUtil.getScreenW() - expandedImageSize) / 2;
                break;
            default:
               if (expandedMargin < 0) {
                   expandedMargin = PixelUtil.dip2px(context,18);
               }
                break;
        }

        if (collapsedTextSize < 0) {
            collapsedTextSize = PixelUtil.sp2px(context,12);
        }

        if (expandedTextSize < 0) {
            expandedTextSize = PixelUtil.sp2px(context,18);
        }

        if (colMargin == 0) {
            colMargin = PixelUtil.dip2px(this.getContext(), 12);
        }

        if (textTopmargin == 0) {
            textTopmargin = PixelUtil.dip2px(this.getContext(), 18);
        }

        avatarTopMargin = PixelUtil.dip2px(this.getContext(), -12);

    }

    public void setCollapseChangedListener(CollapseChangedListener collapseChangedListener) {
        this.collapseChangedListener = collapseChangedListener;
    }

    @NonNull
    private AppBarLayout findParentAppBarLayout() {
        ViewParent parent = this.getParent();
        if (parent instanceof AppBarLayout) {
            return ((AppBarLayout) parent);
        } else if (parent.getParent() instanceof AppBarLayout) {
            return ((AppBarLayout) parent.getParent());
        } else {
            throw new IllegalStateException("Must be inside an AppBarLayout");
            //TODO actually, a collapsingtoolbar
        }
    }

    @NonNull
    private View findAvatar() {
        View avatar = this.findViewById(R.id.cat_avatar);
        if (avatar == null) {
            throw new IllegalStateException("View with id ta_avatar not found");
        }
        return avatar;
    }

    @NonNull
    private TextView findTitle() {
        TextView title = (TextView) this.findViewById(R.id.cat_title);
        if (title == null) {
            throw new IllegalStateException("TextView with id ta_title not found");
        }
        return title;
    }

    @NonNull
    private Toolbar findSiblingToolbar() {
        ViewGroup parent = ((ViewGroup) this.getParent());
        for (int i = 0, c = parent.getChildCount(); i < c; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Toolbar) {
                return (Toolbar) child;
            }
        }
        throw new IllegalStateException("No toolbar found as sibling");
    }

    private void findViews() {
        appBarLayout = findParentAppBarLayout();
        toolbar = findSiblingToolbar();
        avatarView = findAvatar();
        titleView = findTitle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViews();
        if (!isInEditMode()) {
            appBarLayout.addOnOffsetChangedListener(this);
        } else {
            setExpandedValuesForEditMode();
        }
    }
    private void setExpandedValuesForEditMode() {
        calculateValues();
        updateViews(1f, 0);
    }

    private void updateViews(float collapsedProgress, int currentOffset) {
        float expandedPercentage = 1 - collapsedProgress;
        float translation = -currentOffset + ((float) toolbar.getHeight() * expandedPercentage);

        float currHeight = collapsedHeight + (expandedHeight - collapsedHeight) * expandedPercentage;
        float currentPadding = expandedMargin + (collapsedMargin - expandedMargin) * collapsedProgress;
        float currentImageSize = collapsedImageSize + (expandedImageSize - collapsedImageSize) * expandedPercentage;
        float currentTextSize = collapsedTextSize + (expandedTextSize - collapsedTextSize) * expandedPercentage;

        float padding = exTextMargin + (colTextMargin - exTextMargin) * collapsedProgress;
        float topPadding = textTopmargin * expandedPercentage;
        setTextMargin((int)padding, (int)topPadding);

        setContainerOffset(translation);
        setContainerHeight((int) currHeight);
        setAvatarMargin((int) currentPadding);
        setAvatarSize((int) currentImageSize);
        setTextSize(currentTextSize);
    }

    private void setTextMargin(int padding, int topMargin) {
        LayoutParams layout = (LayoutParams) titleView.getLayoutParams();
        layout.leftMargin = padding;
        layout.topMargin = topMargin;
        layout.bottomMargin = 0;
        layout.rightMargin = 0;
    }

    private void setContainerOffset(float translation) {
        this.setTranslationY(translation);
    }

    private void setContainerHeight(int currHeight) {
        this.getLayoutParams().height = currHeight;
    }

    private void setAvatarMargin(int currentMargin) {
        LayoutParams layoutParams = (LayoutParams) avatarView.getLayoutParams();
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = currentMargin;
        layoutParams.bottomMargin = 0;
        layoutParams.rightMargin = 0;
    }

    private void setTextSize(float currentTextSize) {
        if (titleView != null) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);
        }
    }

    private void setAvatarSize(int currentImageSize) {
        avatarView.getLayoutParams().height = currentImageSize;
        avatarView.getLayoutParams().width = currentImageSize;
    }

    private void calculateValues() {

        collapsedHeight = toolbar.getHeight();
        expandedHeight = appBarLayout.getHeight() - toolbar.getHeight() - 50;
        maxOffset = expandedHeight;
        TLog.i(TAG+"_total", PixelUtil.dip2px(this.getContext(),180)+"");
        TLog.i(TAG+"_collapsedHeight",collapsedHeight+"");
        TLog.i(TAG+"_expandedHeight",expandedHeight+"");

        switch (titleGravity) {
            case CENTER:
                exTextMargin = (PixelUtil.getScreenW() - titleView.getMeasuredWidth()) /2;
                textTopmargin = expandedImageSize / 2 + textTopmargin;
                break;
            default:
                exTextMargin = exMargin;
                break;
        }
        colTextMargin =collapsedMargin + collapsedImageSize + colMargin;
      /*  exTextMargin = (ScreenUtils.getScreenW() - titleView.getMeasuredWidth()) /2;
        TLog.log(TAG+"_titleView",titleView.getMeasuredWidth()+"");
        colTextMargin =ScreenUtils.dip2px(this.getContext(), 48)
                + ScreenUtils.dip2px(this.getContext(), 32)
                + ScreenUtils.dip2px(this.getContext(), 12);
        textTopmargin = ScreenUtils.dip2px(this.getContext(), 72)/2 + ScreenUtils.dip2px(this.getContext(), 18);*/
    }

    final String TAG = this.getClass().getSimpleName();

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        TLog.i(TAG,verticalOffset+"_"+appBarLayout.getHeight());
        if (!valuesCalculatedAlready) {
            calculateValues();
            valuesCalculatedAlready = true;
        }
        float collapsedProgress = -verticalOffset / maxOffset;
        updateViews(collapsedProgress, verticalOffset);
        notifyListener(collapsedProgress);
    }

    private void notifyListener(float collapsedProgress) {
        if (collapseChangedListener != null) {
            collapseChangedListener.onCollapseChanged(collapsedProgress);
        }
    }

    public interface CollapseChangedListener {

        void onCollapseChanged(float collapsedProgress);
    }
}
