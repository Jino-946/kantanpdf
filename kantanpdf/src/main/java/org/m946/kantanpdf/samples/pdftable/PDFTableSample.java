package org.m946.kantanpdf.samples.pdftable;


import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.m946.kantanpdf.PageSize;
import org.m946.kantanpdf.RakuRakuPDFTable;
import org.m946.kantanpdf.samples.pdftable.SongPDFTable;

public class PDFTableSample {

	@Test
	public void oldies() {
		List<Song> oldies = SampleService.getSonglist(); 	
		SongPDFTable pdf = new SongPDFTable(PageSize.A4, "なつかしのオールディーズ", oldies);
		try {
			pdf.create("samples/oldies.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void bestSellings() {
		List<BestSelling> records = SampleService.getBestSelling();
		RakuRakuPDFTable<BestSelling> pdf = new RakuRakuPDFTable<BestSelling>(PageSize.A4, "歴代シングル売上", records);
		
		try {
			pdf.create("samples/best.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
