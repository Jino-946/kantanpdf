package org.m946.kantanpdf;


import org.junit.Test;

import com.lowagie.text.PageSize;
import static org.m946.kantanpdf.DPIUtil.*;

public class DesktopPathTest {

	@Test
	public void test() {
		System.out.println(System.getProperty("user.home"));
	
		    KantanPDF pdf = new KantanOpenPDF(PageSize.A4);
		    pdf.newPage()
		   .moveTo(cm(1), cm(10))
		   .lineTo(cm(20), cm(10))
		   .setTextAlign(KantanPDF.TEXTPOS_LL)
		   .textOut(cm(5), cm(10), "TEXTPOS_LL")
		   .setTextAlign(KantanPDF.TEXTPOS_UL)
		   .textOut(cm(5), cm(10),"TEXTPOS_UL")
		   .setTextAlign(KantanPDF.TEXTPOS_ML)
		   .textOut(cm(8), cm(10), "真ん中 左揃え");
				
		    pdf.saveTo("textalign.pdf");
		
		
	}

}
