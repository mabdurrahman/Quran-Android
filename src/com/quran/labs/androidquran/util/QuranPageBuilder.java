package com.quran.labs.androidquran.util;

import com.quran.labs.androidquran.R;
import com.quran.labs.androidquran.widgets.span.HeaderURLSpan;
import com.quran.labs.androidquran.widgets.span.LongURLSpan;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

public class QuranPageBuilder {

	private static String page187 = null;
	private static String page188 = null;
	
	static {
		StringBuffer page187Sb = new StringBuffer();
		page187Sb
		.append("&#64396;&#64405;<br>")
		.append("&#64337;&#64338;&#64339;&#64340;&#64341;&#64342;&#64343;&#64344;&#64345;&#64346;<br>")
		.append("&#64347;&#64348;&#64349;&#64350;&#64351;&#64352;&#64353;&#64354;&#64355;<br>")
		.append("&#64356;&#64357;&#64358;&#64359;&#64360;&#64361;&#64362;&#64363;&#64364;&#64365;&#64366;<br>")
		.append("&#64367;&#64368;&#64369;&#64370;&#64371;&#64372;&#64373;&#64374;&#64375;&#64376;&#64377;<br>")
		.append("&#64378;&#64379;&#64380;&#64381;&#64382;&#64383;&#64384;&#64385;&#64386;&#64387;&#64388;<br>")
		.append("&#64389;&#64390;&#64391;&#64392;&#64393;&#64394;&#64395;&#64396;&#64397;&#64398;<br>")
		.append("&#64399;&#64400;&#64401;&#64402;&#64403;&#64404;&#64405;&#64406;&#64407;<br>")
		.append("&#64408;&#64409;&#64410;&#64411;&#64412;&#64413;&#64414;&#64415;&#64416;<br>")
		.append("&#64417;&#64418;&#64419;&#64420;&#64421;&#64422;&#64423;&#64424;&#64425;&#64426;&#64427;<br>")
		.append("&#64428;&#64429;&#64430;&#64431;&#64432;&#64433;<br>")
		.append("&#64467;&#64468;&#64469;&#64470;&#64471;&#64472;&#64473;&#64474;&#64475;<br>")
		.append("&#64476;&#64477;&#64478;&#64479;&#64480;&#64481;&#64482;&#64483;&#64484;&#64485;<br>")
		.append("&#64486;&#64487;&#64488;&#64489;&#64490;&#64491;&#64492;&#64493;<br>")
		.append("&#64494;&#64495;&#64496;&#64497;&#64498;&#64499;&#64500;&#64501;&#64502;&#64503;&#64504;&#64505;");
		
		page187 = page187Sb.toString();
		
		StringBuffer page188Sb = new StringBuffer();
		page188Sb
		.append("&#64337;&#64338;&#64339;&#64340;&#64341;&#64342;&#64343;<br>")
		.append("&#64344;&#64345;&#64346;&#64347;&#64348;&#64349;&#64350;&#64351;&#64352;<br>")
		.append("&#64353;&#64354;&#64355;&#64356;&#64357;&#64358;&#64359;&#64360;&#64361;<br>")
		.append("&#64362;&#64363;&#64364;&#64365;&#64366;&#64367;&#64368;&#64369;&#64370;<br>")
		.append("&#64371;&#64372;&#64373;&#64374;&#64375;&#64376;&#64377;&#64378;<br>")
		.append("&#64379;&#64380;&#64381;&#64382;&#64383;&#64384;&#64385;&#64386;<br>")
		.append("&#64387;&#64388;&#64389;&#64390;&#64391;&#64392;&#64393;&#64394;&#64395;&#64396;&#64397;<br>")
		.append("&#64398;&#64399;&#64400;&#64401;&#64402;&#64403;&#64404;&#64405;&#64406;&#64407;<br>")
		.append("&#64408;&#64409;&#64410;&#64411;&#64412;&#64413;&#64414;<br>")
		.append("&#64415;&#64416;&#64417;&#64418;&#64419;&#64420;&#64421;&#64422;&#64423;&#64424;<br>")
		.append("&#64425;&#64426;&#64427;&#64428;&#64429;&#64430;&#64431;&#64432;<br>")
		.append("&#64433;&#64467;&#64468;&#64469;&#64470;&#64471;&#64472;&#64473;&#64474;<br>")
		.append("&#64475;&#64476;&#64477;&#64478;&#64479;&#64480;&#64481;<br>")
		.append("&#64482;&#64483;&#64484;&#64485;&#64486;&#64487;&#64488;<br>")
		.append("&#64489;&#64490;&#64491;&#64492;&#64493;&#64494;&#64495;&#64496;&#64497;&#64498;");
		
		page188 = page188Sb.toString();
	}
	
	public static SpannableStringBuilder buildPage(Context context, int page){
		
		if (page % 2 == 0) {

			BitmapDrawable header = (BitmapDrawable) context.getResources().getDrawable(R.drawable.header);
			
			Typeface pageTypeFace = QuranUtils.getFontFromSD(context, "QCF_P187.TTF");
			Typeface titleTypeFace = QuranUtils.getMainFontFromSD(context);

			String original = Html.fromHtml(page187).toString();

			SpannableStringBuilder builder = new SpannableStringBuilder(original);
			builder.setSpan(new HeaderURLSpan("/9/title", titleTypeFace, header, 0), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/1", pageTypeFace), 3, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/2", pageTypeFace), 13, 31, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/3", pageTypeFace), 31, 72, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/4", pageTypeFace), 72, 98, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/5", pageTypeFace), 98, 131, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/6", pageTypeFace), 131, 152, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			return builder;
		} else {

			Typeface pageTypeFace = QuranUtils.getFontFromSD(context, "QCF_P188.TTF");

			String original = Html.fromHtml(page188).toString();

			SpannableStringBuilder builder = new SpannableStringBuilder(original);
			builder.setSpan(new LongURLSpan("/9/7", pageTypeFace), 0, 29, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/8", pageTypeFace), 29, 49, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/9", pageTypeFace), 49, 65, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/10", pageTypeFace), 65, 78, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/11", pageTypeFace), 78, 95, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/12", pageTypeFace), 95, 118, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new LongURLSpan("/9/13", pageTypeFace), 118, 143, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			return builder;
		}
	}
}
