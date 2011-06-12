/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quran.labs.androidquran.widgets;

import com.quran.labs.androidquran.widgets.span.LongClickableSpan;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.text.*;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.*;
import android.view.View;
import android.widget.TextView;

public class LongLinkMovementMethod extends ScrollingMovementMethod {
	private static final int CLICK = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	private static final int LONG_PRESS_DELAY = 400;

	private long mStartPress;
	private boolean mHasPerformedLongPress;

	private int first;
	private int last;

	@Override
	public boolean onKeyDown(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (event.getRepeatCount() == 0) {
				if (action(CLICK, widget, buffer)) {
					return true;
				}
			}
		}

		return super.onKeyDown(widget, buffer, keyCode, event);
	}

	@Override
	protected boolean up(TextView widget, Spannable buffer) {
		if (action(UP, widget, buffer)) {
			return true;
		}

		return super.up(widget, buffer);
	}

	@Override
	protected boolean down(TextView widget, Spannable buffer) {
		if (action(DOWN, widget, buffer)) {
			return true;
		}

		return super.down(widget, buffer);
	}

	@Override
	protected boolean left(TextView widget, Spannable buffer) {
		if (action(UP, widget, buffer)) {
			return true;
		}

		return super.left(widget, buffer);
	}

	@Override
	protected boolean right(TextView widget, Spannable buffer) {
		if (action(DOWN, widget, buffer)) {
			return true;
		}

		return super.right(widget, buffer);
	}

	private boolean action(int what, TextView widget, Spannable buffer) {
		Layout layout = widget.getLayout();

		int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
		int areatop = widget.getScrollY();
		int areabot = areatop + widget.getHeight() - padding;

		int linetop = layout.getLineForVertical(areatop);
		int linebot = layout.getLineForVertical(areabot);

		int first = layout.getLineStart(linetop);
		int last = layout.getLineEnd(linebot);

		ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);

		int a = Selection.getSelectionStart(buffer);
		int b = Selection.getSelectionEnd(buffer);

		int selStart = Math.min(a, b);
		int selEnd = Math.max(a, b);

		if (selStart < 0) {
			if (buffer.getSpanStart(FROM_BELOW) >= 0) {
				selStart = selEnd = buffer.length();
			}
		}

		if (selStart > last)
			selStart = selEnd = Integer.MAX_VALUE;
		if (selEnd < first)
			selStart = selEnd = -1;

		switch (what) {
		case CLICK:
			if (selStart == selEnd) {
				return false;
			}

			LongClickableSpan[] link = buffer.getSpans(selStart, selEnd, LongClickableSpan.class);

			if (link.length != 1)
				return false;

			link[0].onClick(widget);
			break;

		case UP:
			int beststart,
			bestend;

			beststart = -1;
			bestend = -1;

			for (int i = 0; i < candidates.length; i++) {
				int end = buffer.getSpanEnd(candidates[i]);

				if (end < selEnd || selStart == selEnd) {
					if (end > bestend) {
						beststart = buffer.getSpanStart(candidates[i]);
						bestend = end;
					}
				}
			}

			if (beststart >= 0) {
				Selection.setSelection(buffer, bestend, beststart);
				return true;
			}

			break;

		case DOWN:
			beststart = Integer.MAX_VALUE;
			bestend = Integer.MAX_VALUE;

			for (int i = 0; i < candidates.length; i++) {
				int start = buffer.getSpanStart(candidates[i]);

				if (start > selStart || selStart == selEnd) {
					if (start < beststart) {
						beststart = start;
						bestend = buffer.getSpanEnd(candidates[i]);
					}
				}
			}

			if (bestend < Integer.MAX_VALUE) {
				Selection.setSelection(buffer, beststart, bestend);
				return true;
			}

			break;
		}

		return false;
	}

	public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public boolean onTouchEvent(final TextView widget, final Spannable buffer, final MotionEvent event) {
		dumpEvent(event);

		int action = event.getAction();

		LongClickableSpan link = getLongClickableSpan(widget, event, buffer);

		if (link != null) {

			if (action == MotionEvent.ACTION_DOWN) {
				mStartPress = System.currentTimeMillis();

				setFocused(first, last, buffer, link, true);
				widget.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (!mHasPerformedLongPress) {
							event.setAction(MotionEvent.ACTION_UP);
							widget.dispatchTouchEvent(event);
						} else {
							removeFocus(first, last, buffer);
						}

					}
				}, LONG_PRESS_DELAY);

				mHasPerformedLongPress = false;
				
				widget.invalidate();

				return true;
			} else if (action == MotionEvent.ACTION_UP) {
				if (mHasPerformedLongPress == false) {
					mHasPerformedLongPress = true;
					setFocused(first, last, buffer, link, false);
					long currentTime = System.currentTimeMillis();
					if (currentTime - mStartPress > LONG_PRESS_DELAY) {
						link.onLongClick(widget);
						setSelection(first, last, buffer, link);
					} else {
						link.onClick(widget);
					}
					mStartPress = 0;
					
					widget.invalidate();
				}

				return true;
			} else if (action == MotionEvent.ACTION_CANCEL){
				mHasPerformedLongPress = true;
				mStartPress = 0;
				
				return true;
			}
			
		} else {
			removeFocus(first, last, buffer);
			removeSelection(first, last, buffer);
			widget.invalidate();
		}

		return super.onTouchEvent(widget, buffer, event);
	}

	public void initialize(TextView widget, Spannable text) {
		Selection.removeSelection(text);
		text.removeSpan(FROM_BELOW);
	}

	public void onTakeFocus(TextView view, Spannable text, int dir) {
		Selection.removeSelection(text);

		if ((dir & View.FOCUS_BACKWARD) != 0) {
			text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
		} else {
			text.removeSpan(FROM_BELOW);
		}
	}

	private LongClickableSpan getLongClickableSpan(TextView widget, MotionEvent e, Spannable buffer) {
		int x = (int) e.getX();
		int y = (int) e.getY();

		x -= widget.getTotalPaddingLeft();
		y -= widget.getTotalPaddingTop();

		x += widget.getScrollX();
		y += widget.getScrollY();

		Layout layout = widget.getLayout();
		int line = layout.getLineForVertical(y);
		int off = layout.getOffsetForHorizontal(line, x);

		int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();

		int areatop = widget.getScrollY();
		int areabot = areatop + widget.getHeight() - padding;

		int linetop = layout.getLineForVertical(areatop);
		int linebot = layout.getLineForVertical(areabot);

		first = layout.getLineStart(linetop);
		last = layout.getLineEnd(linebot);

		LongClickableSpan[] link = buffer.getSpans(off, off, LongClickableSpan.class);

		if (link.length != 0) {
			return link[0];
		} else {
			return null;
		}

	}

	private void setSelection(int first, int last, Spannable buffer, LongClickableSpan longClickableSpan) {
		LongClickableSpan[] link = buffer.getSpans(first, last, LongClickableSpan.class);

		for (int i = 0; i < link.length; ++i) {
			link[i].setSelected(false);
		}

		longClickableSpan.setSelected(true);
	}

	private void setFocused(int first, int last, Spannable buffer, LongClickableSpan longClickableSpan, boolean focused) {
		LongClickableSpan[] link = buffer.getSpans(first, last, LongClickableSpan.class);

		for (int i = 0; i < link.length; ++i) {
			link[i].setSelected(false);
		}

		longClickableSpan.setFocused(focused);
	}

	private void removeSelection(int first, int last, Spannable buffer) {
		LongClickableSpan[] link = buffer.getSpans(first, last, LongClickableSpan.class);

		for (int i = 0; i < link.length; ++i) {
			link[i].setSelected(false);
		}
	}

	private void removeFocus(int first, int last, Spannable buffer) {
		LongClickableSpan[] link = buffer.getSpans(first, last, LongClickableSpan.class);

		for (int i = 0; i < link.length; ++i) {
			link[i].setFocused(false);
		}
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
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
		Log.d("LongClickableSpan", sb.toString());
	}
	
	public static MovementMethod getInstance() {
		if (sInstance == null)
			sInstance = new LongLinkMovementMethod();

		return sInstance;
	}

	private static LongLinkMovementMethod sInstance;
	private static Object FROM_BELOW = new NoCopySpan.Concrete();
}
