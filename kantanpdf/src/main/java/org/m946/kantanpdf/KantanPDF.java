package org.m946.kantanpdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

/**
* ・ KantanPDFについて
* <pre>
*   1)KantanPDFインスタンスを生成するとPDFを作成する準備は出来ています。
*     座標を指定し文字列や四角形を自由に描いてください。
*     newPage()を呼び出せばいつでも新しいページが準備されます。
*     (コンストラクタで指定した用紙サイズの範囲外の座標は単純に無視します(^^;
*    
*   2)最後にsaveTo(String path)を呼び出すと引数で指定したpathに
*     PDFが出来ているはずです。

*    注意1) 座標系は用紙の左上を原点としてます。
*     
*    注意2) 座標単位は内部的にはpointを使用してます。
*    1 point = 1 unit = 1/72 inch
*    1inch = 25.4mm = 72 unit
*    
*    //メートル単位またはインチ単位からpointに変換するには
*    //DPIUtilのスタティック・メソッドを使って変換してね。
*    import static org.m946.kantanpdf.DPIUtil.*;
*    float x = cm(5);
*    float y = inch(0.3);
*
* 
*   3) サンプル
*    KantanDF pdf = new KantanPDF(PageSize.A4);
*   pdf 
*   .moveTo(cm(1), cm(10))
*   .lineTo(cm(20), cm(10))
*   .setTextAlign(TextAlign.LowerLeftL)
*   .textOut(cm(5), cm(10), "ベースライン下/左揃え")
*   .setTextAlign(TextAlign.UpperLeft)
*   .textOut(cm(5), cm(10),"ベースライン上/左揃え")
*   .setTextAlign(TextAlign.MiddleLeft)
*   .textOut(cm(8), cm(10), "ベースライン中/左揃え");
*		
*    pdf.saveTo("textalign.pdf");
* </pre>
*/


public class KantanPDF {
	private static DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	@SuppressWarnings("unused")
	private static DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	private static final int HORALIGN_LEFT = 0;
	@SuppressWarnings("unused")
	private static final int HORALIGN_CENTER = 1;
	private static final int HORALIGN_RIGHT = 2;
	
	private static final int VERALIGN_LOWER = 0;
	private static final int VERALIGN_MIDDLE = 1;
	private static final int VERALIGN_UPPER = 2;

	//埋め込み明朝体フォントパス 
	private String minchoFont = "truetype/TakaoPMincho.ttf";
	//埋め込みゴシック体フォントパス
	private String gothicFont = "truetype/TakaoPGothic.ttf";

	/* 埋め込みフォントを使用する時はtrue
	 * (動作環境が変わっても全く同一に表示できるが、ファイルサイズが巨大になるので注意のこと)
	 */
	private boolean useEmbeddedFont = false;
	
	private Document document;
	private ByteArrayOutputStream baos;
	private PdfWriter writer;
	private PdfContentByte canvas;
	
	private BaseFont selectedFont;
	
	
	private boolean documentClosed = false;
	private int fontSize = 12;
	private com.lowagie.text.Rectangle pageSize;

	// デフォルトはベースライン下、左揃え
	private TextAlign textAlign = TextAlign.LowerLeft;
	
	//明朝体を使うときはtrue
	private boolean isMincho = false;
	
	/** レフト・マージン */
	private float lm = 18f;
	/** トップ・マージン */
	private float tm = 18f;
	/** 罫線と文字列が重ならないようする間隔 */
	private static float TEXTOFFSET = 2f;

	private boolean autoPageNumber = false;
	private int pageNumber = 1;
	
	/**
	 * 指定したX座標にレフトマージンを加える
	 * 
	 * @param x 座標
	 * @return レフトマージンを加えた座標
	 */
	private float getX(float x){
		return  lm + x;
	}

	/**
	 * 指定したY座標にトップマージンを加える
	 * 
	 * 
	 * @param y 座標
	 * @return トップマージンを加えた座標
	 */
	private float getY(float y){
		return pageSize.getHeight() - (tm + y);
	}
	
	private BaseFont selectFont(){
		BaseFont font = null;
		try {
			if (useEmbeddedFont){
				font = isMincho ? BaseFont.createFont(minchoFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED):
						 		BaseFont.createFont(gothicFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			}else {
				font = isMincho ? BaseFont.createFont("HeiseiMin-W3", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED):
					 			BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
			}
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		return font;
	}

	
	public KantanPDF(com.lowagie.text.Rectangle pageSize) {
		this.pageSize = pageSize;
		document = new Document(this.pageSize);
		baos = new ByteArrayOutputStream();
		try {
			writer = PdfWriter.getInstance(document, baos);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.open();
		canvas = writer.getDirectContent();
		selectedFont = selectFont();
		newPage();
	}
	

	public byte[] getByteArray() {
		close();
		return baos.toByteArray();
	}

	
	public void saveTo(String path) {
		close();
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(baos.toByteArray());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (!documentClosed){
			document.close();
			documentClosed = true;
		}
	}


    /**
     * 垂直のテキストアラインを決定する<br><br>
     * 
     * PdfContentByte#showTextAligned(int, String, float, float, float)<br/>
     * は水平方向のテキストアラインのみサポートしているため、垂直なアラインは
     * このメソッドの戻値と、出力する文字の文字高を元に計算によって求めること。
     * 
     * @return 垂直方向のテキストアライン
     */
	private int getVerticalAlign(){
		if (textAlign.ordinal() < TextAlign.MiddleLeft.ordinal()){
			return VERALIGN_LOWER;
		}else if (textAlign.ordinal() <  TextAlign.UpperLeft.ordinal()) {
			return VERALIGN_MIDDLE;
		}
		return VERALIGN_UPPER;
	}

		
	
	/**
	 * 水平のテキストアラインを決定する
	 * 
	 * @return　水平方向のテキストアライン
	 */
	private int getHorizontalAlign(){
		if (textAlign.ordinal() < TextAlign.MiddleLeft.ordinal()) {
			return textAlign.ordinal();
		}else if (textAlign.ordinal() < TextAlign.UpperLeft.ordinal()) {
			return textAlign.ordinal() - TextAlign.MiddleLeft.ordinal();
		}
		
		return textAlign.ordinal() - TextAlign.UpperLeft.ordinal();
	
	}
		
	/**
	 * 出力する文字列の文字高を求める。
	 * 
	 * @return 文字高
	 */
	private float getTextheight(){
		float ascend = selectedFont.getFontDescriptor(BaseFont.ASCENT, fontSize);
		float descend = selectedFont.getFontDescriptor(BaseFont.DESCENT, fontSize);
	
		return ascend - descend;
	}
		
	
	/**
	 * 指定した座標に文字列を出力する。
	 * 
	 * @param x 座標
	 * @param y 座標
	 * @param text 出力する文字列
	 */
	private void _textout(float x, float y, String text){
		canvas.beginText();
		canvas.setFontAndSize(selectedFont, fontSize);
		float xpos = getX(x);
		int horAlign = getHorizontalAlign();
		if (horAlign == HORALIGN_LEFT){
			xpos += TEXTOFFSET;
		} else if (horAlign == HORALIGN_RIGHT){
			xpos -= TEXTOFFSET;
		}
		
		float ypos = getY(y);
		int verAlign = getVerticalAlign();
		if (verAlign == VERALIGN_UPPER){
			ypos -= getTextheight() -TEXTOFFSET ;
		}else if (verAlign == VERALIGN_MIDDLE){
			ypos -= getTextheight() / 2f - TEXTOFFSET;
		}else {
			ypos += TEXTOFFSET;
		}
	
		canvas.showTextAligned(horAlign, text, xpos, ypos, 0);
		canvas.endText();
	}
	
	
	
	public KantanPDF textOut(float x, float y, String text) {
		_textout(x, y, text);
		return this;
	}

	
	
	public KantanPDF textOut(float x, float y, double val) {
		String text = String.format("%1$,d", (long)val);
		textAlign = TextAlign.LowerRight;
		_textout(x, y, text);
		return this;
	}

	
	public KantanPDF textOut(float x, float y, long val) {
		String text = String.format("%1$,d", (long)val);
		textAlign = TextAlign.LowerRight;
		_textout(x, y, text);
		return this;	
	}

	
	public KantanPDF textOut(float x, float y, LocalDate date) {
		String text = date.format(DEFAULT_DATE_FORMAT);
		textAlign = TextAlign.LowerCenter;
		_textout(x, y, text);
		
		return this;
	}

	
	private void printPageNumber() {
		float x = pageSize.getWidth() / 2;
		float y = pageSize.getHeight() - DPIUtil.cm(1d);
		textAlign = TextAlign.MiddleCenter; 
		_textout(x, y, "- " + pageNumber + " -");
		pageNumber++;
	}

	
	public KantanPDF newPage() {
		document.newPage();
		if (autoPageNumber) {
			printPageNumber();
		}
		return this;
	}

	
	public KantanPDF moveTo(float x, float y) {
		canvas.moveTo(getX(x), getY(y));
		return this;
	}

	
	public KantanPDF lineTo(float x, float y) {
		canvas.lineTo(getX(x), getY(y));
		canvas.stroke();
		return this;
	}

	
	public KantanPDF rectangle(float left, float top, float right, float bottom) {
		canvas.rectangle(getX(left), getY(top), right - left, getY(bottom) - getY(top));
		canvas.stroke();
		return this;
	
	}

	
	
	public KantanPDF rectangle(float left, float top, float right, float bottom, int red, int green, int blue) {
		canvas.saveState();
		canvas.setRGBColorFill(red, green, blue);
		canvas.rectangle(getX(left), getY(top), right - left, getY(bottom) - getY(top));
		canvas.fill();
		canvas.restoreState();
		return this;
	}

	
	public KantanPDF rectangle(float left, float top, float right, float bottom, Color col) {
		canvas.saveState();
		canvas.setRGBColorFill(col.getRed(), col.getGreen(), col.getBlue());
		canvas.rectangle(getX(left), getY(top), right - left, getY(bottom) - getY(top));
		canvas.fill();
		canvas.restoreState();
		return this;
	
	}

	
	public PdfOutline addChapter(String title) {
		PdfOutline chapter = new PdfOutline(canvas.getRootOutline(), new PdfDestination(PdfDestination.FIT), title);
		
		return chapter;
	}

	
	public PdfOutline addSection(PdfOutline chapter, String title) {
		PdfOutline section = new PdfOutline(chapter, new PdfDestination(PdfDestination.FIT), title);
		return section;
	}

	
	public KantanPDF imageOut(String filename, float x, float y, float scale) {
		try {
			Image img = Image.getInstance(filename);
			float yPos = pageSize.getHeight() - img.getScaledHeight() / scale - y - tm;
			img.setAbsolutePosition(getX(x), yPos);
			img.scaleAbsolute(img.getWidth() / scale, img.getHeight() / scale);
			canvas.addImage(img);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return this;
	}

	
	public KantanPDF imageOut(String filename, float x, float y) {
		return imageOut(filename, x, y, 1f);
	}

	
	public KantanPDF imageOut(byte[] image, float x, float y, float scale) {
		try {
			Image img = Image.getInstance(image);
			float yPos = pageSize.getHeight() - img.getScaledHeight() / scale - y - tm;
			img.setAbsolutePosition(getX(x), yPos);
			img.scaleAbsolute(img.getWidth() / scale, img.getHeight() / scale);
			canvas.addImage(img);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return this;
	}
		
	
	public KantanPDF imageOut(byte[] image, float x, float y) {
		return imageOut(image, x, y, 1f);
	}

	
	public KantanPDF setFontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	
	public KantanPDF setTextAlign(TextAlign ta) {
		this.textAlign = ta;
		return this;
	}

	
	public KantanPDF setFontMincho() {
		isMincho = true;
		selectedFont = selectFont();
		return this;
	}

		
	public KantanPDF setFontGothic() {
		isMincho = false;
		selectedFont = selectFont();
		return this;
	}

	
	public KantanPDF setLineWidth(float lineWidth) {
		canvas.setLineWidth(lineWidth);
		return this;
	}

	
	public KantanPDF setLeftMargin(float lm) {
		this.lm = lm;
		return this;
	}

	
	public KantanPDF setTopMargin(float tm) {
		this.tm = tm;
		return this;
	}

	
	public KantanPDF setUseEmbeddedFont(boolean value) {
		useEmbeddedFont = value;
		selectedFont = selectFont();
		return this;
	}
	
	public KantanPDF enableAutoPageNumber() {
		autoPageNumber = true;
		return this;
	}
}
