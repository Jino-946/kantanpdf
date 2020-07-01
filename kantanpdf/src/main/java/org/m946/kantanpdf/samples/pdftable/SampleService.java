package org.m946.kantanpdf.samples.pdftable;

import java.util.ArrayList;
import java.util.List;

public class SampleService {
	public static List<Song> getSonglist(){
		List<Song> oldies = new ArrayList<Song>();
		oldies.add(new Song("Unchained Melody", "ライチャスブラザーズ", "A・ノース", "H・ザレット", 1965));
		oldies.add(new Song("Runaway", "デル・シャノン", "デル・シャノン", "デル・シャノン", 1961));
		oldies.add(new Song("Diana", "ポール・アンカ", "ポール・アンカ", "ポール・アンカ", 1957));
		oldies.add(new Song("Sherry", "フォーシーズンズ", "ボブ・ゴーディオ", "ボブ・ゴーディオ", 1962));
		oldies.add(new Song("Hey Jude", "ビートルズ", "レノン＝マッカートニー", "レノン=マッカートニー", 1968));
		return oldies;
	}
	
	
	public static List<BestSelling> getBestSelling(){
		List<BestSelling> list = new ArrayList<BestSelling>();
		list.add(new BestSelling("White Christmas", "Bing Crosby", 50000000L, "1942年"));
		list.add(new BestSelling("Candle in the Wind", "Elton John", 33000000L, "1997年"));
		list.add(new BestSelling("Silent Night", "Bing Crosby", 30000000L, "1935年"));
		list.add(new BestSelling("Rock Around the Clock", "Bill Haley", 25000000L, "1954年"));
		list.add(new BestSelling("We Are The World", "Usa for Africa", 20000000L, "1985年"));

		return list;
	}
}
