package ru.yandex.yamblz.ui;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by dan on 16.07.16.
 */
public class CustomLayout extends ViewGroup {
    int deviceWidth;

    public CustomLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int numOfChildren = getChildCount();
        int curLeft = l;
        for (int i = 0; i < numOfChildren; i++) {
            View child = getChildAt(i);
            child.layout(curLeft, t, curLeft + child.getMeasuredWidth(), t + child.getMeasuredHeight());
            curLeft +=  child.getMeasuredWidth();
        }
    }



    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int numOfChildren = getChildCount();
        int indexOfMatchParent = -1;
        int childrenWidth = 0;
        int childrenHeight = 0;
        int maxHeight = 0;
        int maxWidth = 0;

        LayoutParams childLayoutParamsParent = null;
        View childMatchParent = null;

        for (int i = 0; i < numOfChildren; i++) {
            View child = getChildAt(i);
            maxHeight = Math.max(maxHeight, child.getLayoutParams().height);
        }

        for (int i = 0; i < numOfChildren; i++) {
            View child = getChildAt(i);
            LayoutParams childLayoutParams = child.getLayoutParams();
            if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
                indexOfMatchParent = i;
                childMatchParent = child;
                childLayoutParamsParent = childLayoutParams;
                continue;
            }
            if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
                measureChild(child, MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY) );
            }
            else {
                measureChild(child, MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY) );
            }
            childrenHeight = Math.max(child.getMeasuredHeight(), childrenHeight);
            childrenWidth += child.getMeasuredWidth();
            maxWidth = Math.max(child.getMeasuredWidth(), maxWidth);
        }

        if (indexOfMatchParent != -1) {
            View child = getChildAt(indexOfMatchParent);
            if (child.getLayoutParams().height == LayoutParams.MATCH_PARENT){
                measureChild(childMatchParent, MeasureSpec.makeMeasureSpec(Math.max(MeasureSpec.getSize(widthMeasureSpec)
                        - childrenWidth, 0), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY));
            } else {
                measureChild(childMatchParent, MeasureSpec.makeMeasureSpec(Math.max(MeasureSpec.getSize(widthMeasureSpec)
                        - childrenWidth, 0), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childLayoutParamsParent.height, MeasureSpec.EXACTLY));
            }
            childrenWidth += childMatchParent.getMeasuredWidth();
        }

        setMeasuredDimension(resolveSize(childrenWidth, widthMeasureSpec), resolveSize(childrenHeight, heightMeasureSpec));

    }

}

