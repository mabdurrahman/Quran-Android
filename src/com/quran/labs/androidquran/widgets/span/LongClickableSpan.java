/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.View;

/**
 * If an object of this type is attached to the text of a TextView
 * with a movement method of LinkMovementMethod, the affected spans of
 * text can be selected.  If clicked, the {@link #onClick} method will
 * be called.
 */
public abstract class LongClickableSpan extends TypefaceSpan {
	
	private int bgcolor = 0x00000000;
	private int selectioncolor = 0xFFFF9200;
	private int focuscolor = 0xA0FF9200;
	private boolean selected = false;
	private boolean focused = false;

	public LongClickableSpan(String typeface) {
		super(typeface);
	}
	
	public LongClickableSpan(Parcel parcel) {
		super(parcel);
	}
	
	/**
     * Performs the click action associated with this span.
     */
    public abstract void onClick(View widget);
	
    /**
     * Performs the long click action associated with this span.
     */
    public abstract void onLongClick(View widget);
   
    /**
     * Makes the text underlined and in the link color.
     */
    @Override
    public void updateDrawState(TextPaint ds) {
    	if (selected)
    		ds.bgColor = selectioncolor;
    	else if (focused)
    		ds.bgColor = focuscolor;
    	else
    		ds.bgColor = bgcolor;
        
        ds.setUnderlineText(false);
    }
    
    public void setSelected(boolean selected){
    	this.selected = selected;
    }
    
    public void setFocused(boolean focused){
    	this.focused = focused;
    }
    
    public void setSelectionColor(int selectioncolor){
    	this.selectioncolor = selectioncolor;
    }
    
    public void setFocusColor(int focuscolor){
    	this.focuscolor = focuscolor;
    }
    
    public void setBackgroundColor(int bgcolor){
    	this.bgcolor = bgcolor;
    }
}
