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

package com.quran.labs.androidquran.widgets.span;

import com.quran.labs.androidquran.widgets.QuranTextView;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;
import android.view.View;

public class LongURLSpan extends LongClickableSpan {

	public static final int LONG_URL_SPAN = 19;
	
    private final String mURL;
    private final Typeface newType;

    public LongURLSpan(String url, Typeface typeface) {
    	super(url);
    	mURL = url;
        newType = typeface;
    }

    public LongURLSpan(Parcel src, Typeface typeface) {
    	super(src);
        mURL = src.readString();
        newType = typeface;
    }
    
    public int getSpanTypeId() {
        return LONG_URL_SPAN;
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
    	if (widget instanceof QuranTextView){
			((QuranTextView) widget).onAyaClick(getURL());
		}
    }
	
	@Override
    public void onLongClick(View widget) {
		if (widget instanceof QuranTextView){
			((QuranTextView) widget).onAyaLongClick(getURL());
		}
    }
	
	@Override
    public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
    	applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
    	super.updateMeasureState(paint);
        applyCustomTypeFace(paint, newType);
    }

    private void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
