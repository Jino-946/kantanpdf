package org.m946.kantanpdf.samples.pdftable;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.m946.kantanpdf.PageSize;

public class PDFTableSample {

	@Test
	public void test() {
		List<Song> oldies = new ArrayList<Song>();
		oldies.add(new Song("Unchained Melody", "ライチャスブラザーズ", "A・ノース", "H・ザレット", 1965));
		oldies.add(new Song("Runaway", "デル・シャノン", "デル・シャノン", "デル・シャノン", 1961));
		oldies.add(new Song("Diana", "ポール・アンカ", "ポール・アンカ", "ポール・アンカ", 1957));
		oldies.add(new Song("Sherry", "フォーシーズンズ", "ボブ・ゴーディオ", "ボブ・ゴーディオ", 1962));
		oldies.add(new Song("Hey Jude", "ビートルズ", "レノン＝マッカートニー", "レノン=マッカートニー", 1968));
		
		SongPDFTable pdf = new SongPDFTable(PageSize.A4, "なつかしのオールディーズ", oldies);
		try {
			pdf.create("samples/oldies.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
