package com.telemedicine.myclinic;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class AdjustingViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private View mAdjustableView;
    private int mOldVisibleFrameBottom;
    private int mVisibleWindowFrameBottom;
    private final int mSoftKeyboardEstimatedMinHeight;

    public AdjustingViewGlobalLayoutListener(View adjustableView) {
        mAdjustableView = adjustableView;

        Rect visibleWindowFrame = new Rect();
        mAdjustableView.getWindowVisibleDisplayFrame(visibleWindowFrame);
        mVisibleWindowFrameBottom = visibleWindowFrame.bottom;
        mSoftKeyboardEstimatedMinHeight = mVisibleWindowFrameBottom / 4;
    }

    @Override
    public void onGlobalLayout() {
        Rect visibleWindowRect = new Rect();
        mAdjustableView.getWindowVisibleDisplayFrame(visibleWindowRect);

        int[] adjustableViewCoords = {0,0};
        mAdjustableView.getLocationOnScreen(adjustableViewCoords);
        int adjustableViewBottom = adjustableViewCoords[1] + mAdjustableView.getHeight();
        int deltaVisibleWindowBottom = mOldVisibleFrameBottom - visibleWindowRect.bottom;
        ObjectAnimator translationYAnimator = null;

        if (Math.abs(deltaVisibleWindowBottom) > mSoftKeyboardEstimatedMinHeight) {
            // The visible height of the window has changed significantly enough
            // to suggest that the soft keyboard has been shown or hidden.
            boolean softKeyboardVisible = deltaVisibleWindowBottom > 0;
            // Subtract the height of the footer from the height of the soft keyboard to get the adjustment length
            int adjustmentLength = deltaVisibleWindowBottom - (mVisibleWindowFrameBottom - adjustableViewBottom);

            if (softKeyboardVisible) {
                translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", -adjustmentLength);
            } else if (Math.abs(deltaVisibleWindowBottom)
                    > ((ViewGroup.MarginLayoutParams)mAdjustableView.getLayoutParams()).bottomMargin) {
                // The soft keyboard has been hidden.
                translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", 0);
            }
        } else if (mVisibleWindowFrameBottom - visibleWindowRect.bottom > mSoftKeyboardEstimatedMinHeight
                && deltaVisibleWindowBottom != 0) {
            // The soft keyboard is most likely still visible, and an attachment, such as the text suggestion bar,
            // was added to the soft keyboard.
            translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", -deltaVisibleWindowBottom
                    + mAdjustableView.getTranslationY());
        }

        if (translationYAnimator != null) {
            // Translate the mAdjustableContainer up or down equal to the distance of the height of the soft keyboard.
            translationYAnimator.start();
        }
        mOldVisibleFrameBottom = visibleWindowRect.bottom;
    }
}
