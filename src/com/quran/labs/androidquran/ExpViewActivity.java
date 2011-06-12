package com.quran.labs.androidquran;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quran.labs.androidquran.common.GestureQuranActivity;
import com.quran.labs.androidquran.common.QuranGalleryAdapter;
import com.quran.labs.androidquran.data.ApplicationConstants;
import com.quran.labs.androidquran.util.QuranPageBuilder;
import com.quran.labs.androidquran.util.QuranSettings;
import com.quran.labs.androidquran.widgets.GalleryFriendlyScrollView;
import com.quran.labs.androidquran.widgets.QuranTextView;

public class ExpViewActivity extends GestureQuranActivity {
	
	public class QuranGalleryPageAdapter extends QuranGalleryAdapter {
		private Map<String, SoftReference<SpannableStringBuilder>> cache = 
            new HashMap<String, SoftReference<SpannableStringBuilder>>();
		
	    public QuranGalleryPageAdapter(Context context) {
	    	super(context);
	    }
	    
	    @Override
	    public void emptyCache() {
	    	super.emptyCache();
	    	cache.clear();
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	PageHolder holder;
	    	if (convertView == null){
	    		convertView = mInflater.inflate(R.layout.quran_page, null);
				holder = new PageHolder();
				holder.page = (QuranTextView)convertView.findViewById(R.id.pagePinchView);
				holder.scroll = (GalleryFriendlyScrollView)convertView.findViewById(R.id.pageScrollView);
				holder.txtPageNotFound = (TextView)convertView.findViewById(R.id.txtPageNotFound);
				convertView.setTag(holder);
	    	} else {
	    		holder = (PageHolder)convertView.getTag();
	    	}
	    	
	        int page = ApplicationConstants.PAGES_LAST - position;
	        SpannableStringBuilder pageBuilder = getPageBuilder(page);
	        
	        if (pageBuilder == null) {
	        	Log.d("QuranAndroid", "Page not found: " + page);
	        	adjustView(holder, true);
	        } else {
	        	holder.page.setText(pageBuilder);
	        	adjustView(holder, false);
				QuranSettings.getInstance().setLastPage(page);
				QuranSettings.save(prefs);
	        }
	        
			if (!inReadingMode)
				updatePageInfo(position);
			adjustBookmarkView();
			
	    	return convertView;
	    }
	    
	    private void adjustView(PageHolder holder, boolean pageNotFound) {
	    	if (pageNotFound) {
	    		holder.txtPageNotFound.setVisibility(View.VISIBLE);
	        	holder.page.setVisibility(View.GONE);
	    	} else {
	    		holder.txtPageNotFound.setVisibility(View.GONE);
	        	holder.page.setVisibility(View.VISIBLE);
	    	}
	    }
	    
	    private SpannableStringBuilder getPageBuilder(int page) {
	    	SpannableStringBuilder pageBuilder = null;
	    	if (cache.containsKey("page_" + page)){
	        	SoftReference<SpannableStringBuilder> pageBuilderRef = cache.get("page_" + page);
	        	pageBuilder = pageBuilderRef.get();
	        	//Log.d("exp_v", "reading page  " + page + " from cache!");
	        }
	        
	        if (pageBuilder == null){
	        	pageBuilder = QuranPageBuilder.buildPage(context, page);
	        	//Log.d("exp_v", "reading page " + page + " for first time!");
	        	cache.put("page_" + page, new SoftReference<SpannableStringBuilder>(pageBuilder));
	        }
	        
	        return pageBuilder;
	    }
	}
	
	static class PageHolder {
		TextView txtPageNotFound;
		QuranTextView page;
		ScrollView scroll;
	}

	@Override
	protected void initGalleryAdapter() {
		galleryAdapter = new QuranGalleryPageAdapter(this);
	}
	
}
