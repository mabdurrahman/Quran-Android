package com.quran.labs.androidquran.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

// http://stackoverflow.com/questions/5286115
public class QuranGallery extends Gallery {
	
	// Gallery Scrolling Speed
	/*public static final int GALLERY_SCROLLING_SPEED = 250;

	private float mInitialX;
    private float mInitialY;
    private boolean mNeedToRebase;
    private boolean mIgnore;*/
	
	/** 
     * The distance the user has to move their finger, in density independent
     * pixels, before we count the motion as A) intended for the ScrollView if
     * the motion is in the vertical direction or B) intended for ourselfs, if
     * the motion is in the horizontal direction - after the user has moved this
     * amount they are "locked" into this direction until the next ACTION_DOWN
     * event
     */
    private static final int DRAG_BOUNDS_IN_DP = 20;

    /**
     * A value representing the "unlocked" state - we test all MotionEvents
     * when in this state to see whether a lock should be make
     */
    private static final int SCROLL_LOCK_NONE = 0;

    /**
     * A value representing a lock in the vertical direction - once in this state
     * we will never redirect MotionEvents from the ScrollView to ourself
     */
    private static final int SCROLL_LOCK_VERTICAL = 1;

    /**
     * A value representing a lock in the horizontal direction - once in this
     * state we will not deliver any more MotionEvents to the ScrollView, and
     * will deliver them to ourselves instead.
     */
    private static final int SCROLL_LOCK_HORIZONTAL = 2;

    /**
     * The drag bounds in density independent pixels converted to actual pixels
     */
    private int mDragBoundsInPx = 0;

    /**
     * The coordinates of the intercepted ACTION_DOWN event
     */
    private float mTouchStartX;
    private float mTouchStartY;

    /**
     * The current scroll lock state
     */
    private int mScrollLock = SCROLL_LOCK_NONE;

    
	public QuranGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCustomGallery(context);
	}
	
	private void initCustomGallery(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        mDragBoundsInPx = (int) (scale*DRAG_BOUNDS_IN_DP + 0.5f);
    }

	/*@Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        if (mNeedToRebase) {
            mNeedToRebase = false;
            distanceX = 0;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }*/
    
    /**
     * This will be called before the intercepted views onTouchEvent is called
     * Return false to keep intercepting and passing the event on to the target view
     * Return true and the target view will recieve ACTION_CANCEL, and the rest of the
     * events will be delivered to our onTouchEvent
     *
     * Checkout this post at stack overflow
     * http://stackoverflow.com/questions/4918548
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            mTouchStartX = ev.getX();
            mTouchStartY = ev.getY();
            mScrollLock = SCROLL_LOCK_NONE;

            /**
             * Deliver the down event to the Gallery to avoid jerky scrolling
             * if we decide to redirect the ScrollView events to ourself
             */
            super.onTouchEvent(ev);
            break;

        case MotionEvent.ACTION_MOVE:
            if (mScrollLock == SCROLL_LOCK_VERTICAL) {
                // keep returning false to pass the events
                // onto the ScrollView
                return false;
            }

            final float touchDistanceX = (ev.getX() - mTouchStartX);
            final float touchDistanceY = (ev.getY() - mTouchStartY);

            if (Math.abs(touchDistanceY) > mDragBoundsInPx) {
                mScrollLock = SCROLL_LOCK_VERTICAL;
                return false;
            }
            if (Math.abs(touchDistanceX) > mDragBoundsInPx) {
                mScrollLock = SCROLL_LOCK_HORIZONTAL; // gallery action
                return true; // redirect MotionEvents to ourself
            }
            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            // if we're still intercepting at this stage, make sure the gallery
            // also recieves the up/cancel event as we gave it the down event earlier
            super.onTouchEvent(ev);
            break;
        }

        return false;
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mIgnore = false;
                mNeedToRebase = true;
                mInitialX = e.getX();
                mInitialY = e.getY();
                return false;
            }

            case MotionEvent.ACTION_MOVE: {
                if (!mIgnore) {
                    float deltaX = Math.abs(e.getX() - mInitialX);
                    float deltaY = Math.abs(e.getY() - mInitialY);
                    mIgnore = deltaX < deltaY;
                    return !mIgnore;
                }
                return false;
            }
            default: {
                return super.onInterceptTouchEvent(e);
            }
        }
    }*/
    
    /**
     * Checkout this post at stack overflow
     * http://stackoverflow.com/questions/2373617/how-to-stop-scrolling-in-a-gallery-widget
     */
    /*@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	boolean leftScroll = false;
		if (e1 != null && e2 != null)
			leftScroll = isScrollingLeft(e1, e2);

		if (leftScroll) {
			velocityX = GALLERY_SCROLLING_SPEED;
		} else {
			velocityX = -GALLERY_SCROLLING_SPEED;
		}

    	return super.onFling(e1, e2, velocityX, velocityY);
    }
    
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}*/
}
