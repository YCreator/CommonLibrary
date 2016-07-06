package com.frame.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CustomGridView extends GridView {   
	
//	private boolean isTrue = true;
	  
	 public CustomGridView(Context context, AttributeSet attrs) {   
         super(context, attrs);   
     }   
    
     public CustomGridView(Context context) {   
         super(context);   
     }   
    
     public CustomGridView(Context context, AttributeSet attrs, int defStyle) {   
         super(context, attrs, defStyle);   
     }   
    /*
     public void setEnable(boolean isTrue) {
    	 this.isTrue = isTrue;
     }*/
     @Override   
     public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                 MeasureSpec.AT_MOST);
         super.onMeasure(widthMeasureSpec, expandSpec);
     }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE){
		//	CustomGridView.this.clearFocus();
			return super.onInterceptTouchEvent(ev);
		}else{
			//CustomGridView.this.requestFocus();
			return false;
		}
	}  


}   