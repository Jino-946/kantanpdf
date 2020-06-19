package org.m946.kantanpdf.samples;
import org.junit.Test;


import org.m946.kantanpdf.KantanPDF;
import org.m946.kantanpdf.PageSize;
import org.m946.kantanpdf.TextAlign;

import static org.m946.kantanpdf.DPIUtil.*;

import java.io.IOException;

@lombok.extern.slf4j.Slf4j
public class SamplePDF {
	@Test
	public void helloWorld() throws IOException {
		KantanPDF pdf = new KantanPDF(PageSize.A4);
		pdf.textOut(0, 0, "Hello, world")
			.saveTo("samples/helloworld.pdf");
	}

	// A4サイズ 210 x 297mm
	@Test
	public void helloPDF() throws IOException {
		log.error("Error だよ");
		log.debug("there.");
		KantanPDF pdf = new KantanPDF(PageSize.A4.rotate());
		log.info("here!!");
		pdf.setTextAlign(TextAlign.LowerCenter)
			.setFontSize(30)
			.textOut(cm(29.7f / 2f), cm(21f / 2f), "Hello, PDF");
		log.error("Hi");
		pdf.saveTo("samples/hellopdf.pdf");
	}
}
