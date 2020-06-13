package org.m946.kantanpdf;


import org.junit.Test;

//import org.m946.kantanpdf.PageSize;
import static org.m946.kantanpdf.DPIUtil.*;
public class DesktopPathTest {

	@Test
	public void test() {
		System.out.println(System.getProperty("user.home"));
	
		    KantanPDF pdf = new KantanPDF(PageSize.A4);
		    pdf.enableAutoPageNumber()
		   .newPage()
		   .moveTo(cm(1), cm(10))
		   .lineTo(cm(20), cm(10))
		   .setTextAlign(TextAlign.LowerLeft)
		   .textOut(cm(5), cm(10), "下ベース 左揃え")
		   .setTextAlign(TextAlign.UpperLeft)
		   .textOut(cm(5), cm(10),"上ベース 左揃え")
		   .setTextAlign(TextAlign.MiddleLeft)
		   .textOut(cm(12), cm(10), "中ベース 左揃え")
		   .setTextAlign(TextAlign.MiddleRight)
		   .textOut(cm(12), cm(10), "中ベース 右揃え")
		   .newPage()
		   .newPage();
		    pdf.saveTo("textalign.pdf");
	}

}
