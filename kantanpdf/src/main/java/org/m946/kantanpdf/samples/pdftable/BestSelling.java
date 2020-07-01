package org.m946.kantanpdf.samples.pdftable;

import org.m946.kantanpdf.PDFColumn;

import com.lowagie.text.pdf.PdfContentByte;

@lombok.AllArgsConstructor
public class BestSelling {
	@PDFColumn(name = "曲名", order = 0, textAlign = PdfContentByte.ALIGN_LEFT, width = 5f)
	private String song;
	
	@PDFColumn(name = "アーティスト", order = 1, textAlign = PdfContentByte.ALIGN_LEFT, width = 4f)
	private String artist;
	
	@PDFColumn(name = "売上(千枚)", order = 2, textAlign = PdfContentByte.ALIGN_RIGHT, width = 4f)
	private Long sales;
	
	@PDFColumn(name = "リリース", order = 3, textAlign = PdfContentByte.ALIGN_CENTER, width = 3f)
	private String releaseYear;
	
}
