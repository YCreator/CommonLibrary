package com.frame.core.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.core.R;

/**
 * 提示
 *
 */
public class DRecyclerViewPrompt extends FrameLayout {

    private View frameView;
    private ImageView mPromptImageView;
    private ProgressBar mPromptProgressBar;
    private TextView mPromptTextView;

    public DRecyclerViewPrompt(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.d_recycler_footer_layout, this);
        frameView = view.findViewById(R.id.loadmore_frame);
        mPromptImageView = (ImageView) view.findViewById(R.id.footer_image);
        mPromptProgressBar = (ProgressBar) view.findViewById(R.id.footer_progressbar);
        mPromptTextView = (TextView) view.findViewById(R.id.footer_text);
    }

    public void resizeLarge() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.d_recycler_prompt_large_height)));
    }

    public void resizeFooter() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.d_recycler_prompt_footer_height)));
    }

    public void hide() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
    }

    public void loading() {
        resizeLarge();
        setClickable(false);
        mPromptImageView.setVisibility(View.GONE);
        mPromptProgressBar.setVisibility(View.VISIBLE);
        mPromptTextView.setVisibility(View.GONE);
    }

    public void empty(String hint) {
        resizeLarge();
        setClickable(false);
        mPromptImageView.setVisibility(View.VISIBLE);
        mPromptProgressBar.setVisibility(View.GONE);
        mPromptTextView.setVisibility(View.VISIBLE);
        mPromptTextView.setText(hint);
    }

    public void moreLoading() {
        resizeFooter();
        setClickable(false);
        mPromptImageView.setVisibility(View.GONE);
        mPromptProgressBar.setVisibility(View.VISIBLE);
        mPromptTextView.setVisibility(View.GONE);
    }

    public void moreButton() {
        resizeFooter();
        setClickable(true);
        mPromptImageView.setVisibility(View.GONE);
        mPromptProgressBar.setVisibility(View.GONE);
        mPromptTextView.setVisibility(View.VISIBLE);
        mPromptTextView.setText("LOAD MORE");
    }

    public void moreEnd() {
        resizeFooter();
        setClickable(false);
        mPromptImageView.setVisibility(View.GONE);
        mPromptProgressBar.setVisibility(View.GONE);
        mPromptTextView.setVisibility(View.VISIBLE);
        mPromptTextView.setText("THE END");
    }

}
