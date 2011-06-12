package com.quran.labs.androidquran.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

public class QuranTextView extends TextView /*implements OnTouchListener*/ {

	public static final String TAG = "QuranTextView";

	// Remember some things for zooming
	//private PointF start = new PointF();
	//private PointF scrollPoint = new PointF();
	//private PointF mid = new PointF();

	//private float oldDist = 1f;
	//private PointF oldDistPoint = new PointF();

	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;

	//private int mode = NONE;
	
	private int originalWidth;
	private int originalHeight;
	private float originalTextSize;
	
	private float curentScaleX = 1.0f;
	private float curentScaleY = 1.0f;
	
	private static final float MAX_SCALE_X = 2.5f;
	private static final float MAX_SCALE_Y = 2.5f;
	
	private static final float MIN_SCALE_X = 1.0f;
	private static final float MIN_SCALE_Y = 1.0f;
	
	public static boolean DEBUG = false;

	public QuranTextView(Context context) {
		super(context);
		init();
	}

	public QuranTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuranTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Sets the text that this TextView is to display (see
	 * {@link #setText(CharSequence)}) and also sets whether it is stored in a
	 * styleable/spannable buffer and whether it is editable.
	 * 
	 * @attr ref android.R.styleable#TextView_text
	 * @attr ref android.R.styleable#TextView_bufferType
	 */
	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		setMovementMethod(LongLinkMovementMethod.getInstance());
	}

	private void init() {
		/*setOnTouchListener(this);*/
	}

	/*@Override
	public boolean onTouch(View view, MotionEvent event) {
		super.onTouchEvent(event);
		
		if (originalWidth == 0){
			originalWidth = getWidth();
			originalHeight = getHeight();
			originalTextSize = getTextSize();
		}
		
		dumpEvent(event);
		
		Log.d(TAG, "mode=DRAG");
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			start.set(event.getX(), event.getY());
			scrollPoint.set(getScrollX(), getScrollY());
			
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;

			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				mid = midPoint(event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			System.out.println("current time :" + System.currentTimeMillis());
			break;// return !gestureDetector.onTouchEvent(event);
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Log.d(TAG, "mode=NONE");
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				int x = (int) (event.getX() - start.x);
				int y = (int) (event.getY() - start.y);
				dragBy(x, y);
				
			} else if (mode == ZOOM) {
				float newD = spacing(event);
				PointF newDist = spacingPoint(event);
				
				mid = midPoint(event);
				
				Log.e(TAG, "newDist=" + newDist);
				float relativeScale = (newD / oldDist) - 1;
				zoom(relativeScale, relativeScale, mid);
			}
			break;
		}
		return true;
	}*/
	
	/**
	 * scaling is done from here
	 */
	public void scaleTo(Float scaleX, Float scaleY) {
		setTextSize(originalTextSize * scaleX);
		
		getLayoutParams().height = (int)(originalHeight * scaleY);
		getLayoutParams().width = (int)(originalWidth * scaleX);
		setWidth((int)(originalWidth * scaleX));
		setHeight((int)(originalHeight * scaleY));
	}
	
	public void dragBy(int x, int y){
		
		if (getLeft() + x > 0 && x > 0){
			if (getLeft() < 0){
				x = -getLeft();
			} else {
				x = 0;
			}
		}
		
		if (getTop() + y > 0 && y > 0){
			if (getTop() < 0){
				y = -getTop();
			} else {
				y = 0;
			}
		}
		
		if (getRight() + x < originalWidth && x < 0){
			if (getRight() > originalWidth){
				x = -(getRight() - originalWidth);
			} else {
				x = 0;
			}
		}
		
		if (getBottom() + y < originalHeight && y < 0){
			if (getBottom() > originalHeight){
				y = -(getBottom() - originalHeight);
			} else {
				y = 0;
			}
		}
		
		if (x == 0 && y == 0){
			return; // Don't update layout
		}
		
		layout(getLeft() + x, getTop() + y, getRight() + x, getBottom() + y);
	}
	
	public void zoomIn(){
		if (curentScaleX != MAX_SCALE_X && curentScaleY != MAX_SCALE_Y){
			zoom(0.5f, 0.5f, true);
		}
	}
	
	public void zoomOut(){
		if (curentScaleX != MIN_SCALE_X && curentScaleY != MIN_SCALE_Y){
			zoom(-0.5f, -0.5f, true);
		}
	}
	
	public void zoomFit() {
		if (curentScaleX != MIN_SCALE_X && curentScaleY != MIN_SCALE_Y){
			zoom(-MAX_SCALE_X, -MAX_SCALE_Y, true);
		}
	}
	
	public void zoom(Float scaleX, Float scaleY, boolean center) {
		PointF pivot = new PointF();
		if (center){
			pivot.x = getWidth() / 2;
			pivot.y = getHeight() / 2;
		} else {
			pivot.x = 0;
			pivot.y = 0;
		}
		zoom(scaleX, scaleY, pivot);
	}
	
	/**
	 * zooming is done from here
	 */
	public void zoom(Float scaleX, Float scaleY, final PointF pivot) {
		
		if (originalWidth == 0){
			originalWidth = getWidth();
			originalHeight = getHeight();
			originalTextSize = getTextSize();
		}
		
		final PointF pivotNewPos = new PointF();
		
		/*if (curentScaleX != MIN_SCALE_X && curentScaleY != MIN_SCALE_Y
		 && curentScaleX != MAX_SCALE_X && curentScaleY != MAX_SCALE_Y){*/
			
			if (curentScaleX + scaleX > MAX_SCALE_X || curentScaleY + scaleY > MAX_SCALE_Y){
				curentScaleX = MAX_SCALE_X;
				curentScaleY = MAX_SCALE_Y;
			} else if (curentScaleX + scaleX < MIN_SCALE_X || curentScaleY + scaleY < MIN_SCALE_Y){
				curentScaleX = MIN_SCALE_X;
				curentScaleY = MIN_SCALE_Y;
			} else {
				curentScaleX += scaleX;
				curentScaleY += scaleY;
			}
			/*} else {
			return; // Don't make any zooming if in max zoom or min zoom
		}*/
		
		scaleTo(curentScaleX, curentScaleY);
		
		pivotNewPos.x = pivot.x / originalWidth * getLayoutParams().width;
		pivotNewPos.y = pivot.y / originalHeight * getLayoutParams().height;
		
		post(new Runnable() {
			
			int dragByX = -(int)(pivotNewPos.x - pivot.x);
			int dragByY = -(int)(pivotNewPos.y - pivot.y);
			
			@Override
			public void run() {
				dragBy(dragByX, dragByY);
			}
		});
	}

	/**
	 * space between the first two fingers
	 */
	/*private float spacing(MotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}*/

	/*private PointF spacingPoint(MotionEvent event) {
		PointF f = new PointF();
		f.x = event.getX(0) - event.getX(1);
		f.y = event.getY(0) - event.getY(1);
		return f;
	}*/

	/**
	 * the mid point of the first two fingers
	 */
	/*private PointF midPoint(MotionEvent event) {
		PointF f = new PointF();
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		f.set(x / 2, y / 2);
		return f;
	}*/
	
	public void onAyaClick(String aya){
		String suraNum = aya.split("/")[1];
		String ayaNum = aya.split("/")[2];
		Toast.makeText(getContext(), "Normal click on Sura: " + suraNum + ", and Aya: " + ayaNum, Toast.LENGTH_SHORT).show();
	}
	
	public void onAyaLongClick(String aya){
		String suraNum = aya.split("/")[1];
		String ayaNum = aya.split("/")[2];
		Toast.makeText(getContext(), "Long click on Sura: " + suraNum + ", and Aya: " + ayaNum, Toast.LENGTH_SHORT).show();
	}

	/** Show an event in the LogCat view, for debugging */
	/*private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		Log.d(TAG, sb.toString());
	}*/
}
