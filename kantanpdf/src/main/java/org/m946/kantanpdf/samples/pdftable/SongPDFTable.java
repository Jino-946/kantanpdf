package org.m946.kantanpdf.samples.pdftable;

import java.util.Arrays;
import java.util.List;

import org.m946.kantanpdf.AbstractPDFTable;
import org.m946.kantanpdf.TextAlign;

import static org.m946.kantanpdf.DPIUtil.*;

import com.lowagie.text.Rectangle;

public class SongPDFTable extends AbstractPDFTable<Song> {

	public SongPDFTable(Rectangle paperSize, String title, List<Song> data) {
		super(paperSize, title, data);
	}

	@Override
	protected void buildFixedColumns() {
		colWidths = Arrays.asList(cm(3.5), cm(3.5), cm(4.5), cm(4.5), cm(2));
		colCount = colWidths.size();
		fixedColNames = Arrays.asList("曲名", "演奏", "作曲", "作詞", "リリース");
	}

	@Override
	protected void printRow(float y, int idx) {
		 float x = tableTopLeft.getX();
		 pdf.setTextAlign(TextAlign.LowerLeft);
		 pdf.textOut(x, y, data.get(idx).getSongTitle());
		 x += colWidths.get(0);
		 pdf.textOut(x, y, data.get(idx).getArtist());
		 x += colWidths.get(1);
		 pdf.textOut(x, y, data.get(idx).getMusic());
		 x += colWidths.get(2);
		 pdf.textOut(x, y, data.get(idx).getWords());
		 x += colWidths.get(3);
		 pdf.textOut(x, y, data.get(idx).getReleaseYear().toString());
	}

}
