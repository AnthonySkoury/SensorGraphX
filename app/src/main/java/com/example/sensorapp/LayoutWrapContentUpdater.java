package com.example.sensorapp;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


public class LayoutWrapContentUpdater
{
    public static final String TAG = LayoutWrapContentUpdater.class.getName();


    public static final void wrapContentAgain( ViewGroup subTreeRoot )
    {
        wrapContentAgain( subTreeRoot, false, MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED );
    }
    public static final void wrapContentAgain( ViewGroup subTreeRoot, boolean relayoutAllNodes )
    {
        wrapContentAgain( subTreeRoot, relayoutAllNodes, MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED );
    }

    public static void wrapContentAgain( ViewGroup subTreeRoot, boolean relayoutAllNodes,
                                         int subTreeRootWidthMeasureSpec, int subTreeRootHeightMeasureSpec  )
    {
        Log.d(TAG, "+++ LayoutWrapContentUpdater wrapContentAgain on subTreeRoot=["+ subTreeRoot +"], with w="
                + subTreeRootWidthMeasureSpec +" and h="+ subTreeRootHeightMeasureSpec );
        assert( "main".equals( Thread.currentThread().getName() ) );

        if (subTreeRoot == null)
            return;
        LayoutParams layoutParams = subTreeRoot.getLayoutParams();

        int widthMeasureSpec 	= subTreeRootWidthMeasureSpec;
        if ( layoutParams.width  != LayoutParams.WRAP_CONTENT && subTreeRoot.getWidth() > 0 )
            widthMeasureSpec 	=  MeasureSpec.makeMeasureSpec( subTreeRoot.getWidth(), MeasureSpec.EXACTLY );
        int heightMeasureSpec 	= subTreeRootHeightMeasureSpec;
        if ( layoutParams.height != LayoutParams.WRAP_CONTENT && subTreeRoot.getHeight() > 0 )
            heightMeasureSpec 	=  MeasureSpec.makeMeasureSpec( subTreeRoot.getHeight(), MeasureSpec.EXACTLY );
        subTreeRoot.measure( widthMeasureSpec, heightMeasureSpec );

        recurseWrapContent( subTreeRoot, relayoutAllNodes );

        subTreeRoot.requestLayout();
        return;
    }


    private static void recurseWrapContent( View nodeView, boolean relayoutAllNodes )
    {
        if ( nodeView.getVisibility() == View.GONE ) {
            return;
        }

        LayoutParams layoutParams = nodeView.getLayoutParams();
        boolean isWrapWidth  = ( layoutParams.width  == LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;
        boolean isWrapHeight = ( layoutParams.height == LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;

        if ( isWrapWidth || isWrapHeight ) {

            boolean changed = false;
            int right  = nodeView.getRight();
            int bottom = nodeView.getBottom();

            if ( isWrapWidth  && nodeView.getMeasuredWidth() > 0 ) {
                right = nodeView.getLeft() + nodeView.getMeasuredWidth();
                changed = true;
                Log.v(TAG, "+++ LayoutWrapContentUpdater recurseWrapContent set Width to "+ nodeView.getMeasuredWidth() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }
            if ( isWrapHeight && nodeView.getMeasuredHeight() > 0 ) {
                bottom = nodeView.getTop() + nodeView.getMeasuredHeight();
                changed = true;
                Log.v(TAG, "+++ LayoutWrapContentUpdater recurseWrapContent set Height to "+ nodeView.getMeasuredHeight() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }

            if (changed) {
                nodeView.layout( nodeView.getLeft(), nodeView.getTop(), right, bottom );
                // FIXME: Adjust left & top position when gravity = "center" / "bottom" / "right"
            }
        }

        if ( nodeView instanceof ViewGroup ) {
            ViewGroup nodeGroup = (ViewGroup)nodeView;
            for (int i = 0; i < nodeGroup.getChildCount(); i++) {
                recurseWrapContent( nodeGroup.getChildAt(i), relayoutAllNodes );
            }
        }
        return;
    }

}
