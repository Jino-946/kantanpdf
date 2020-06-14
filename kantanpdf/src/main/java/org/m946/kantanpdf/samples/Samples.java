package org.m946.kantanpdf.samples;
import org.junit.Test;
import org.m946.kantanpdf.KantanPDF;
import org.m946.kantanpdf.PageSize;
import org.m946.kantanpdf.TextAlign;

import static org.m946.kantanpdf.DPIUtil.*;

public class Samples {
	@Test
	public void helloWorld() {
		KantanPDF pdf = new KantanPDF(PageSize.A4);
		pdf.textOut(0, 0, "Hello, world")
			.saveTo("samples/helloworld.pdf");
	}

	// A4サイズ 210 x 297mm
	@Test
	public void helloPDF() {
		KantanPDF pdf = new KantanPDF(PageSize.A4.rotate());
		pdf.setTextAlign(TextAlign.LowerCenter)
			.setFontSize(30)
			.textOut(cm(29.7f / 2f), cm(21f / 2f), "Hello, PDF");

		pdf.saveTo("samples/hellopdf.pdf");
	}
}
