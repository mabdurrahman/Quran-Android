package com.quran.labs.androidquran.widgets.span;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.text.style.LineBackgroundSpan;

public class HeaderURLSpan extends LongURLSpan implements LineBackgroundSpan {

	private BitmapDrawable drawable;
	private int line;
	
	public HeaderURLSpan(String url, Typeface typeface, BitmapDrawable drawable, int line) {
    	super(url, typeface);
    	this.drawable = drawable;
    	this.line = line;
    }

    public HeaderURLSpan(Parcel src, Typeface typeface, BitmapDrawable drawable, int line) {
    	super(src, typeface);
    	this.drawable = drawable;
    	this.line = line;
    }
    
    @Override
	public void drawBackground(Canvas c, Paint p, final int left, final int right, final int top, int baseline, final int bottom, CharSequence text, int start, int end, int lnum) {
		//System.out.println("Left: " + left + ", Top: " + top + ", Right: " + right + ", Bottom: " + bottom + ", Baseline: " + baseline + ", Chars: " + text + ", Start: " + start + ", LNum: " + lnum);
		
		if (lnum == line){
			/*p.setColor(0xEEFF0000);
			c.drawRect(new Rect(left, top, right, bottom), p);*/
			//Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.header);
			//bitmap = Bitmap.createScaledBitmap(bitmap, right - left, bottom - top, false);
			//c.drawBitmap(bitmap, left, top, p);
			Bitmap bitmap = Bitmap.createScaledBitmap(drawable.getBitmap(), right - left, bottom - top, false);
			c.drawBitmap(bitmap, left, top, p);
		}
	}
}
